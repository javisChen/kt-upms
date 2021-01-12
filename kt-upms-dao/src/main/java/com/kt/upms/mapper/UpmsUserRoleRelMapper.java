package com.kt.upms.mapper;

import com.kt.upms.entity.UpmsUserRoleRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户角色关联表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface UpmsUserRoleRelMapper extends BaseMapper<UpmsUserRoleRel> {

    void batchSaveRelation(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
}
