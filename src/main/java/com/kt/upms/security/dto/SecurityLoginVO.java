package com.kt.upms.security.dto;

import lombok.Data;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Data
public class SecurityLoginVO {

    private String accessToken;
    private Long expire;

    public SecurityLoginVO(String accessToken, Long expire) {
        this.accessToken = accessToken;
        this.expire = expire;
    }
}
