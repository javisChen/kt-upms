package com.kt.upms.security.access;

import com.kt.upms.security.model.AuthenticationRequest;
import com.kt.upms.security.model.AuthenticationResponse;
import com.kt.upms.security.service.IApiAuthService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Component
public class PermissionChecker implements InitializingBean {

    @Autowired
    private IApiAuthService iApiAuthService;

    private Map<String, String> map = new HashMap<>();
    private List<String> dontNeedAuthUrl = new ArrayList<>();

    public boolean check(HttpServletRequest request, Authentication authentication) {

        AuthenticationRequest authReq;
        String requestURI = request.getRequestURI();
        if (allow(requestURI)) {
            return true;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (new AntPathMatcher().match(entry.getKey(), requestURI)) {
                requestURI = entry.getValue();
                break;
            }
        }
        authReq = createAuthenticationRequest(request, requestURI, (String) authentication.getPrincipal());
        AuthenticationResponse authResp = iApiAuthService.auth(authReq);
        return authResp.getHasPermission();
    }

    private AuthenticationRequest createAuthenticationRequest(HttpServletRequest request, String requestURI, String userCode) {
        // 检查是否有权限
        AuthenticationRequest authReq = new AuthenticationRequest();
        authReq.setUserCode(userCode);
        authReq.setUrl(requestURI);
        authReq.setMethod(request.getMethod());
        authReq.setApplication("permission");
        return authReq;
    }

    private boolean allow(String url) {
        return dontNeedAuthUrl.contains(url);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        map.put("/upms/demo/hello/*/user/*", "/upms/hello/{id}/user/{sid}");

        dontNeedAuthUrl.add("/upms/user/permission/elements");
        dontNeedAuthUrl.add("/upms/user/permission/routes");
        dontNeedAuthUrl.add("/upms/user/info");
    }
}
