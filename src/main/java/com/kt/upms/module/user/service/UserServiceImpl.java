package com.kt.upms.module.user.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.logger.CatchAndLog;
import com.kt.upms.common.util.Assert;
import com.kt.upms.enums.BizEnums;
import com.kt.upms.enums.DeletedEnums;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.enums.UserStatusEnums;
import com.kt.upms.module.permission.persistence.UpmsPermission;
import com.kt.upms.module.permission.service.IPermissionService;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.user.persistence.UpmsUserRoleRel;
import com.kt.upms.module.role.service.IRoleService;
import com.kt.upms.module.user.converter.UserBeanConverter;
import com.kt.upms.module.user.dto.UserPageListSearchDTO;
import com.kt.upms.module.user.dto.UserUpdateDTO;
import com.kt.upms.module.user.persistence.UpmsUser;
import com.kt.upms.module.user.persistence.dao.UpmsUserMapper;
import com.kt.upms.module.user.persistence.dao.UpmsUserRoleRelMapper;
import com.kt.upms.module.user.vo.UserDetailVO;
import com.kt.upms.module.user.vo.UserPageListVO;
import com.kt.upms.module.usergroup.persistence.UpmsUserGroupUserRel;
import com.kt.upms.module.usergroup.persistence.dao.UpmsUserGroupUserRelMapper;
import com.kt.upms.module.usergroup.service.IUserGroupService;
import com.kt.upms.security.login.LoginUserDetails;
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
public class UserServiceImpl extends ServiceImpl<UpmsUserMapper, UpmsUser> implements IUserService {

