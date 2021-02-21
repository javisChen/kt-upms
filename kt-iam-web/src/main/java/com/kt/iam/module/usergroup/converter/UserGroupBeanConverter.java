package com.kt.iam.module.usergroup.converter;

import com.kt.iam.module.role.service.IRoleService;
import com.kt.iam.module.usergroup.dto.UserGroupUpdateDTO;
import com.kt.iam.module.usergroup.persistence.IamUserGroup;
import com.kt.iam.module.usergroup.vo.UserGroupDetailVO;
import com.kt.iam.module.usergroup.vo.UserGroupListTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserGroupBeanConverter {

    @Autowired
    private IRoleService iRoleService;

    public UserGroupListTreeVO convertToUserGroupListTreeVO(IamUserGroup item) {
        UserGroupListTreeVO vo = new UserGroupListTreeVO();
        vo.setId(item.getId());
        vo.setPid(item.getPid());
        vo.setName(item.getName());
        vo.setStatus(item.getStatus());
        vo.setCreateTime(item.getGmtCreate());
        vo.setUpdateTime(item.getGmtModified());
        vo.setInheritType(item.getInheritType());
        vo.setChildren(new ArrayList<>());
        vo.setRoles(iRoleService.getRoleNamesByUserGroupId(item.getId()));
        return vo;
    }

    public IamUserGroup convertToDO(UserGroupUpdateDTO dto) {
        IamUserGroup iamUserGroup = new IamUserGroup();
        iamUserGroup.setId(dto.getId());
        iamUserGroup.setName(dto.getName());
        iamUserGroup.setPid(dto.getPid());
        iamUserGroup.setInheritType(dto.getInheritType());
        return iamUserGroup;
    }

    public UserGroupDetailVO convertToUserGroupDetailVO(IamUserGroup userGroup) {
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
