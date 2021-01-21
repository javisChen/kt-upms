package com.kt.upms.security.token;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class UserTokenFilterRequestMatcher implements RequestMatcher {

    private List<String> allowList;
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean matches(HttpServletRequest request) {
        if ("DEV".equals(request.getHeader("X-DEV-MODE"))) {
            return false;
        }
        if (CollectionUtil.isEmpty(allowList)) {
            return true;
        }
        return allowList.stream().noneMatch(item -> pathMatcher.match(item, request.getServletPath()));
    }

    public UserTokenFilterRequestMatcher(List<String> allowList) {
        this.allowList = allowList;
    }
}