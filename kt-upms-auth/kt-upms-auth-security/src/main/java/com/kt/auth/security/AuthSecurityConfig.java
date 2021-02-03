package com.kt.auth.security;

import com.kt.upms.auth.core.check.AuthCheckFilter;
import com.kt.upms.auth.core.context.LoginUserContextPersistenceFilter;
import com.kt.upms.config.AuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;

/**
 * 安全控制配置
 */
@Slf4j
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter implements InitializingBean {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    private AuthProperties authProperties;
    private List<String> allowList;

    public AuthSecurityConfig(AuthProperties authProperties) {
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
                .addFilterBefore(new AuthCheckFilter(authProperties, allowList), LoginUserContextPersistenceFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
    }
}
