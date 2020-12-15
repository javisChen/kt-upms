package com.kt.upms.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kt.upms.entity.UpmsUserGroupUserRel;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * <p>
 * 用户组与用户关联表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface UpmsUserGroupUserRelMapper extends BaseMapper<UpmsUserGroupUserRel> {
    int insertBatch(@Param("upmsUserGroupUserRelCollection") Collection<UpmsUserGroupUserRel> upmsUserGroupUserRelCollection);

}
