package com.kt.iam.config;

import lombok.Data;

/**
 * token配置文件
 * @author javis
 */
@Data
public class AccessTokenProperties {

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
}
