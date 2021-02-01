package com.kt.upms.module.usergroup.converter;

import com.kt.upms.module.role.service.IRoleService;
import com.kt.upms.module.usergroup.dto.UserGroupUpdateDTO;
import com.kt.upms.module.usergroup.persistence.UpmsUserGroup;
import com.kt.upms.module.usergroup.vo.UserGroupDetailVO;
import com.kt.upms.module.usergroup.vo.UserGroupListTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserGroupBeanConverter {

    @Autowired
    private IRoleService iRoleService;

    public UserGroupListTreeVO convertToUserGroupListTreeVO(UpmsUserGroup item) {
        UserGroupListTreeVO vo = new UserGroupListTreeVO();
        vo.setId(item.getId());
        vo.setPid(item.getPid());
        vo.setName(item.getName());
        vo.setStatus(item.getStatus());
        vo.setCreateTime(item.getGmtCreate());
        vo.setUpdateTime(item.getGmtModified());
        vo.setChildren(new ArrayList<>());
        vo.setRoles(iRoleService.getRoleNamesByUserGroupId(item.getId()));
        return vo;
    }

    public UpmsUserGroup convertToDO(UserGroupUpdateDTO dto) {
        UpmsUserGroup upmsUserGroup = new UpmsUserGroup();
        upmsUserGroup.setId(dto.getId());
        upmsUserGroup.setName(dto.getName());
        upmsUserGroup.setPid(dto.getPid());
        return upmsUserGroup;
    }

    public UserGroupDetailVO convertToUserGroupDetailVO(UpmsUserGroup userGroup) {
        UserGroupDetailVO userGroupDetailVO = new UserGroupDetailVO();
        userGroupDetailVO.setId(userGroup.getId());
        userGroupDetailVO.setPid(userGroup.getPid());
        userGroupDetailVO.setName(userGroup.getName());
        userGroupDetailVO.setStatus(userGroup.getStatus());
        userGroupDetailVO.setLevel(userGroup.getLevel());
        userGroupDetailVO.setRoleIds(iRoleService.getRoleIdsByUserGroupId(userGroup.getId()));
        return userGroupDetailVO;
    }
}
