package com.kt.upms.module.user.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.logger.CatchAndLog;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.entity.UpmsUser;
import com.kt.upms.entity.UpmsUserGroupUserRel;
import com.kt.upms.entity.UpmsUserRoleRel;
import com.kt.upms.enums.BizEnums;
import com.kt.upms.enums.UserStatusEnums;
import com.kt.upms.mapper.UpmsUserGroupUserRelMapper;
import com.kt.upms.mapper.UpmsUserMapper;
import com.kt.upms.mapper.UpmsUserRoleRelMapper;
import com.kt.upms.module.permission.service.IPermissionService;
import com.kt.upms.module.role.service.IUpmsRoleService;
import com.kt.upms.module.user.dto.UserAddDTO;
import com.kt.upms.module.user.dto.UserPageListSearchDTO;
import com.kt.upms.module.user.dto.UserUpdateDTO;
import com.kt.upms.module.user.vo.UserDetailVO;
import com.kt.upms.module.user.vo.UserPageListVO;
import com.kt.upms.module.user.converter.UserBeanConverter;
import com.kt.upms.module.usergroup.service.IUpmsUserGroupService;
import com.kt.upms.security.DefaultUser;
import com.kt.upms.support.IUserPasswordHelper;
import com.kt.upms.util.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    private IUpmsRoleService iUpmsRoleService;
    @Autowired
    private IUpmsUserGroupService iUpmsUserGroupService;
    @Autowired
    private IPermissionService iUpmsPermissionService;
    @Autowired
    private IUserPasswordHelper iUserPasswordHelper;
    @Autowired
    private UpmsUserRoleRelMapper upmsUserRoleRelMapper;
    @Autowired
    private UpmsUserGroupUserRelMapper upmsUserGroupUserRelMapper;
    @Autowired
    private UserBeanConverter beanConverter;

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20000)
    public void saveUser(UserAddDTO dto) {
        doCheckBeforeSave(dto);

        UpmsUser upmsUser = doSaveUser(dto);
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

    private UpmsUser doSaveUser(UserAddDTO dto) {
        UpmsUser upmsUser = new UpmsUser();
        upmsUser.setPhone(dto.getPhone());
        upmsUser.setPassword(dto.getPassword());
        upmsUser.setName(dto.getName());
        upmsUser.setPassword(iUserPasswordHelper.enhancePassword(DigestUtil.md5Hex(upmsUser.getPassword())));
        this.save(upmsUser);
        return upmsUser;
    }

    private void doCheckBeforeSave(UserAddDTO dto) {
        int count = countUserByPhone(dto.getPhone());
        Assert.isTrue(count > 0, BizEnums.USER_ALREADY_EXISTS);
    }

    private int countUserByPhone(String phone) {
        return this.count(new LambdaQueryWrapper<>(UpmsUser.class).eq(UpmsUser::getPhone, phone));
    }

    @Override
    public void updateUserById(UserUpdateDTO dto) {
        UpmsUser upmsUser = CglibUtil.copy(dto, UpmsUser.class);
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
    public List<UpmsPermission> getUserPermissions(Long userId) {
        List<Long> roleIds = getRoleIdsByUserId(userId);
        List<Long> userGroupIds = getUserGroupIdsByUserId(userId);
        List<Long> userGroupsRoleIds = iUpmsRoleService.getRoleIdsByUserGroupIds(userGroupIds);
        log.debug("用户拥有的角色 --------> {}", roleIds);
        log.debug("用户所归属的用户组 --------> {}", userGroupIds);
        roleIds.addAll(userGroupsRoleIds);
        log.debug("用户组拥有的角色 --------> {}", roleIds);
        Set<Long> roleIdSet = new HashSet<>(roleIds);
        log.debug("角色交集 --------> {}", roleIdSet);
        return iUpmsPermissionService.getPermissionByRoleIds(roleIdSet);
    }

    @Override
    public UpmsUser getUserByPhone(String phone) {
        return this.getOne(new LambdaQueryWrapper<UpmsUser>().eq(UpmsUser::getPhone, phone));
    }

    @Override
    public UserDetailVO getUserDetailVOById(Long userId) {
        UpmsUser user = getUserById(userId);
        return beanConverter.convertToUserDetailVO(user);
    }

    @Override
    public DefaultUser getUserInfoByPhone(String phone) {
        UpmsUser upmsUser = getUserByPhone(phone);
        Long userId = upmsUser.getId();
        List<UpmsPermission> userPermissions = getUserPermissions(userId);
        List<SimpleGrantedAuthority>  grantedAuthorities = userPermissions.stream()
                .map(item -> new SimpleGrantedAuthority(String.format("ROLE_%s", item.getCode()))).collect(Collectors.toList());
        return new DefaultUser(upmsUser.getName(), upmsUser.getPassword(), grantedAuthorities);
    }

    private List<Long> getUserGroupIdsByUserId(Long userId) {
        return iUpmsUserGroupService.getUserGroupIdsByUserId(userId);
    }

    private List<Long> getRoleIdsByUserId(Long userId) {
        return iUpmsRoleService.getRoleIdsByUserId(userId);
    }

    private UpmsUser getUserById(Long userId) {
        return Optional.ofNullable(this.getById(userId)).orElseGet(UpmsUser::new);
    }

    private void updateStatus(UserUpdateDTO userUpdateDTO, UserStatusEnums statusEnum) {
        this.update(new LambdaUpdateWrapper<UpmsUser>()
                .eq(UpmsUser::getStatus, userUpdateDTO.getId())
                .set(UpmsUser::getStatus, statusEnum.getValue()));
    }

}
