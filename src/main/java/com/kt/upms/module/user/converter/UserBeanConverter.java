package com.kt.upms.module.user.converter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.entity.UpmsUser;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.role.service.IRoleService;
import com.kt.upms.module.user.dto.UserUpdateDTO;
import com.kt.upms.module.user.service.IUserService;
import com.kt.upms.module.user.vo.UserDetailVO;
import com.kt.upms.module.user.vo.UserPageListVO;
import com.kt.upms.module.usergroup.service.IUserGroupService;
import com.kt.upms.module.user.support.IUserPasswordHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Component
public class UserBeanConverter {

    @Autowired
    private IRoleService iRoleService;
    @Autowired
    private IUserGroupService iUserGroupService;
    @Autowired
    private IUserPasswordHelper iUserPasswordHelper;
    @Autowired
    private IUserService iUserService;

    public UserPageListVO convertToUserPageListVO(UpmsUser upmsUser) {
        Long userId = upmsUser.getId();
        List<String> roles = iRoleService.getRoleNamesByUserId(userId);
        List<String> userGroups = iUserGroupService.getUserGroupNamesByUserId(userId);
        UserPageListVO userListVO = new UserPageListVO();
        userListVO.setId(upmsUser.getId());
        userListVO.setPhone(upmsUser.getPhone());
        userListVO.setName(upmsUser.getName());
        userListVO.setStatus(upmsUser.getStatus());
        userListVO.setRoles(roles);
        userListVO.setUserGroups(userGroups);
        return userListVO;
    }

    public UserDetailVO convertToUserDetailVO(UpmsUser user) {
        UserDetailVO vo = new UserDetailVO();
        Long userId = user.getId();
        vo.setId(userId);
        vo.setPhone(user.getPhone());
        vo.setName(user.getName());
        vo.setStatus(user.getStatus());
        vo.setRoleIds(iRoleService.getRoleIdsByUserId(userId));
        vo.setUserGroupIds(iUserGroupService.getUserGroupIdsByUserId(userId));
        return vo;
    }

    public PermissionVO convertToPermissionVO(UpmsPermission permission) {
        PermissionVO permissionVO = new PermissionVO();
        permissionVO.setPermissionCode(permission.getCode());
        return permissionVO;
    }

    public UpmsUser convertToUserDO(UserUpdateDTO dto) {
        UpmsUser upmsUser = new UpmsUser();
        upmsUser.setId(dto.getId());
        upmsUser.setName(dto.getName());
        upmsUser.setPhone(dto.getPhone());
        upmsUser.setPassword(dto.getPassword());
        upmsUser.setStatus(dto.getStatus());
        String code = generateUserCode();
        upmsUser.setCode(code);
        upmsUser.setPassword(iUserPasswordHelper.enhancePassword(DigestUtil.md5Hex(upmsUser.getPassword())));
        return upmsUser;
    }

    private String generateUserCode() {
        // 生成后先查询一遍，防止生成了重复的code，其实几率微乎其微
        String code;
        do {
            code = StringUtils.remove(StrUtil.uuid(), "-");
        } while (codeExists(code));
        return code;
    }

    private boolean codeExists(String code) {
        return iUserService.countUserByCode(code) > 0;
    }
}
