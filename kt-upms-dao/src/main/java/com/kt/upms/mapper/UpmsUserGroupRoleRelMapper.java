package com.kt.upms.mapper;

import com.kt.upms.entity.UpmsUserGroupRoleRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户组与角色关联表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
public interface UpmsUserGroupRoleRelMapper extends BaseMapper<UpmsUserGroupRoleRel> {

    int deleteByRoleIdsAndUserGroupId(@Param("userGroupId") Long userGroupId, @Param("roleIds") List<Long> roleIds);

    void insertBatch(@Param("rels") List<UpmsUserGroupRoleRel> rels);
}
