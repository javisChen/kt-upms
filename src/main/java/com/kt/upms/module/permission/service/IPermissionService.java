package com.kt.upms.module.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.module.permission.vo.PermissionVO;
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

    void addPermission(Long resourceId, PermissionTypeEnums permissionTypeEnums);

    void batchSaveRolePermissionRel(Long roleId, List<Long> permissionIds);

    /**
     * 根据角色id和权限类型删除指定应用下的指定权限
     * @param roleId 角色id
     * @param permissionIds 权限id
     */
    void removeRolePermission(Long roleId, List<Long> permissionIds);

    void updateStatus(PermissionUpdateDTO dto);

    List<UpmsPermission> getPermissionByRoleIds(Set<Long> roleIds, PermissionTypeEnums permissionTypeEnums);

    List<UpmsPermission> getAllPermissionsByType(PermissionTypeEnums type);

    UpmsPermission getPermissionByResourceIdAndType(Long resourceId, PermissionTypeEnums permissionTypeEnums);

    List<PermissionVO> getRolePermissionVos(Long applicationId, Long roleId, String permissionType);

    UpmsPermission getPermission(Long resourceId, PermissionTypeEnums pageElement);

    boolean hasApiPermission(String application, Long userId, String url, String method);

    boolean hasApiPermission(String application, String userCode, String url, String method);

    void removeByResourceIds(List<Long> ids);
}
