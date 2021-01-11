package com.kt.upms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.permission.PermissionQueryDTO;
import com.kt.model.dto.role.RolePermissionAddDTO;
import com.kt.model.dto.permission.PermissionUpdateDTO;
import com.kt.model.vo.permission.PermissionElementVO;
import com.kt.model.vo.permission.PermissionRouteVO;
import com.kt.model.vo.permission.PermissionVO;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.enums.PermissionTypeEnums;

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

    List<PermissionElementVO> getPermissionElements(Long routePermissionId);

    UpmsPermission getPermissionByResourceIdAndType(Long resourceId, PermissionTypeEnums permissionTypeEnums);

    List<PermissionVO> getPermissionVOSByRoleIdAndType(Long id, String type);
}
