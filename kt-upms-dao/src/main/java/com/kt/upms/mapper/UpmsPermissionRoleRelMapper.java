package com.kt.upms.mapper;

import com.kt.model.dto.role.RolePermissionAddDTO;
import com.kt.upms.entity.UpmsPermissionRoleRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色与权限关联表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface UpmsPermissionRoleRelMapper extends BaseMapper<UpmsPermissionRoleRel> {

    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    void batchInsert(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);
}
