package com.kt.upms.security.configuration;

import com.kt.upms.config.AccessTokenProperties;
import com.kt.upms.config.LoginProperties;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全配置
 *
 * @author Javis
 */
@ConfigurationProperties(prefix = "kt.security")
@Data
@Configuration
public class SecurityCoreProperties implements InitializingBean {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 登录配置
     */
    @NestedConfigurationProperty
    private LoginProperties login;

    /**
     * 认证配置
     */
    @NestedConfigurationProperty
    private AccessTokenProperties authentication = new AccessTokenProperties();


    /**
     * 放行名单
     */
    private List<String> allowList = new ArrayList<>();

    /**
     * 禁止名单
     */
    private List<String> blockList = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
//        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RestController.class);
//        System.out.println(beansWithAnnotation);
//        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : requestMappingHandlerMapping.getHandlerMethods().entrySet()) {
//            Map.Entry<RequestMappingInfo, HandlerMethod> methodEntry = entry;
//            RequestMappingInfo key = methodEntry.getKey();
//            HandlerMethod value = methodEntry.getValue();
//            System.out.println(key);
//            System.out.println(value);
//        }
    }
}
