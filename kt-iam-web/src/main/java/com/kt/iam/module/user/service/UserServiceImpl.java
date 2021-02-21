package com.kt.iam.module.user.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.logger.CatchAndLog;
import com.kt.iam.common.util.Assert;
import com.kt.iam.enums.BizEnums;
import com.kt.iam.enums.DeletedEnums;
import com.kt.iam.enums.PermissionTypeEnums;
import com.kt.iam.enums.UserStatusEnums;
import com.kt.iam.module.permission.persistence.IamPermission;
import com.kt.iam.module.permission.service.IPermissionService;
import com.kt.iam.module.permission.vo.PermissionVO;
import com.kt.iam.module.user.persistence.IamUserRoleRel;
import com.kt.iam.module.role.service.IRoleService;
import com.kt.iam.module.user.converter.UserBeanConverter;
import com.kt.iam.module.user.dto.UserPageListSearchDTO;
import com.kt.iam.module.user.dto.UserUpdateDTO;
import com.kt.iam.module.user.persistence.IamUser;
import com.kt.iam.module.user.persistence.dao.IamUserMapper;
import com.kt.iam.module.user.persistence.dao.IamUserRoleRelMapper;
import com.kt.iam.module.user.vo.UserDetailVO;
import com.kt.iam.module.user.vo.UserPageListVO;
import com.kt.iam.module.usergroup.persistence.IamUserGroupUserRel;
import com.kt.iam.module.usergroup.persistence.dao.IamUserGroupUserRelMapper;
import com.kt.iam.module.usergroup.service.IUserGroupService;
import com.kt.iam.auth.core.model.LoginUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
@Service
@CatchAndLog
@Slf4j
public class UserServiceImpl extends ServiceImpl<IamUserMapper, IamUser> implements IUserService {

