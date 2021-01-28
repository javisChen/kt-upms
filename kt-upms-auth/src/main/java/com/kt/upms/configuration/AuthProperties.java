
package com.kt.upms.configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "kt.security.auth")
@Data
@Configuration
public class AuthProperties {

    private String host;
    private Integer port;


}
