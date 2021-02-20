package com.kt.auth.security.autoconfigure;

import com.kt.auth.security.AuthSecurityConfig;
import com.kt.iam.config.AuthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthSecurityAutoConfiguration {

    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter() {
        return new AuthSecurityConfig();
    }
}
