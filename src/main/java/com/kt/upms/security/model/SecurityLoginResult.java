package com.kt.upms.security.model;

import lombok.Data;

@Data
public class SecurityLoginResult {

    private String accessToken;
    private Long expire;

    public SecurityLoginResult(String accessToken, Long expire) {
        this.accessToken = accessToken;
        this.expire = expire;
    }
}
