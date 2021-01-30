package com.kt.upms.auth.core.model;

import com.kt.component.dto.ResponseEnums;
import lombok.Data;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Data
public class AuthResponse {

    private String code;
    private Boolean hasPermission;
    private String msg;

    public AuthResponse(String code, Boolean hasPermission, String msg) {
        this.code = code;
        this.hasPermission = hasPermission;
        this.msg = msg;
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

    public static AuthResponse fail(String msg) {
        return new AuthResponse(ResponseEnums.USER_ACCESS_DENIED.getCode(), false, msg);
    }
}
