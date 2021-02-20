package com.kt.iam.auth.core.model;

import lombok.Data;

@Data
public class AuthRequest {

    private String accessToken;
    private Long userId;
    private String userCode;
    private String requestUri;
    private String method;
    private String applicationCode;


}
