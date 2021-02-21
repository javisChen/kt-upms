package com.kt.iam.security.access;

import com.kt.iam.auth.core.check.AuthCheck;
import com.kt.iam.auth.core.model.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 访问校验
 */
@Component
public class ApiAccessChecker {

    @Autowired
    private AuthCheck localAuthCheck;

    public boolean check(HttpServletRequest request, Authentication authentication) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        String userCode = (String) authentication.getPrincipal();
        String applicationCode = "permission";
        AuthRequest authReq = createAuthenticationRequest(method, requestUri, userCode, applicationCode);
        return localAuthCheck.checkPermission(authReq).getHasPermission();
    }

    private AuthRequest createAuthenticationRequest(String method, String requestUri, String userCode, String applicationCode) {
        // 检查是否有权限
        AuthRequest authReq = new AuthRequest();
        authReq.setUserCode(userCode);
        authReq.setRequestUri(requestUri);
        authReq.setMethod(method);
        authReq.setApplicationCode(applicationCode);
        return authReq;
    }

}
