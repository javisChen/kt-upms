package com.kt.iam.module.auth.service;

import com.kt.iam.module.auth.dto.AuthKickDTO;

import javax.servlet.http.HttpServletRequest;

public interface IAuthService {

    void kick(AuthKickDTO dto);

    void logout(HttpServletRequest request);
}
