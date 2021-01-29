package com.kt.auth.security.autoconfigure;

import com.kt.auth.security.AuthSecurityConfig;
import com.kt.component.redis.RedisService;
import com.kt.upms.auth.core.cache.RedisUserTokenCache;
import com.kt.upms.auth.core.cache.UserTokenCache;
import com.kt.upms.config.AuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthSecurityAutoConfiguration {

    @Autowired
    private RedisService redisService;

    @Autowired
    private AuthProperties authProperties;

    @Bean
    public UserTokenCache userTokenCache() {
        return new RedisUserTokenCache(redisService);
    }

    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter(UserTokenCache tokenCache) {
        return new AuthSecurityConfig(tokenCache, authProperties);
    }
}
