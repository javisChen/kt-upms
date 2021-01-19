package com.kt.upms.security.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全配置
 * @author Javis
 */
@ConfigurationProperties(prefix = "kt.security")
@Data
@Configuration
public class SecurityCoreProperties {

    /**
     * 登录配置
     */
    @NestedConfigurationProperty
    private LoginProperties login;

    /**
     * 认证配置
     */
    @NestedConfigurationProperty
    private AuthenticationProperties authentication;


    /**
     * 放行名单
     */
    private List<String> allowList = new ArrayList<>();

    /**
     * 禁止名单
     */
    private List<String> blockList = new ArrayList<>();
}
