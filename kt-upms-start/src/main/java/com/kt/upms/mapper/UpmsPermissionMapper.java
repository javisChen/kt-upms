package com.kt.upms.mapper;

import com.kt.upms.module.permission.vo.PermissionElementVO;
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

    List<PermissionElementVO> selectPageElementPermissionsByRouteId(@Param("routeId") Long routeId);

    List<UpmsPermission> selectByRoleIdAndType(@Param("roleId") Long roleId, @Param("type") String type);
}
