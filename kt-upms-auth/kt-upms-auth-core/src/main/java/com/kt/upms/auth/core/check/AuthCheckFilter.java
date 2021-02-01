package com.kt.upms.auth.core.check;

import com.alibaba.fastjson.JSONObject;
import com.kt.component.dto.ResponseEnums;
import com.kt.component.dto.ServerResponse;
import com.kt.upms.auth.core.cache.UserTokenCache;
import com.kt.upms.auth.core.context.LoginUserContextHolder;
import com.kt.upms.auth.core.extractor.DefaultTokenExtractor;
import com.kt.upms.auth.core.extractor.TokenExtractor;
import com.kt.upms.auth.core.model.AuthRequest;
import com.kt.upms.auth.core.model.AuthResponse;
import com.kt.upms.auth.core.model.LoginUserContext;
import com.kt.upms.config.AccessTokenProperties;
import com.kt.upms.config.AuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthCheckFilter extends GenericFilterBean {

    private TokenExtractor tokenExtractor = new DefaultTokenExtractor();
    private AccessTokenProperties accessTokenProperties = new AccessTokenProperties();
    private UserTokenCache userTokenCache;
    private AuthCheck authCheck;

    public AuthCheckFilter(UserTokenCache userTokenCache, AuthProperties authProperties) {
        this.userTokenCache = userTokenCache;
        this.authCheck = new RemoteAuthCheck(authProperties);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        boolean debugEnabled = log.isDebugEnabled();
        logDebug(debugEnabled, "Auth Filter：Processing Auth Check......");

        String token = tokenExtractor.extract(request, accessTokenProperties);
        logDebug(debugEnabled, "Auth Filter：Current Token -> [{}]", token);
        if (StringUtils.isBlank(token)) {
            logDebug(debugEnabled, "Auth Filter：Token [{}] is blank", token);
            responseFail(response, HttpStatus.UNAUTHORIZED, ResponseEnums.USER_AUTHENTICATION_FAIL);
            return;
        }

        LoginUserContext loginUserContext = userTokenCache.get(token);
        if (loginUserContext == null) {
            logDebug(debugEnabled, "Auth Filter：Token [{}] is invalid", token);
            responseFail(response, HttpStatus.UNAUTHORIZED, ResponseEnums.USER_AUTHENTICATION_FAIL);
            return;
        }

        AuthResponse authResponse = requestCheckPermission(loginUserContext, request);
        if (authResponse.getHasPermission()) {
            logDebug(debugEnabled, "Auth Filter：Token [{}] Check Result： Pass", token);
            LoginUserContextHolder.setContext(loginUserContext);
            chain.doFilter(request, response);
            return;
        }

        logDebug(debugEnabled, "Auth Filter：Token [{}] Check Result： Fail, Reason：{}", token, authResponse.getMsg());
        responseFail(response, HttpStatus.FORBIDDEN, ResponseEnums.USER_ACCESS_DENIED);
    }

    private void responseFail(HttpServletResponse response, HttpStatus httpStatus, ResponseEnums responseEnums) throws IOException {
        responseFail(response, httpStatus, responseEnums.getCode(), responseEnums.getMsg());
    }

    private void responseFail(HttpServletResponse response, HttpStatus httpStatus, String code, String msg) throws IOException {
        response.setStatus(httpStatus.value());
        JSONObject.writeJSONString(response.getOutputStream(), ServerResponse.error(code, msg));
    }

    private AuthResponse requestCheckPermission(LoginUserContext loginUserContext, HttpServletRequest request) {
        AuthRequest authRequest = createAuthRequest(loginUserContext, request);
        return authCheck.checkPermission(authRequest);
    }

    private AuthRequest createAuthRequest(LoginUserContext loginUserContext, HttpServletRequest request) {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserId(loginUserContext.getUserId());
        authRequest.setUserCode(loginUserContext.getUserCode());
        authRequest.setUrl(request.getRequestURI());
        authRequest.setMethod(request.getMethod());
        authRequest.setApplicationCode(authRequest.getApplicationCode());
        return authRequest;
    }

    private void logDebug(boolean debugEnabled, String s, Object... o) {
        if (debugEnabled) {
            log.debug(s, o);
        }
    }

}
