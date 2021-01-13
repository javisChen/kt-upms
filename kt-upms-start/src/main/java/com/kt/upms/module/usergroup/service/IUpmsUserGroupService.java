package com.kt.upms.module.usergroup.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.component.dto.PageResponse;
import com.kt.upms.entity.UpmsUserGroup;
import com.kt.upms.module.usergroup.dto.*;
import com.kt.upms.module.usergroup.vo.TreeVO;
import com.kt.upms.module.usergroup.vo.UserGroupListTreeVO;
import com.kt.upms.module.usergroup.vo.UserGroupVO;

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

    PageResponse<UserGroupListTreeVO> pageList(UserGroupQueryDTO pageRequest);

    UserGroupAddDTO saveUserGroup(UserGroupAddDTO userGroupAddDTO);

    void updateUserGroupById(UserGroupUpdateDTO userGroupUpdateDTO);

    void updateStatus(UserGroupUpdateDTO dto);

    void addOrRemoveUserInUserGroup(UserGroupUserAddDTO dto);

    PageResponse getUsersUnderUserGroupPageList(Page page, UserGroupUserQueryDTO dto);

    void addOrRemoveRoleInUserGroup(UserGroupRoleAddDTO dto);

    PageResponse getRolesUnderUserGroupPageList(Page page, UserGroupRoleQueryDTO params);

    List<Long> getUserGroupIdsByUserId(Long userId);

    List<TreeVO> getTree(UserGroupQueryDTO dto);

    List<UserGroupVO> listAllVos();

    List<String> getUserGroupNamesByUserId(Long userId);
}
