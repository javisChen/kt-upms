package com.kt.upms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.user.UserAddDTO;
import com.kt.model.dto.user.UserQueryDTO;
import com.kt.model.dto.user.UserUpdateDTO;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.entity.UpmsUser;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface IUpmsUserService extends IService<UpmsUser> {

    void saveUser(UserAddDTO entity);

    void updateUserById(UserUpdateDTO userUpdateDTO);

    PageResponse<UpmsUser> pageList(IPage<UpmsUser> page, UserQueryDTO params);

    void updateStatus(UserUpdateDTO userUpdateDTO);

    /**
     * 获取用户所拥有的全部权限
     */
    List<UpmsPermission> getUserPermissions(Long userId);

    UpmsUser getUserByPhone(String username);
}
