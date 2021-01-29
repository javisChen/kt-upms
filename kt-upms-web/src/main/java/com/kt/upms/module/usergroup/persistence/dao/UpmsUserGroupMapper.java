package com.kt.upms.module.usergroup.persistence.dao;

import com.kt.upms.module.usergroup.persistence.UpmsUserGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户组表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
public interface UpmsUserGroupMapper extends BaseMapper<UpmsUserGroup> {

    List<Long> selectUserGroupIdsByUserId(@Param("userId") Long userId);

    List<String> selectUserGroupNamesByUserId(@Param("userId") Long userId);
}
