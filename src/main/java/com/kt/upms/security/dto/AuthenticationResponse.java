package com.kt.upms.security.dto;

import lombok.Data;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Data
public class AuthenticationResponse {

    private Integer code;
    private Boolean hasPermission;
    private String msg;

    public AuthenticationResponse(Integer code, Boolean hasPermission, String msg) {
        this.code = code;
        this.hasPermission = hasPermission;
        this.msg = msg;
    }

    public AuthenticationResponse(Integer code, Boolean hasPermission) {
        this.code = code;
        this.hasPermission = hasPermission;
    }

    public AuthenticationResponse(Boolean hasPermission) {
        this.hasPermission = hasPermission;
    }

    public static AuthenticationResponse hasPermission() {
        return new AuthenticationResponse(true);
    }

    public AuthenticationResponse noPermission() {
        return new AuthenticationResponse(false);
    }

    public static AuthenticationResponse create(boolean hasPermission) {
        return new AuthenticationResponse(hasPermission);
    }
}
