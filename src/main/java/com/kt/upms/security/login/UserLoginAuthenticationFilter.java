package com.kt.upms.security.login;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.component.dto.ResponseEnums;
import com.kt.component.dto.ServerResponse;
import com.kt.component.dto.SingleResponse;
import com.kt.upms.security.model.SecurityLoginResult;
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

    public UserLoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        setAuthenticationSuccessHandler(authenticationSuccessHandler());
        setAuthenticationFailureHandler(authenticationFailureHandler());
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/auth/login");
    }


    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (httpServletRequest, response, e) -> {
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
            LoginUserDetails user = (LoginUserDetails) authentication.getPrincipal();
            SecurityLoginResult vo = new SecurityLoginResult(user.getAccessToken(), user.getExpires());
            JSONObject.writeJSONString(response.getOutputStream(), SingleResponse.ok(vo));
        };
    }
}