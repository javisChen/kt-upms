package com.kt.iam.auth.core.model;

import lombok.Data;

/**
 * 登录信息信息
 */
@Data
public class LoginUserContext {

    private Long userId;
    private String userCode;
    private String username;
    private String accessToken;
    private Long expires;
    private Boolean isSuperAdmin;

}
