/*
 * 版权所有.(c)2008-2018.广州市森锐科技股份有限公司
 */

package com.kt.upms.security.configuration;

import lombok.Data;

/**
 * jwt配置文件
 * @author javis
 */
@Data
public class AuthenticationProperties {
    /**
     * token的Header
     */
    private String tokenHeader = "Authorization";

    /**
     * 认证token前缀
     */
    private String tokenHeaderPrefix = "Bearer ";

    /**
     * 认证token查询参数的key
     */
    private String tokenQueryParam = "accessToken";

    /**
     * token中是否包含用户的所有信息，默认为包含
     */
    private boolean userDetails = true;

    /**
     * 12小时
     */
    private Long expire = 3600 * 12L;

    private Long refreshTokenExpireTime = expire * 2;
}
