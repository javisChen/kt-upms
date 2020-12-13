package com.kt.upms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.UserGroupAddDTO;
import com.kt.model.dto.UserGroupQueryDTO;
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

    UserGroupAddDTO save(UserGroupAddDTO userGroupAddDTO);
}
