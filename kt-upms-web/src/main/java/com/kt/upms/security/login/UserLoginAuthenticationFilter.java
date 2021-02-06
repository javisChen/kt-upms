package com.kt.upms.security.login;

import com.alibaba.fastjson.JSONObject;
import com.kt.component.dto.ResponseEnums;
import com.kt.component.dto.ServerResponse;
import com.kt.component.dto.SingleResponse;
import com.kt.upms.auth.core.cache.UserCacheInfo;
import com.kt.upms.auth.core.cache.UserTokenCache;
import com.kt.upms.auth.core.context.LoginUserContextHolder;
import com.kt.upms.auth.core.model.LoginUserContext;
import com.kt.upms.auth.core.model.LoginUserDetails;
import com.kt.upms.security.configuration.SecurityCoreProperties;
import com.kt.upms.security.model.SecurityLoginRequest;
import com.kt.upms.security.model.SecurityLoginResult;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 账号密码登录过滤器
 */
public class UserLoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private String filterProcessesUrl = "/auth/login";
    private SecurityCoreProperties securityCoreProperties;
    private UserTokenCache userTokenCache;

    public UserLoginAuthenticationFilter(AuthenticationManager authenticationManager,
                                         SecurityCoreProperties securityCoreProperties,
                                         UserTokenCache userTokenCache) {
        setAuthenticationSuccessHandler(authenticationSuccessHandler());
        setAuthenticationFailureHandler(authenticationFailureHandler());
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl(filterProcessesUrl);
        this.securityCoreProperties = securityCoreProperties;
        this.userTokenCache = userTokenCache;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        doCheck(request);
        SecurityLoginRequest loginRequest = parseLoginRequest(request);
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private void doCheck(HttpServletRequest request) {
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
    }

    private SecurityLoginRequest parseLoginRequest(HttpServletRequest request) throws AuthenticationException {
        try {
            return JSONObject.parseObject(request.getInputStream(), SecurityLoginRequest.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException("");
        }
    }

    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (httpServletRequest, response, e) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            setupContentType(response);
            JSONObject.writeJSONString(response.getOutputStream(), ServerResponse.error(ResponseEnums.USER_LOGIN_FAIL));
        };
    }

    private void setupContentType(HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication authentication) -> {
            setupContentType(response);
            // 认证成功后执行缓存
            LoginUserDetails user = (LoginUserDetails) authentication.getPrincipal();
            UserCacheInfo userCacheInfo = cacheAuthentication(user);
            JSONObject.writeJSONString(response.getOutputStream(), SingleResponse.ok(createLoginResultVo(userCacheInfo)));
        };
    }

    private SecurityLoginResult createLoginResultVo(UserCacheInfo userCacheInfo) {
        return new SecurityLoginResult(userCacheInfo.getAccessToken(), userCacheInfo.getExpires());
    }

    private UserCacheInfo cacheAuthentication(LoginUserDetails user) {
        LoginUserContext loginUserContext = buildLoginUserContext(user);
        return userTokenCache.save(JSONObject.toJSONString(loginUserContext));
    }

    private LoginUserContext buildLoginUserContext(LoginUserDetails details) {
        LoginUserContext loginUserContext = LoginUserContextHolder.getContext();
        loginUserContext.setUserId(details.getUserId());
        loginUserContext.setUserCode(details.getUserCode());
        loginUserContext.setUsername(details.getUsername());
        loginUserContext.setIsSuperAdmin(details.getIsSuperAdmin());
        return loginUserContext;
    }
}