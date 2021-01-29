package com.kt.upms.auth.core.check;

import cn.hutool.http.HttpUtil;
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
import com.kt.upms.config.AuthProperties;
import com.kt.upms.config.AuthenticationProperties;
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
    private AuthenticationProperties authenticationProperties = new AuthenticationProperties();
    private UserTokenCache userTokenCache;
    private AuthProperties authProperties;

    public AuthCheckFilter(UserTokenCache userTokenCache, AuthProperties authProperties) {
        this.userTokenCache = userTokenCache;
        this.authProperties = authProperties;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        boolean debug = log.isDebugEnabled();

        try {

            String token = tokenExtractor.extract(request, authenticationProperties);
            if (StringUtils.isBlank(token)) {
                responseFail(response, HttpStatus.UNAUTHORIZED, ResponseEnums.USER_AUTHENTICATION_FAIL);
                return;
            }

            LoginUserContext loginUserContext = userTokenCache.get(token);
            if (loginUserContext == null) {
                responseFail(response, HttpStatus.UNAUTHORIZED, ResponseEnums.USER_AUTHENTICATION_FAIL);
                return;
            }

            AuthResponse authResponse = requestCheckPermission(loginUserContext, request);
            if (authResponse.getHasPermission()) {
                LoginUserContextHolder.setContext(loginUserContext);
                chain.doFilter(request, response);
                return;
            }

            responseFail(response, HttpStatus.FORBIDDEN, ResponseEnums.USER_ACCESS_DENIED.getCode(), authResponse.getMsg());
        } finally {

        }
    }

    private void responseFail(HttpServletResponse response, HttpStatus httpStatus, ResponseEnums responseEnums) throws IOException {
        responseFail(response, httpStatus, responseEnums.getCode(), responseEnums.getMsg());
    }

    private void responseFail(HttpServletResponse response, HttpStatus httpStatus, String code, String msg) throws IOException {
        response.setStatus(httpStatus.value());
        JSONObject.writeJSONString(response.getOutputStream(), ServerResponse.error(code, msg));
    }

    private AuthResponse requestCheckPermission(LoginUserContext loginUserContext, HttpServletRequest request) {
        String serverUrl = authProperties.getServerUrl();
        AuthRequest authRequest = createAuthRequest(loginUserContext, request);
        String body = JSONObject.toJSONString(authRequest);
        String authResponseBody = null;
        try {
            authResponseBody = HttpUtil.post(serverUrl, body, authProperties.getTimeout());
        } catch (Exception e) {
            log.error("统一认证服务请求失败", e);
            return AuthResponse.create(false, "Auth Server Error：" + e.getMessage());
        }
        if (StringUtils.isBlank(authResponseBody)) {
            return AuthResponse.create(false, "Auth Server Error：Unknown Error");
        }
        return JSONObject.parseObject(authResponseBody, AuthResponse.class);
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

}
