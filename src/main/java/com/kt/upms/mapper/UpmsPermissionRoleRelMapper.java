package com.kt.upms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kt.upms.entity.UpmsPermissionRoleRel;
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


    void batchInsert(@Param("roleId") Long roleId,
                     @Param("type") String type,
                     @Param("permissionIds") List<Long> permissionIds);

    List<String> selectRoleNamesByUserId(@Param("userId") Long userId);
}
