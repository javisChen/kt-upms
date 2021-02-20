package com.kt.iam.module.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.iam.enums.PermissionTypeEnums;
import com.kt.iam.module.permission.bo.ApiPermissionBO;
import com.kt.iam.module.permission.persistence.IamPermission;
import com.kt.iam.module.permission.vo.PermissionVO;

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
public interface IPermissionService extends IService<IamPermission> {

    void addPermission(Long resourceId, PermissionTypeEnums permissionTypeEnums);

    void batchSaveRolePermissionRel(Long roleId, List<Long> permissionIds);

    /**
     * 根据角色id和权限类型删除指定应用下的指定权限
     *
     * @param roleId        角色id
     * @param permissionIds 权限id
     */
    void removeRolePermission(Long roleId, List<Long> permissionIds);

    List<IamPermission> getPermissionByRoleIds(Set<Long> roleIds, PermissionTypeEnums permissionTypeEnums);

    List<IamPermission> getAllPermissionsByType(PermissionTypeEnums type);

    IamPermission getPermissionByResourceIdAndType(Long resourceId, PermissionTypeEnums permissionTypeEnums);

    List<PermissionVO> getRolePermissionVos(Long applicationId, Long roleId, String permissionType);

    IamPermission getPermission(Long resourceId, PermissionTypeEnums pageElement);

    void removeByResourceIds(List<Long> ids);

    List<ApiPermissionBO> getApiPermissionByIds(List<Long> permissionIds);

    Set<ApiPermissionBO> getApiPermissionByRoleIdsAndApplicationCode(String applicationCode, Set<Long> roleIdSet);
}
