package com.kt.upms.module.user.converter;

import com.kt.upms.entity.UpmsUser;
import com.kt.upms.module.role.service.IRoleService;
import com.kt.upms.module.user.vo.UserDetailVO;
import com.kt.upms.module.user.vo.UserPageListVO;
import com.kt.upms.module.usergroup.service.IUpmsUserGroupService;
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
    private IRoleService iUpmsRoleService;
    @Autowired
    private IUpmsUserGroupService iUpmsUserGroupService;

    public UserPageListVO convertToUserPageListVO(UpmsUser upmsUser) {
        Long userId = upmsUser.getId();
        List<String> roles = iUpmsRoleService.getRoleNamesByUserId(userId);
        List<String> userGroups = iUpmsUserGroupService.getUserGroupNamesByUserId(userId);
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
        vo.setRoleIds(iUpmsRoleService.getRoleIdsByUserId(userId));
        vo.setUserGroupIds(iUpmsUserGroupService.getUserGroupIdsByUserId(userId));
        return vo;
    }
}
