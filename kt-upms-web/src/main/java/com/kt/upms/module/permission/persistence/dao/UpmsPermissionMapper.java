package com.kt.upms.module.permission.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kt.upms.module.permission.persistence.UpmsPermission;
import com.kt.upms.module.permission.bo.ApiPermissionBO;
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
public interface UpmsPermissionMapper extends BaseMapper<UpmsPermission> {

    List<UpmsPermission> selectByRoleIdsAndType(@Param("roleIds") Set<Long> roleIds,
                                                @Param("type") String type);

    List<UpmsPermission> selectByRoleIdAndType(@Param("applicationId") Long applicationId,
                                               @Param("roleId") Long roleId,
                                               @Param("type") String type);

    List<ApiPermissionBO> selectApiPermissionByIds(@Param("permissionIds") List<Long> permissionIds);
}