    @Autowired
    private IPermissionService iPermissionService;
    @Autowired
    private IUserPermissionService iUserPermissionService;
    @Autowired
    private UpmsUserRoleRelMapper upmsUserRoleRelMapper;
    @Autowired
    private UpmsUserGroupUserRelMapper upmsUserGroupUserRelMapper;
    @Autowired
    private UserBeanConverter beanConverter;
    @Autowired
    private IRoleService iRoleService;
    @Autowired
    private IUserGroupService iUserGroupService;

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20000)
    public void saveUser(UserUpdateDTO dto) {
        UpmsUser upmsUser = beanConverter.convertToUserDO(dto);

        doCheckBeforeSave(upmsUser);

        this.save(upmsUser);
        Long userId = upmsUser.getId();

        doSaveUserRoleRelation(userId, dto.getRoleIds());

        doSaveUserUserGroupRelation(userId, dto.getUserGroupIds());
    }

    private void doSaveUserUserGroupRelation(Long userId, List<Long> userGroupIds) {
        if (CollectionUtil.isNotEmpty(userGroupIds)) {
            upmsUserGroupUserRelMapper.batchSaveRelation(userId, userGroupIds);
        }
    }

    private void doSaveUserRoleRelation(Long userId, List<Long> roleIds) {
        if (CollectionUtil.isNotEmpty(roleIds)) {
            upmsUserRoleRelMapper.batchSaveRelation(userId, roleIds);
        }
    }

    @Override
    public int countUserByCode(String code) {
        return this.count(new LambdaQueryWrapper<>(UpmsUser.class).eq(UpmsUser::getCode, code));
    }

    private void doCheckBeforeSave(UpmsUser user) {
        int count = countUserByPhone(user.getPhone());
        Assert.isTrue(count > 0, BizEnums.USER_ALREADY_EXISTS);
    }

    private int countUserByPhone(String phone) {
        return this.count(new LambdaQueryWrapper<>(UpmsUser.class).eq(UpmsUser::getPhone, phone));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserById(UserUpdateDTO dto) {
        UpmsUser upmsUser = beanConverter.convertToUserDO(dto);
        Long userId = upmsUser.getId();

        // 先把原本角色和用户组清空
        removeUserRoleRelation(userId);

        removeUserUserGroupRelation(userId);

        doSaveUserRoleRelation(userId, dto.getRoleIds());

        doSaveUserUserGroupRelation(userId, dto.getUserGroupIds());

        this.updateById(upmsUser);
    }

    private void removeUserUserGroupRelation(Long userId) {
        final LambdaQueryWrapper<UpmsUserGroupUserRel> eq = new LambdaQueryWrapper<UpmsUserGroupUserRel>()
                .eq(UpmsUserGroupUserRel::getUserId, userId);
        upmsUserGroupUserRelMapper.delete(eq);
    }

    private void removeUserRoleRelation(Long userId) {
        LambdaQueryWrapper<UpmsUserRoleRel> eq = new LambdaQueryWrapper<UpmsUserRoleRel>()
                .eq(UpmsUserRoleRel::getUserId, userId);
        upmsUserRoleRelMapper.delete(eq);
    }

    @Override
    public Page<UserPageListVO> pageList(UserPageListSearchDTO params) {
        IPage<UpmsUser> result = this.page(new Page<>(params.getCurrent(), params.getSize()), new QueryWrapper<UpmsUser>()
                .like(StrUtil.isNotBlank(params.getPhone()), "phone", params.getPhone())
                .like(StrUtil.isNotBlank(params.getName()), "name", params.getName())
                .select("id", "phone", "name", "status"));
        List<UpmsUser> records = result.getRecords();
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
    public UpmsUser getUserByPhone(String phone) {
        LambdaQueryWrapper<UpmsUser> qw = new LambdaQueryWrapper<UpmsUser>()
                .eq(UpmsUser::getPhone, phone);
        return this.getOne(qw);
    }

    @Override
    public UserDetailVO getUserDetailVOById(Long userId) {
        UpmsUser user = getUserById(userId);
        return beanConverter.convertToUserDetailVO(user);
    }

    @Override
    public User getUserInfoByPhone(String phone) {
        UpmsUser user = getUserByPhone(phone);
        if (user == null) {
            return null;
        }
        Long userId = user.getId();
        List<UpmsPermission> userPermissions = iUserPermissionService.getUserPermissions(userId, PermissionTypeEnums.FRONT_ROUTE);
        List<SimpleGrantedAuthority> grantedAuthorities = userPermissions.stream()
                .map(item -> new SimpleGrantedAuthority(String.format("ROLE_%s", item.getCode())))
                .collect(Collectors.toList());
        return new LoginUserDetails(iUserPermissionService.isSuperAdmin(user.getCode()), user.getId(), user.getCode(),
                user.getName(), user.getPassword(), grantedAuthorities);
    }

    @Override
    public List<PermissionVO> getUserElements(String userCode) {
        List<UpmsPermission> userRoutePermissions;
        // 超管直接赋予所有权限
        if (iUserPermissionService.isSuperAdmin(userCode)) {
            userRoutePermissions = iPermissionService.getAllPermissionsByType(PermissionTypeEnums.PAGE_ELEMENT);
        } else {
            UpmsUser user = getUserByCode(userCode);
            userRoutePermissions = iUserPermissionService.getUserPermissions(user.getId(), PermissionTypeEnums.PAGE_ELEMENT);
        }
        return userRoutePermissions.stream().map(beanConverter::convertToPermissionVO).collect(Collectors.toList());
    }

    @Override
    public UpmsUser getUserById(Long userId) {
        return Optional.ofNullable(this.getById(userId)).orElseGet(UpmsUser::new);
    }

    @Override
    public UpmsUser getUserByCode(String userCode) {
        LambdaQueryWrapper<UpmsUser> qw = new LambdaQueryWrapper<>();
        qw.select(UpmsUser::getCode, UpmsUser::getName, UpmsUser::getId);
        qw.eq(UpmsUser::getCode, userCode);
        qw.eq(UpmsUser::getStatus, UserStatusEnums.ENABLED.getValue());
        return Optional.ofNullable(this.getOne(qw)).orElseGet(UpmsUser::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUserById(Long id) {
        LambdaUpdateWrapper<UpmsUser> qw = new LambdaUpdateWrapper<>();
        qw.eq(UpmsUser::getId, id);
        qw.eq(UpmsUser::getIsDeleted, DeletedEnums.NOT.getCode());
        qw.set(UpmsUser::getIsDeleted, id);
        this.update(qw);

        // 移除角色关系
        iRoleService.removeUserRoleRelByUserId(id);
        // 移除用户组关系
        iUserGroupService.removeUserUserGroupRelByUserId(id);
    }

    private void updateStatus(UserUpdateDTO userUpdateDTO, UserStatusEnums statusEnum) {
        this.update(new LambdaUpdateWrapper<UpmsUser>()
                .eq(UpmsUser::getStatus, userUpdateDTO.getId())
                .set(UpmsUser::getStatus, statusEnum.getValue()));
    }

}
