package com.kt.upms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.user.UserAddDTO;
import com.kt.model.dto.user.UserQueryDTO;
import com.kt.model.dto.user.UserUpdateDTO;
import com.kt.upms.entity.UpmsUser;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface IUpmsUserService extends IService<UpmsUser> {

    UserAddDTO save(UserAddDTO entity);

    UserUpdateDTO updateUserById(UserUpdateDTO userUpdateDTO);

    PageResponse<UpmsUser> pageList(IPage<UpmsUser> page, UserQueryDTO params);

    void disableUser(UserUpdateDTO userUpdateDTO);

    void enableUser(UserUpdateDTO userUpdateDTO);
}
