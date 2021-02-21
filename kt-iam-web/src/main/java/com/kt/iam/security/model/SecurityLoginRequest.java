package com.kt.iam.security.model;

import lombok.Data;

@Data
public class SecurityLoginRequest {

    private String username;
    private String password;

}
