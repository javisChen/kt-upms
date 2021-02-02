package com.kt.upms.security.access;

import com.kt.upms.auth.core.check.AuthCheck;
import com.kt.upms.module.api.cache.ApiCacheHolder;
import com.kt.upms.module.application.persistence.UpmsApplication;
import com.kt.upms.module.application.service.IApplicationService;
import com.kt.upms.security.configuration.SecurityCoreProperties;
import org.springframework.util.AntPathMatcher;

import java.util.List;

public abstract class AbstractAuthCheck implements AuthCheck {

    private final SecurityCoreProperties securityCoreProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final ApiCacheHolder apiCacheHolder;
    private final IApplicationService iApplicationService;

    public AbstractAuthCheck(SecurityCoreProperties securityCoreProperties,
                             ApiCacheHolder apiCacheHolder,
                             IApplicationService iApplicationService) {
        this.securityCoreProperties = securityCoreProperties;
        this.apiCacheHolder = apiCacheHolder;
        this.iApplicationService = iApplicationService;
    }

    public boolean matchDefaultAllowUrl(String requestUri) {
        return securityCoreProperties.getAllowList().stream()
                .anyMatch(item -> pathMatcher.match(item, requestUri));
    }

    public String attemptReplaceHasPathVariableUrl(String requestUri) {
        List<String> hasPathVariableApiCache = apiCacheHolder.getHasPathVariableApiCache();
        return hasPathVariableApiCache.stream()
                .filter(item -> pathMatcher.match(item, requestUri))
                .findFirst()
                .orElse(requestUri);
    }

    private String createKey(String url, String method, Long applicationId) {
        return url + ":" + method + ":" + applicationId;
    }

    /**
     * 尝试匹配无需授权的资源
     * @return 匹配成功=true，不成功=false
     */
    public boolean checkUriIsNoNeedAuthorization(String url, String method, Long applicationId) {
        return apiCacheHolder.getNoNeedAuthorizationApiCache().get(createKey(url, method, applicationId)) != null;
    }

    /**
     * 尝试匹配无需认证的资源
     * @return 匹配成功=true，不成功=false
     */
    public boolean checkUriIsNoNeedAuthentication(String url, String method, Long applicationId) {
        return apiCacheHolder.getNoNeedAuthenticationApiCache().get(createKey(url, method, applicationId)) != null;
    }

    public UpmsApplication getApplicationByCode(String applicationCode) {
        return iApplicationService.getApplicationByCode(applicationCode);
    }

}
