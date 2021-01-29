package com.kt.auth.security;

import com.kt.upms.auth.core.cache.UserTokenCache;
import com.kt.upms.auth.core.check.AuthCheckFilter;
import com.kt.upms.auth.core.context.LoginUserContextPersistenceFilter;
import com.kt.upms.config.AuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

/**
 * 安全控制配置
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 启用@PreAuthorize注解
@Slf4j
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserTokenCache userTokenCache;
    private AuthProperties authProperties;

    public AuthSecurityConfig(UserTokenCache userTokenCache, AuthProperties authProperties) {
        this.userTokenCache = userTokenCache;
        this.authProperties = authProperties;
    }

    /**
     * 配置请求认证的参数
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 是否启用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 关闭csrf
                .csrf().disable();

        setupAuthFilter(http);

    }

    private void setupAuthFilter(HttpSecurity http) throws Exception {
        http.addFilterBefore(new LoginUserContextPersistenceFilter(), SecurityContextPersistenceFilter.class)
                .addFilterBefore(new AuthCheckFilter(userTokenCache, authProperties), LoginUserContextPersistenceFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
