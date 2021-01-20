package com.kt.upms.security.access;

import com.kt.upms.security.service.IApiAuthService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Component
public class PermissionChecker implements InitializingBean {

    private final String ADMIN = "Admin";

    @Autowired
    private IApiAuthService iApiAuthService;
    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    private Map<String, String> map = new HashMap<>();

    public boolean check(HttpServletRequest request, Authentication authentication) {
//        String requestURI = request.getRequestURI();
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            if (new AntPathMatcher().match(entry.getKey(), requestURI)) {
//                requestURI = entry.getValue();
//                break;
//            }
//        }
        return true;
//         Admin直接通过
//        if (ADMIN.equals(authentication.getPrincipal())) {
//            return true;
//        }
//        UserTokenAuthenticationToken authenticationToken = (UserTokenAuthenticationToken) authentication;
//        Long userId = authenticationToken.getUserId();
//        // 检查是否有权限
//        AuthenticationRequest authReq = new AuthenticationRequest();
//        authReq.setUserId(userId);
//        authReq.setUrl(requestURI);
//        authReq.setMethod(request.getMethod());
//        authReq.setApplication("permission");
//        AuthenticationResponse authResp = iApiAuthService.auth(authReq);
//        return authResp.getHasPermission();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        map.put("/upms/demo/hello/*/user/*", "/upms/hello/{id}/user/{sid}");
    }
}
