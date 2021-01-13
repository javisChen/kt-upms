package com.kt.upms.module.auth.service;

import com.kt.upms.module.auth.dto.LoginDTO;
import com.kt.upms.module.auth.dto.LoginUserDTO;

/**
 * <p>
 * 认证授权
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface IAuthService {


    LoginUserDTO login(LoginDTO dto);
}
