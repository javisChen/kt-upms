package com.kt.upms.security.token;

import cn.hutool.core.collection.CollectionUtil;
import com.kt.upms.security.access.ApiAccessChecker;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class UserTokenFilterRequestMatcher implements RequestMatcher {

    private List<String> allowList;
    private ApiAccessChecker apiAccessChecker;
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    public UserTokenFilterRequestMatcher(List<String> allowList, ApiAccessChecker apiAccessChecker) {
        this.allowList = allowList;
        this.apiAccessChecker = apiAccessChecker;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        Long applicationId = 1L;

        // 先尝试uri是否匹配系统中存在的包含路径参数的api，如果存在的话就替换成统一的格式
        requestUri = apiAccessChecker.attemptReplaceHasPathVariableUrl(requestUri);

        // 尝试是否匹配不需要授权的api
        if (apiAccessChecker.attemptMatchNoNeedAuthenticationUrl(requestUri, method, applicationId)) {
            return false;
        }

        if (CollectionUtil.isEmpty(allowList)) {
            return true;
        }
        return allowList.stream().noneMatch(item -> pathMatcher.match(item, request.getServletPath()));
    }

}