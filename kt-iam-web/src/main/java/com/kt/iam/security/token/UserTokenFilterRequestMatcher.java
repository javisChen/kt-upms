package com.kt.iam.security.token;

import com.kt.iam.security.access.LocalAuthCheck;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class UserTokenFilterRequestMatcher implements RequestMatcher {

    private LocalAuthCheck localAuthCheck;

    public UserTokenFilterRequestMatcher(LocalAuthCheck localAuthCheck) {
        this.localAuthCheck = localAuthCheck;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        Long applicationId = 1L;

        // 先尝试uri是否匹配系统中存在的包含路径参数的api，如果存在的话就替换成统一的格式
        requestUri = localAuthCheck.attemptReplaceHasPathVariableUrl(requestUri);

        // 尝试匹配是否不需要授权的api
        if (localAuthCheck.checkUriIsNoNeedAuthentication(requestUri, method, applicationId)) {
            return false;
        }

        // 尝试匹配默认放行的资源
        if (localAuthCheck.matchDefaultAllowUrl(requestUri)) {
            return false;
        }
        return true;
    }

}