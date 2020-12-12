package com.kt.upms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.model.dto.UserAddDTO;
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

    UpmsUser updateUser(UpmsUser upmsUser);
}
