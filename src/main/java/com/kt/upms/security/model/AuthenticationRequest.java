package com.kt.upms.security.model;

import lombok.Data;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Data
public class AuthenticationRequest {

    private Long userId;
    private String userCode;
    private String url;
    private String method;
    private String application;

}
