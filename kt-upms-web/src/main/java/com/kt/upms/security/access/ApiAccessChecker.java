package com.kt.upms.security.access;

import com.kt.upms.module.api.cache.ApiCacheManager;
import com.kt.upms.security.configuration.SecurityCoreProperties;
import com.kt.upms.security.model.AuthenticationRequest;
import com.kt.upms.security.model.AuthenticationResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 访问校验
 */
@Component
public class ApiAccessChecker implements InitializingBean {

    @Autowired
    private IApiAuthService iApiAuthService;
    @Autowired
    private ApiCacheManager apiCacheManager;
    @Autowired
    private SecurityCoreProperties securityCoreProperties;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    public boolean check(HttpServletRequest request, Authentication authentication) {
        AuthenticationRequest authReq;
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        Long applicationId = 1L;

        // 先尝试uri是否匹配系统中存在的包含路径参数的api，如果存在的话就替换成统一的格式
        requestUri = attemptReplaceHasPathVariableUrl(requestUri);

        // 尝试是否匹配不需要授权的api
        if (attemptMatchDefaultAllowUrl(requestUri)) {
            return true;
        }

        // 尝试是否匹配不需要授权的api
        if (attemptMatchNoNeedAuthorizationUrl(requestUri, method, applicationId)) {
            return true;
        }

        authReq = createAuthenticationRequest(request, requestUri, (String) authentication.getPrincipal());
        AuthenticationResponse authResp = iApiAuthService.auth(authReq);
        final Boolean hasPermission = authResp.getHasPermission();
        return hasPermission;
    }

    public boolean attemptMatchDefaultAllowUrl(String requestUri) {
        return securityCoreProperties.getAllowList().stream()
                .anyMatch(item -> pathMatcher.match(item, requestUri));
    }

    public String attemptReplaceHasPathVariableUrl(String requestUri) {
        List<String> hasPathVariableApiCache = apiCacheManager.getHasPathVariableApiCache();
        return hasPathVariableApiCache.stream()
                .filter(item -> pathMatcher.match(item, requestUri))
                .findFirst()
                .orElse(requestUri);
    }

    private AuthenticationRequest createAuthenticationRequest(HttpServletRequest request, String requestUri, String userCode) {
        // 检查是否有权限
        AuthenticationRequest authReq = new AuthenticationRequest();
        authReq.setUserCode(userCode);
        authReq.setUrl(requestUri);
        authReq.setMethod(request.getMethod());
        authReq.setApplication("permission");
        return authReq;
    }

    private String createKey(String url, String method, Long applicationId) {
        return url + ":" + method + ":" + applicationId;
    }

    private boolean attemptMatchNoNeedAuthorizationUrl(String url, String method, Long applicationId) {
        return apiCacheManager.getNoNeedAuthorizationApiCache().get(createKey(url, method, applicationId)) != null;
    }

    public boolean attemptMatchNoNeedAuthenticationUrl(String url, String method, Long applicationId) {
        return apiCacheManager.getNoNeedAuthenticationApiCache().get(createKey(url, method, applicationId)) != null;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }

}