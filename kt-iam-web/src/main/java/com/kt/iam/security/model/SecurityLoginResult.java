package com.kt.iam.security.model;

import lombok.Data;

@Data
public class SecurityLoginResult {

    private String accessToken;
    private Integer expires;

    public SecurityLoginResult(String accessToken, Integer expires) {
        this.accessToken = accessToken;
        this.expires = expires;
    }
}