    @Autowired
    private IPermissionService iPermissionService;
    @Autowired
    private IUserPermissionService iUserPermissionService;
    @Autowired
    private IamUserRoleRelMapper iamUserRoleRelMapper;
    @Autowired
    private IamUserGroupUserRelMapper iamUserGroupUserRelMapper;
    @Autowired
    private UserBeanConverter beanConverter;
    @Autowired
    private IRoleService iRoleService;
    @Autowired
    private IUserGroupService iUserGroupService;

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20000)
    public void saveUser(UserUpdateDTO dto) {
        IamUser iamUser = beanConverter.convertToUserDO(dto);

        doCheckBeforeSave(iamUser);

        this.save(iamUser);
        Long userId = iamUser.getId();

        doSaveUserRoleRelation(userId, dto.getRoleIds());

        doSaveUserUserGroupRelation(userId, dto.getUserGroupIds());
    }

    private void doSaveUserUserGroupRelation(Long userId, List<Long> userGroupIds) {
        if (CollectionUtil.isNotEmpty(userGroupIds)) {
            iamUserGroupUserRelMapper.batchSaveRelation(userId, userGroupIds);
        }
    }

    private void doSaveUserRoleRelation(Long userId, List<Long> roleIds) {
        if (CollectionUtil.isNotEmpty(roleIds)) {
            iamUserRoleRelMapper.batchSaveRelation(userId, roleIds);
        }
    }

    @Override
    public int countUserByCode(String code) {
        return this.count(new LambdaQueryWrapper<>(IamUser.class).eq(IamUser::getCode, code));
    }

    private void doCheckBeforeSave(IamUser user) {
        int count = countUserByPhone(user.getPhone());
        Assert.isTrue(count > 0, BizEnums.USER_ALREADY_EXISTS);
    }

    private int countUserByPhone(String phone) {
        return this.count(new LambdaQueryWrapper<>(IamUser.class).eq(IamUser::getPhone, phone));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserById(UserUpdateDTO dto) {
        IamUser iamUser = beanConverter.convertToUpdateUserDO(dto);
        Long userId = iamUser.getId();

        // 先把原本角色和用户组清空
        removeUserRoleRelation(userId);

        removeUserUserGroupRelation(userId);

        // 重新保存
        doSaveUserRoleRelation(userId, dto.getRoleIds());

        doSaveUserUserGroupRelation(userId, dto.getUserGroupIds());

        this.updateById(iamUser);
    }

    private void removeUserUserGroupRelation(Long userId) {
        final LambdaQueryWrapper<IamUserGroupUserRel> eq = new LambdaQueryWrapper<IamUserGroupUserRel>()
                .eq(IamUserGroupUserRel::getUserId, userId);
        iamUserGroupUserRelMapper.delete(eq);
    }

    private void removeUserRoleRelation(Long userId) {
        LambdaQueryWrapper<IamUserRoleRel> eq = new LambdaQueryWrapper<IamUserRoleRel>()
                .eq(IamUserRoleRel::getUserId, userId);
        iamUserRoleRelMapper.delete(eq);
    }

    @Override
    public Page<UserPageListVO> pageList(UserPageListSearchDTO params) {
        IPage<IamUser> result = this.page(new Page<>(params.getCurrent(), params.getSize()), new QueryWrapper<IamUser>()
                .like(StrUtil.isNotBlank(params.getPhone()), "phone", params.getPhone())
                .like(StrUtil.isNotBlank(params.getName()), "name", params.getName())
                .select("id", "phone", "name", "status"));
        List<IamUser> records = result.getRecords();
        List<UserPageListVO> vos = records.stream().map(beanConverter::convertToUserPageListVO).collect(Collectors.toList());
        Page<UserPageListVO> pageVo = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        pageVo.setRecords(vos);
        return pageVo;
    }

    @Override
    public void updateStatus(UserUpdateDTO userUpdateDTO) {
        updateStatus(userUpdateDTO, UserStatusEnums.ENABLED);
    }

    @Override
    public IamUser getUserByPhone(String phone) {
        LambdaQueryWrapper<IamUser> qw = new LambdaQueryWrapper<IamUser>()
                .eq(IamUser::getPhone, phone);
        return this.getOne(qw);
    }

    @Override
    public UserDetailVO getUserDetailVOById(Long userId) {
        IamUser user = getUserById(userId);
        return beanConverter.convertToUserDetailVO(user);
    }

    @Override
    public User getUserInfoByPhone(String phone) {
        IamUser user = getUserByPhone(phone);
        if (user == null) {
            return null;
        }
        Long userId = user.getId();
        List<IamPermission> userPermissions = iUserPermissionService.getUserPermissions(userId, PermissionTypeEnums.FRONT_ROUTE);
        List<SimpleGrantedAuthority> grantedAuthorities = userPermissions.stream()
                .map(item -> new SimpleGrantedAuthority(String.format("ROLE_%s", item.getCode())))
                .collect(Collectors.toList());
        return new LoginUserDetails(iUserPermissionService.isSuperAdmin(user.getCode()), user.getId(), user.getCode(),
                user.getName(), user.getPassword(), grantedAuthorities);
    }

    @Override
    public List<PermissionVO> getUserElements(String userCode) {
        List<IamPermission> userRoutePermissions;
        // 超管直接赋予所有权限
        if (iUserPermissionService.isSuperAdmin(userCode)) {
            userRoutePermissions = iPermissionService.getAllPermissionsByType(PermissionTypeEnums.PAGE_ELEMENT);
        } else {
            IamUser user = getUserByCode(userCode);
            userRoutePermissions = iUserPermissionService.getUserPermissions(user.getId(), PermissionTypeEnums.PAGE_ELEMENT);
        }
        return userRoutePermissions.stream().map(beanConverter::convertToPermissionVO).collect(Collectors.toList());
    }

    @Override
    public IamUser getUserById(Long userId) {
        return Optional.ofNullable(this.getById(userId)).orElseGet(IamUser::new);
    }

    @Override
    public IamUser getUserByCode(String userCode) {
        LambdaQueryWrapper<IamUser> qw = new LambdaQueryWrapper<>();
        qw.select(IamUser::getCode, IamUser::getName, IamUser::getId);
        qw.eq(IamUser::getCode, userCode);
        qw.eq(IamUser::getStatus, UserStatusEnums.ENABLED.getValue());
        return Optional.ofNullable(this.getOne(qw)).orElseGet(IamUser::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUserById(Long id) {
        LambdaUpdateWrapper<IamUser> qw = new LambdaUpdateWrapper<>();
        qw.eq(IamUser::getId, id);
        qw.eq(IamUser::getIsDeleted, DeletedEnums.NOT.getCode());
        qw.set(IamUser::getIsDeleted, id);
        this.update(qw);

        // 移除角色关系
        iRoleService.removeUserRoleRelByUserId(id);
        // 移除用户组关系
        iUserGroupService.removeUserGroupUserRelByUserId(id);
    }

    private void updateStatus(UserUpdateDTO userUpdateDTO, UserStatusEnums statusEnum) {
        this.update(new LambdaUpdateWrapper<IamUser>()
                .eq(IamUser::getStatus, userUpdateDTO.getId())
                .set(IamUser::getStatus, statusEnum.getValue()));
    }

}
