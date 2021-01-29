
package com.kt.upms.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "kt.security.auth")
@Data
@Configuration
public class AuthProperties {

    /**
     * 服务地址
     */
    private String serverUrl;
    /**
     * 应用编码
     */
    private String applicationCode;

}
