package com.kt.iam.module.permission.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kt.iam.module.permission.persistence.IamPermission;
import com.kt.iam.module.permission.bo.ApiPermissionBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
public interface IamPermissionMapper extends BaseMapper<IamPermission> {

    List<IamPermission> selectByRoleIdsAndType(@Param("roleIds") Set<Long> roleIds,
                                               @Param("type") String type);

    List<IamPermission> selectByRoleIdAndType(@Param("applicationId") Long applicationId,
                                              @Param("roleId") Long roleId,
                                              @Param("type") String type);

    List<ApiPermissionBO> selectApiPermissionByIds(@Param("permissionIds") List<Long> permissionIds);

    /**
     * 根据角色id和应用id查询api权限
     */
    Set<ApiPermissionBO> selectApiPermissionsByRoleIdsAndApplicationId(@Param("applicationId") Long applicationId,
                                                                       @Param("roleIds") Set<Long> roleIdSet);
}
