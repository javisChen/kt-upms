package com.kt.upms.mapper;

import com.kt.upms.module.permission.bo.ApiPermissionBO;
import com.kt.upms.module.route.vo.RouteElementVO;
import com.kt.upms.entity.UpmsPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

    List<UpmsPermission> selectByRoleIds(@Param("roleIds") Set<Long> roleIds);

    List<RouteElementVO> selectPageElementPermissionsByRouteId(@Param("routeId") Long routeId);

    List<UpmsPermission> selectByRoleIdAndType(@Param("roleId") Long roleId, @Param("type") String type);

    List<ApiPermissionBO> selectApiPermissionByIds(@Param("permissionIds") List<Long> permissionIds);
}
