package com.kt.upms.auth.core.model;

import com.kt.component.dto.ResponseEnums;
import lombok.Data;

/**
 * 权限校验响应体
 */
@Data
public class AuthResponse {

    private String code;
    private Boolean hasPermission;
    private LoginUserContext loginUserContext;
    private String msg;

    public AuthResponse(String code, Boolean hasPermission, String msg) {
        this.code = code;
        this.hasPermission = hasPermission;
        this.msg = msg;
    }

    public AuthResponse(String code, Boolean hasPermission, LoginUserContext loginUserContext) {
        this.code = code;
        this.hasPermission = hasPermission;
        this.loginUserContext = loginUserContext;
    }

    public AuthResponse(String code, Boolean hasPermission) {
        this.code = code;
        this.hasPermission = hasPermission;
    }

    public AuthResponse(Boolean hasPermission) {
        this.hasPermission = hasPermission;
    }

    public AuthResponse(boolean hasPermission, String msg) {
        this.hasPermission = hasPermission;
        this.msg = msg;
    }

    public static AuthResponse success() {
        return new AuthResponse(ResponseEnums.OK.getCode(), true);
    }

    public static AuthResponse success(LoginUserContext loginUserContext) {
        return new AuthResponse(ResponseEnums.OK.getCode(), true, loginUserContext);
    }

    public static AuthResponse fail(String msg) {
        return new AuthResponse(ResponseEnums.USER_ACCESS_DENIED.getCode(), false, msg);
    }
}
