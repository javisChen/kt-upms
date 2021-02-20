
package com.kt.iam.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@ConfigurationProperties(prefix = "kt.security.auth")
@Data
@Configuration
public class AuthProperties {

    /**
     * 服务地址
     */
    private String serverUrl;

    /**
     * 认证服务超时时间 单位：毫秒
     */
    private Integer timeout = 15000;

    /**
     * 应用编码
     */
    private String applicationCode;

}
