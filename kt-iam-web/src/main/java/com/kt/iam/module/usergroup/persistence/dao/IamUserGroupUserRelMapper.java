package com.kt.iam.module.usergroup.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kt.iam.module.usergroup.persistence.IamUserGroupUserRel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户组与用户关联表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
public interface IamUserGroupUserRelMapper extends BaseMapper<IamUserGroupUserRel> {
    int insertBatch(@Param("list") List<IamUserGroupUserRel> list);

    int deleteByUserIdsAndUserGroupId(@Param("userGroupId") Long userGroupId, @Param("userIds") List<Long> userIds);


    void batchSaveRelation(@Param("userId") Long userId, @Param("userGroupIds") List<Long> userGroupIds);
}
