package com.kt.iam.security.access;

import com.kt.component.dto.ResponseEnums;
import com.kt.iam.auth.core.cache.UserTokenCache;
import com.kt.iam.auth.core.model.AuthRequest;
import com.kt.iam.auth.core.model.AuthResponse;
import com.kt.iam.auth.core.model.LoginUserContext;
import com.kt.iam.module.api.cache.ApiCacheHolder;
import com.kt.iam.module.application.persistence.IamApplication;
import com.kt.iam.module.application.service.IApplicationService;
import com.kt.iam.module.user.service.IUserPermissionService;
import com.kt.iam.security.configuration.SecurityCoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoteAuthCheck extends AbstractAuthCheck {

    @Autowired
    private IUserPermissionService iUserPermissionService;
    @Autowired
    private UserTokenCache userTokenCache;

    public RemoteAuthCheck(SecurityCoreProperties securityCoreProperties,
                           ApiCacheHolder apiCacheHolder,
                           IApplicationService iApplicationService) {
        super(securityCoreProperties, apiCacheHolder, iApplicationService);
    }


    private boolean doCheck(AuthRequest request) {
        boolean result = false;
        boolean accessToken = StringUtils.isNotBlank(request.getAccessToken());
        boolean url = StringUtils.isNotBlank(request.getRequestUri());
        boolean method = StringUtils.isNotBlank(request.getMethod());
        boolean applicationCode = StringUtils.isNotBlank(request.getApplicationCode());
        if (accessToken && url && method && applicationCode) {
            result = true;
        }
        return result;
    }

    @Override
    public AuthResponse checkPermission(AuthRequest authRequest) {
        boolean checkResult = doCheck(authRequest);
        if (!checkResult) {
            return AuthResponse.fail("Request is invalid");
        }
        String requestUri = authRequest.getRequestUri();
        String applicationCode = authRequest.getApplicationCode();
        String method = authRequest.getMethod();
        String accessToken = authRequest.getAccessToken();
        IamApplication application = getApplicationByCode(applicationCode);
        if (application == null) {
            return AuthResponse.fail("Application not exists");
        }
        requestUri = attemptReplaceHasPathVariableUrl(requestUri);
        // 检查是否需要认证，如果需要的话检查token合法性
        Long applicationId = application.getId();
        if (checkUriIsNoNeedAuthentication(requestUri, method, applicationId)) {
            return AuthResponse.success();
        }
        if (StringUtils.isBlank(accessToken)) {
            return AuthResponse.fail(ResponseEnums.USER_AUTHENTICATION_FAIL.getMsg());
        }
        LoginUserContext userContext = userTokenCache.get(accessToken);
        if (userContext == null) {
            return AuthResponse.fail(ResponseEnums.USER_AUTHENTICATION_FAIL.getMsg());
        }

        if (checkUriIsNoNeedAuthorization(requestUri, method, applicationId)) {
            return AuthResponse.success(userContext);
        }

        fillAuthRequest(authRequest, userContext);
        AuthResponse authResponse = iUserPermissionService.checkApiPermission(authRequest);
        authResponse.setLoginUserContext(userContext);
        return authResponse;
    }

    private void fillAuthRequest(AuthRequest authReq, LoginUserContext userContext) {
        authReq.setUserCode(userContext.getUserCode());
        authReq.setUserId(userContext.getUserId());
    }

    private AuthRequest createAuthenticationRequest(String method, String requestUri, LoginUserContext userContext,
                                                    String applicationCode) {
        AuthRequest authReq = new AuthRequest();
        authReq.setUserCode(userContext.getUserCode());
        authReq.setUserId(userContext.getUserId());
        authReq.setRequestUri(requestUri);
        authReq.setMethod(method);
        authReq.setApplicationCode(applicationCode);
        return authReq;
    }
}
