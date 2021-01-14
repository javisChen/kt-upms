package com.kt.upms.module.permission.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.component.dto.PageResponse;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.module.route.vo.RouteElementVO;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.route.dto.PermissionQueryDTO;
import com.kt.upms.module.route.dto.PermissionUpdateDTO;

import java.util.List;
import java.util.Set;

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

    void addPermission(Long resourceId, PermissionTypeEnums permissionTypeEnums);

    void updateStatus(PermissionUpdateDTO dto);

    List<UpmsPermission> getPermissionByRoleIds(Set<Long> roleIds);

    List<RouteElementVO> getPermissionElements(Long routePermissionId);

    UpmsPermission getPermissionByResourceIdAndType(Long resourceId, PermissionTypeEnums permissionTypeEnums);

    List<PermissionVO> getPermissionVOSByRoleIdAndType(Long permissionId, String permissionType);

    UpmsPermission getPermission(Long resourceId, PermissionTypeEnums pageElement);
}
