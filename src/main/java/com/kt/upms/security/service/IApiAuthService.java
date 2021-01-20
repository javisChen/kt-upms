package com.kt.upms.security.service;

import com.kt.upms.security.dto.AuthenticationRequest;
import com.kt.upms.security.dto.AuthenticationResponse;


/**
 * Api权限校验
 */
public interface IApiAuthService {

    /**
     * 根据用户id检查是否有权限访问
     */
    AuthenticationResponse auth(AuthenticationRequest request);
}
