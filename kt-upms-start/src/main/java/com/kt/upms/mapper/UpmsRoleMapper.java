package com.kt.upms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kt.upms.module.usergroup.vo.UserGroupUserListVO;
import com.kt.upms.entity.UpmsRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface UpmsRoleMapper extends BaseMapper<UpmsRole> {

    IPage<UserGroupUserListVO> selectByUserGroupId(@Param("page") Page page, @Param("userGroupId") Long id);
}
