package com.kt.auth.security;

import com.kt.upms.auth.core.SkipPermissionCheck;
import com.kt.upms.auth.core.check.AuthCheckFilter;
import com.kt.upms.auth.core.context.LoginUserContextPersistenceFilter;
import com.kt.upms.config.AuthProperties;
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
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
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
        initAllowList();
    }

    private void initAllowList() {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RestController.class);
        for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
            Object value = entry.getValue();
            Annotation annotation = value.getClass().getAnnotation(SkipPermissionCheck.class);
            if (annotation != null) {
                for (Method method : value.getClass().getDeclaredMethods()) {
                    add(method);
                }
            }
        }
        System.out.println(beansWithAnnotation);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        allowList = new ArrayList<>(handlerMethods.size());
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo key = entry.getKey();
            HandlerMethod value = entry.getValue();
            SkipPermissionCheck annotation = value.getMethodAnnotation(SkipPermissionCheck.class);
            if (annotation != null) {
                Iterator<String> stringIterator = key.getPatternsCondition().getPatterns().iterator();
                Iterator<RequestMethod> requestMethodIterator = key.getMethodsCondition().getMethods().iterator();
                if (requestMethodIterator.hasNext() && stringIterator.hasNext()) {
                    allowList.add(requestMethodIterator.next().name() + ":" + stringIterator.next());
                }
            }
        }
    }

    private void add(Method method) {
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if (postMapping != null) {
            allowList.add("POST" + ":" + postMapping.path()[0]);
            return;
        }
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        if (getMapping != null) {
            allowList.add("GET" + ":" + getMapping.path()[0]);
            return;
        }
        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
        if (deleteMapping != null) {
            allowList.add("DELETE" + ":" + deleteMapping.path()[0]);
            return;
        }
        PutMapping putMapping = method.getAnnotation(PutMapping.class);
        if (putMapping != null) {
            allowList.add("POST" + ":" + putMapping.path()[0]);
            return;
        }
        PatchMapping patchMapping = method.getAnnotation(PatchMapping.class);
        if (patchMapping != null) {
            allowList.add("PATCH" + ":" + patchMapping.path()[0]);
        }
    }
}
