package com.kt.upms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.usergroup.UserGroupAddDTO;
import com.kt.model.dto.usergroup.UserGroupQueryDTO;
import com.kt.model.dto.usergroup.UserGroupUpdateDTO;
import com.kt.model.dto.usergroup.UserGroupUserAddDTO;
import com.kt.upms.entity.UpmsUserGroup;
import com.baomidou.mybatisplus.extension.service.IService;

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

    void addUserToGroup(UserGroupUserAddDTO dto);
}
