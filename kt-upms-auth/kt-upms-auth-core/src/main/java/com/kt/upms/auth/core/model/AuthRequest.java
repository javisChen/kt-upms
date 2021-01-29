package com.kt.upms.auth.core.model;

import lombok.Data;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Data
public class AuthRequest {

    private Long userId;
    private String userCode;
    private String url;
    private String method;
    private String applicationCode;

}
