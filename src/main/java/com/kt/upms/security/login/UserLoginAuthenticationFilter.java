package com.kt.upms.security.login;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.component.dto.ResponseEnums;
import com.kt.component.dto.ServerResponse;
import com.kt.component.dto.SingleResponse;
import com.kt.upms.security.common.RedisKeyConst;
import com.kt.upms.security.configuration.SecurityCoreProperties;
import com.kt.upms.security.model.SecurityLoginResult;
import com.kt.upms.security.token.manager.UserTokenManager;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 账号密码登录过滤器
 */
public class UserLoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final String filterProcessesUrl = "/auth/login";
    private SecurityCoreProperties securityCoreProperties;
    private UserTokenManager userTokenManager;

    public UserLoginAuthenticationFilter(AuthenticationManager authenticationManager,
                                         SecurityCoreProperties securityCoreProperties,
                                         UserTokenManager userTokenManager) {
        setAuthenticationSuccessHandler(authenticationSuccessHandler());
        setAuthenticationFailureHandler(authenticationFailureHandler());
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl(filterProcessesUrl);
        this.securityCoreProperties = securityCoreProperties;
        this.userTokenManager = userTokenManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        String verify_code = (String) request.getSession().getAttribute("verify_code");
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE) || request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
            Map<String, String> loginData = new HashMap<>();
            try {
                loginData = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            } catch (IOException e) {
            } finally {
//                String code = loginData.get("code");
//                checkCode(response, code, verify_code);
            }
            String username = loginData.get(getUsernameParameter());
            String password = loginData.get(getPasswordParameter());
            if (username == null) {
                username = "";
            }
            if (password == null) {
                password = "";
            }
            username = username.trim();
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        } else {
//            checkCode(response, request.getParameter("code"), verify_code);
            return super.attemptAuthentication(request, response);
        }
    }

    public void checkCode(HttpServletResponse resp, String code, String verify_code) {
        if (code == null || verify_code == null || "".equals(code) || !verify_code.toLowerCase().equals(code.toLowerCase())) {
            //验证码不正确
            throw new AuthenticationServiceException("验证码不正确");
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
            // 认证成功后执行缓存
            LoginUserDetails user = (LoginUserDetails) authentication.getPrincipal();
            cacheAuthentication(user);
            setupContentType(response);
            SecurityLoginResult vo = new SecurityLoginResult(user.getAccessToken(), user.getExpires());
            JSONObject.writeJSONString(response.getOutputStream(), SingleResponse.ok(vo));
        };
    }

    private void cacheAuthentication(LoginUserDetails user) {
        String key = RedisKeyConst.USER_ACCESS_TOKEN_KEY_PREFIX + user.getAccessToken();
        user.setExpires(securityCoreProperties.getAuthentication().getExpire());
        userTokenManager.save(key, JSONObject.toJSONString(user), user.getExpires());
    }
}