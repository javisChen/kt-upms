package com.kt.upms.auth.core.check;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.kt.component.dto.ResponseEnums;
import com.kt.component.dto.ServerResponse;
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
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
public class AuthCheckFilter extends GenericFilterBean {

    private TokenExtractor tokenExtractor = new DefaultTokenExtractor();
    private AccessTokenProperties accessTokenProperties = new AccessTokenProperties();
    private AuthProperties authProperties;
    private List<String> allowList;
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    public AuthCheckFilter(AuthProperties authProperties, List<String> allowList) {
        this.authProperties = authProperties;
        this.allowList = allowList;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        boolean debugEnabled = log.isDebugEnabled();
        logDebug(debugEnabled, "Auth Filter：Processing Auth Check......");

        // 检查下当前接口是否不走权限中心
        if (matchNoPermission(request)) {
            LoginUserContextHolder.setContext(null);
            chain.doFilter(request, response);
            return;
        }

        AuthResponse authResponse = requestCheckPermission(request);
        if (authResponse.getHasPermission()) {
            logDebug(debugEnabled, "Auth Filter：Check Result： Pass");
            LoginUserContextHolder.setContext(authResponse.getLoginUserContext());
            chain.doFilter(request, response);
            return;
        }

        logDebug(debugEnabled, "Auth Filter：Check Result： Fail, Reason：{}", authResponse.getMsg());
        responseFail(response, HttpStatus.FORBIDDEN, authResponse.getCode(), authResponse.getMsg());
    }

    private boolean matchNoPermission(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        final boolean b = allowList.stream().anyMatch(item -> pathMatcher.match(item, servletPath));
        return b;
    }

    private void responseFail(HttpServletResponse response, HttpStatus httpStatus, ResponseEnums responseEnums) throws IOException {
        responseFail(response, httpStatus, responseEnums.getCode(), responseEnums.getMsg());
    }

    private void responseFail(HttpServletResponse response, HttpStatus httpStatus, String code, String msg) throws IOException {
        response.setStatus(httpStatus.value());
        JSONObject.writeJSONString(response.getOutputStream(), ServerResponse.error(code, msg));
    }

    /**
     * 请求权限中心
     */
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

        AuthResponse authResponse = JSONObject.parseObject(authResponseBody, AuthResponse.class);
        logDebug(log.isDebugEnabled(), "Receive Auth Result -----> {}", authResponse);
        return authResponse;
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
