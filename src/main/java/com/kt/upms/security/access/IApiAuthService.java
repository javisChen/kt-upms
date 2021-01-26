package com.kt.upms.security.access;

import com.kt.upms.security.model.AuthenticationRequest;
import com.kt.upms.security.model.AuthenticationResponse;


/**
 * Api权限校验
 */
public interface IApiAuthService {

    /**
     * 根据用户id检查是否有权限访问
     */
    AuthenticationResponse auth(AuthenticationRequest request);
}