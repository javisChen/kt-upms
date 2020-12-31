package com.kt.upms.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.dto.PageResponse;
import com.kt.component.logger.CatchAndLog;
import com.kt.model.dto.user.UserAddDTO;
import com.kt.model.dto.user.UserQueryDTO;
import com.kt.model.dto.user.UserUpdateDTO;
import com.kt.model.enums.BizEnums;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.entity.UpmsUser;
import com.kt.upms.enums.UserStatusEnums;
import com.kt.upms.mapper.UpmsUserMapper;
import com.kt.upms.service.IUpmsPermissionService;
import com.kt.upms.service.IUpmsRoleService;
import com.kt.upms.service.IUpmsUserGroupService;
import com.kt.upms.service.IUpmsUserService;
import com.kt.upms.support.IUserPasswordHelper;
import com.kt.upms.util.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class UpmsUserServiceImpl extends ServiceImpl<UpmsUserMapper, UpmsUser> implements IUpmsUserService {

    @Autowired
    private IUpmsRoleService iUpmsRoleService;
    @Autowired
    private IUpmsUserGroupService iUpmsUserGroupService;
    @Autowired
    private IUpmsPermissionService iUpmsPermissionService;
    @Autowired
    private IUserPasswordHelper iUserPasswordHelper;

    @Override
    public void saveUser(UserAddDTO userAddDTO) {
        doCheckBeforeSave(userAddDTO);

        doSave(userAddDTO);
    }

    private void doSave(UserAddDTO dto) {
        UpmsUser upmsUser = new UpmsUser();
        upmsUser.setPhone(dto.getPhone());
        upmsUser.setPassword(dto.getPassword());
        upmsUser.setName(dto.getName());
        upmsUser.setStatus(UserStatusEnums.ENABLED.getValue());
        upmsUser.setPassword(iUserPasswordHelper.enhancePassword(DigestUtil.md5Hex(upmsUser.getPassword())));
        this.save(upmsUser);
    }

    private void doCheckBeforeSave(UserAddDTO dto) {
        int count = countUserByPhone(dto.getPhone());
        Assert.isTrue(count > 0, BizEnums.USER_ALREADY_EXISTS);
    }

    private int countUserByPhone(String phone) {
        return this.count(new LambdaQueryWrapper<>(UpmsUser.class).eq(UpmsUser::getPhone, phone));
    }

    @Override
    public void updateUserById(UserUpdateDTO userUpdateDTO) {
        UpmsUser upmsUser = CglibUtil.copy(userUpdateDTO, UpmsUser.class);
        this.updateById(upmsUser);
    }

    @Override
    public PageResponse<UpmsUser> pageList(IPage<UpmsUser> page, UserQueryDTO params) {
        IPage<UpmsUser> result = this.page(page, new QueryWrapper<UpmsUser>()
                .like(StrUtil.isNotBlank(params.getPhone()), "phone", params.getPhone())
                .like(StrUtil.isNotBlank(params.getName()), "name", params.getName())
                .select("id", "phone", "name", "status"));
        return PageResponse.success(result);
    }

    @Override
    public void updateStatus(UserUpdateDTO userUpdateDTO) {
        updateStatus(userUpdateDTO, UserStatusEnums.ENABLED);
    }

    @Override
    public List<UpmsPermission> getUserPermissions(Long userId) {
        List<Long> roleIds = iUpmsRoleService.getRoleIdsByUserId(userId);
        List<Long> userGroupIds = iUpmsUserGroupService.getUserGroupIdsByUserId(userId);
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
    public UpmsUser getUserByPhone(String username) {
        return this.getOne(new LambdaQueryWrapper<UpmsUser>().eq(UpmsUser::getPhone, username));
    }

    private void updateStatus(UserUpdateDTO userUpdateDTO, UserStatusEnums statusEnum) {
        this.update(new LambdaUpdateWrapper<UpmsUser>()
                .eq(UpmsUser::getStatus, userUpdateDTO.getId())
                .set(UpmsUser::getStatus, statusEnum.getValue()));
    }

}
