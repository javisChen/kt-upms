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

        boolean debugEnabled = log.isDebugEnabled();
        logDebug(debugEnabled, "Auth Filter：Processing Auth Check......");

        final String requestURI = request.getRequestURI();
        AuthResponse authResponse = requestCheckPermission(request);
        if (authResponse.getHasPermission()) {
            logDebug(debugEnabled, "Auth Filter：Check Result： Pass");
            LoginUserContextHolder.setContext(authResponse.getLoginUserContext());
            chain.doFilter(request, response);
            return;
        }

        logDebug(debugEnabled, "Auth Filter：Check Result： Fail, Reason：{}", authResponse.getMsg());
        responseFail(response, HttpStatus.FORBIDDEN, ResponseEnums.USER_ACCESS_DENIED);
    }

    private void responseFail(HttpServletResponse response, HttpStatus httpStatus, ResponseEnums responseEnums) throws IOException {
        responseFail(response, httpStatus, responseEnums.getCode(), responseEnums.getMsg());
    }

    private void responseFail(HttpServletResponse response, HttpStatus httpStatus, String code, String msg) throws IOException {
        response.setStatus(httpStatus.value());
        JSONObject.writeJSONString(response.getOutputStream(), ServerResponse.error(code, msg));
    }

    private AuthResponse requestCheckPermission(HttpServletRequest request) {
        AuthRequest authRequest = createAuthRequest(request);

        String body = JSONObject.toJSONString(authRequest);
        String authResponseBody = null;
        try {
            authResponseBody = HttpUtil.post(authProperties.getServerUrl(), body, authProperties.getTimeout());
        } catch (Exception e) {
            log.error("统一认证服务请求失败", e);
            return AuthResponse.fail("Auth Server Error：" + e.getMessage());
        }
        if (StringUtils.isBlank(authResponseBody)) {
            return AuthResponse.fail("Auth Server Error：Unknown Error");
        }
        if (log.isDebugEnabled()) {
            log.debug("Receive Auth Result -----> {}", authResponseBody);
        }

        return JSONObject.parseObject(authResponseBody, AuthResponse.class);
    }

    public static void main(String[] args) {
        final String s = "{\"code\":\"000000\",\"hasPermission\":true,\"loginUserContext\":{\"accessToken\":\"44708494-e199-4700-828d-c1348945356b\",\"expires\":43200,\"isSuperAdmin\":false,\"userCode\":\"b6fc6b30b1564ab88c45b16fca44433f\",\"userId\":2,\"username\":\"JCKT\"}}";
        AuthResponse authResponse = JSONObject.parseObject(s, AuthResponse.class);
        System.out.println(authResponse);
    }

    private AuthRequest createAuthRequest(HttpServletRequest request) {
        String token = tokenExtractor.extract(request, accessTokenProperties);
        AuthRequest authRequest = new AuthRequest();
        authRequest.setRequestUri(request.getRequestURI());
        authRequest.setMethod(request.getMethod());
        authRequest.setAccessToken(token);
        authRequest.setApplicationCode(authProperties.getApplicationCode());
        return authRequest;
    }


    private void logDebug(boolean debugEnabled, String s, Object... o) {
        if (debugEnabled) {
            log.debug(s, o);
        }
    }

}
