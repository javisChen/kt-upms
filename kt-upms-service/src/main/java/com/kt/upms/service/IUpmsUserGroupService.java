package com.kt.upms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.usergroup.*;
import com.kt.model.vo.usergroup.UserGroupTreeVO;
import com.kt.upms.entity.UpmsUserGroup;

import java.util.List;

/**
 * <p>
 * 用户组表 服务类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface IUpmsUserGroupService extends IService<UpmsUserGroup> {

    PageResponse pageList(Page page, UserGroupQueryDTO params);

    UserGroupAddDTO saveUserGroup(UserGroupAddDTO userGroupAddDTO);

    void updateUserGroupById(UserGroupUpdateDTO userGroupUpdateDTO);

    void updateStatus(UserGroupUpdateDTO dto);

    void addOrRemoveUserInUserGroup(UserGroupUserAddDTO dto);

    PageResponse getUsersUnderUserGroupPageList(Page page, UserGroupUserQueryDTO dto);

    void addOrRemoveRoleInUserGroup(UserGroupRoleAddDTO dto);

    PageResponse getRolesUnderUserGroupPageList(Page page, UserGroupRoleQueryDTO params);

    List<Long> getUserGroupIdsByUserId(Long userId);

    List<UserGroupTreeVO> getTree(UserGroupQueryDTO dto);
}
