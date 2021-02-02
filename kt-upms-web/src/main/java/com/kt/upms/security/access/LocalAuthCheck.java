package com.kt.upms.security.access;

import com.kt.upms.auth.core.model.AuthRequest;
import com.kt.upms.auth.core.model.AuthResponse;
import com.kt.upms.module.api.cache.ApiCacheHolder;
import com.kt.upms.module.application.persistence.UpmsApplication;
import com.kt.upms.module.application.service.IApplicationService;
import com.kt.upms.module.user.service.IUserPermissionService;
import com.kt.upms.security.configuration.SecurityCoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalAuthCheck extends AbstractAuthCheck {

    @Autowired
    private IUserPermissionService iUserPermissionService;

    public LocalAuthCheck(SecurityCoreProperties securityCoreProperties,
                          ApiCacheHolder apiCacheHolder,
                          IApplicationService iApplicationService) {
        super(securityCoreProperties, apiCacheHolder, iApplicationService);
    }

    @Override
    public AuthResponse checkPermission(AuthRequest authRequest) {
        String requestUri = authRequest.getRequestUri();
        String applicationCode = authRequest.getApplicationCode();
        String method = authRequest.getMethod();
        // 先尝试uri是否匹配系统中存在的包含路径参数的api，如果存在的话就替换成统一的格式
        requestUri = attemptReplaceHasPathVariableUrl(requestUri);

        // 尝试是否匹配默认设置不需要授权的api
        if (matchDefaultAllowUrl(requestUri)) {
            return AuthResponse.success();
        }

        UpmsApplication application = getApplicationByCode(applicationCode);

        // 尝试是否匹配不需要授权的api
        if (checkUriIsNoNeedAuthorization(requestUri, method, application.getId())) {
            return AuthResponse.success();
        }

        return iUserPermissionService.checkPermission(authRequest);
    }
}
