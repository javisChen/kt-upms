package com.kt.upms.module.usergroup.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.upms.module.usergroup.persistence.UpmsUserGroup;
import com.kt.upms.module.usergroup.dto.UserGroupUpdateDTO;
import com.kt.upms.module.usergroup.dto.UserGroupQueryDTO;
import com.kt.upms.module.usergroup.vo.UserGroupBaseVO;
import com.kt.upms.module.usergroup.vo.UserGroupListTreeVO;
import com.kt.upms.module.usergroup.vo.UserGroupTreeVO;

import java.util.List;

/**
 * <p>
 * 用户组表 服务类
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
public interface IUserGroupService extends IService<UpmsUserGroup> {

    Page<UserGroupListTreeVO> pageList(UserGroupQueryDTO pageRequest);

    void saveUserGroup(UserGroupUpdateDTO userGroupUpdateDTO);

    void updateUserGroupById(UserGroupUpdateDTO userGroupUpdateDTO);

    List<Long> getUserGroupIdsByUserId(Long userId);

    List<UserGroupTreeVO> getTree(UserGroupQueryDTO dto);

    List<UserGroupBaseVO> listAllVos();

    List<String> getUserGroupNamesByUserId(Long userId);

    void removeUserUserGroupRelByUserId(Long userId);
}
