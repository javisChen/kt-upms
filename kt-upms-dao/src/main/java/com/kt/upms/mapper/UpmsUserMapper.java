package com.kt.upms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kt.model.vo.usergroup.UserGroupUserListVO;
import com.kt.upms.entity.UpmsUser;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface UpmsUserMapper extends BaseMapper<UpmsUser> {

    IPage<UserGroupUserListVO> selectByUserGroupId(@Param("page") Page page, @Param("userGroupId") Long userGroupId);
}
