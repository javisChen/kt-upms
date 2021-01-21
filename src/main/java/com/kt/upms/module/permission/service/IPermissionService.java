package com.kt.upms.module.permission.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.component.dto.PageResponse;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.enums.PermissionTypeEnums;
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
public interface IPermissionService extends IService<UpmsPermission> {

    PageResponse pageList(Page page, PermissionQueryDTO params);

    void addPermission(Long resourceId, PermissionTypeEnums permissionTypeEnums);

    void batchSaveRolePermission(Long applicationId, Long roleId, String type, List<Long> permissionIds);

    /**
     * 根据角色id和权限类型删除角色权限
     * @param applicationId
     * @param roleId 角色id
     * @param permissionType 权限类型
     */
    void removeRolePermission(Long applicationId, Long roleId, String permissionType);

    void updateStatus(PermissionUpdateDTO dto);

    List<UpmsPermission> getPermissionByRoleIds(Set<Long> roleIds, PermissionTypeEnums permissionTypeEnums);

    UpmsPermission getPermissionByResourceIdAndType(Long resourceId, PermissionTypeEnums permissionTypeEnums);

    List<PermissionVO> getRolePermissVos(Long applicationId, Long roleId, String permissionType);

    UpmsPermission getPermission(Long resourceId, PermissionTypeEnums pageElement);

    boolean hasApiPermission(String application, Long userId, String url, String method);
}
