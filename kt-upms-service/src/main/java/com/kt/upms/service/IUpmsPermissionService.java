package com.kt.upms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.permission.PermissionAddDTO;
import com.kt.model.dto.permission.PermissionQueryDTO;
import com.kt.model.dto.permission.PermissionUpdateDTO;
import com.kt.upms.entity.UpmsPermission;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface IUpmsPermissionService extends IService<UpmsPermission> {

    PageResponse pageList(Page page, PermissionQueryDTO params);

    PermissionAddDTO savePermission(PermissionAddDTO dto);

    void updatePermissionById(PermissionUpdateDTO dto);

    void updateStatus(PermissionUpdateDTO dto);

}
