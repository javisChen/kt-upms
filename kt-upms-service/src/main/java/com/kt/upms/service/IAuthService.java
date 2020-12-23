package com.kt.upms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.model.dto.auth.LoginDTO;
import com.kt.model.dto.auth.LoginUserDTO;
import com.kt.upms.entity.UpmsApi;

/**
 * <p>
 * api表 服务类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface IAuthService {


    LoginUserDTO login(LoginDTO dto);
}
