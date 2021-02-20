package com.kt.auth.security;

import com.kt.iam.auth.core.SkipCheck;
import com.kt.iam.auth.core.check.AuthCheckFilter;
import com.kt.iam.auth.core.context.LoginUserContextPersistenceFilter;
import com.kt.iam.config.AuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 安全控制配置
 */
@Slf4j
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter implements InitializingBean {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private AuthProperties authProperties;

    private List<String> allowList;

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
        initAllowList();
    }

    private void initAllowList() {
        allowList = new ArrayList<>(handlerMapping.getHandlerMethods().size());
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RestController.class);
        for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
            Object value = entry.getValue();
            // 如果Controller上有SkipPermissionCheck注解的话，里面的所有接口都跳过权限校验
            // 如果没有的话，则根据接口上的注解决定是否跳过
            Annotation classAnnotation = value.getClass().getAnnotation(SkipCheck.class);
            for (Method method : value.getClass().getDeclaredMethods()) {
                if (classAnnotation != null || method.getAnnotation(SkipCheck.class) != null) {
                    addToAllowList(method);
                }
            }
        }
    }

    private void addToAllowList(Method method) {
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if (postMapping != null) {
            allowList.add("POST" + ":" + postMapping.value()[0]);
            return;
        }
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        if (getMapping != null) {
            String s = getMapping.value()[0];
            allowList.add("GET" + ":" + s);
            return;
        }
        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
        if (deleteMapping != null) {
            allowList.add("DELETE" + ":" + deleteMapping.value()[0]);
            return;
        }
        PutMapping putMapping = method.getAnnotation(PutMapping.class);
        if (putMapping != null) {
            allowList.add("POST" + ":" + putMapping.value()[0]);
            return;
        }
        PatchMapping patchMapping = method.getAnnotation(PatchMapping.class);
        if (patchMapping != null) {
            allowList.add("PATCH" + ":" + patchMapping.value()[0]);
        }
    }
}
