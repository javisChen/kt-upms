/*
 * 版权所有.(c)2008-2019.广州市森锐科技股份有限公司
 */
package com.kt.upms.security.token;


import com.alibaba.fastjson.JSONObject;
import com.kt.component.dto.ResponseEnums;
import com.kt.component.dto.ServerResponse;
import com.kt.upms.security.access.ApiAccessChecker;
import com.kt.upms.security.configuration.SecurityCoreProperties;
import com.kt.upms.security.context.LoginUserContextHolder;
import com.kt.upms.security.model.LoginUserContext;
import com.kt.upms.security.token.extractor.TokenExtractor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * @author JavisChen
 * @desc Token认证过滤
 * @date 2019-10-19 17:17
 */
public class UserTokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private TokenExtractor tokenExtractor;
    private SecurityCoreProperties securityCoreProperties;

    public UserTokenAuthenticationProcessingFilter(TokenExtractor tokenExtractor,
                                                   SecurityCoreProperties securityCoreProperties,
                                                   ApiAccessChecker apiAccessChecker) {
        super(new UserTokenFilterRequestMatcher(apiAccessChecker));
        this.tokenExtractor = tokenExtractor;
        this.securityCoreProperties = securityCoreProperties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String token = tokenExtractor.extract(request, securityCoreProperties.getAuthentication());
        UserTokenAuthenticationToken authentication = new UserTokenAuthenticationToken(token, null);
        return getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UserTokenAuthenticationToken userTokenAuthenticationToken = (UserTokenAuthenticationToken) authentication;
        userTokenAuthenticationToken.setAuthenticated(true);
        context.setAuthentication(userTokenAuthenticationToken);
        SecurityContextHolder.setContext(context);

        LoginUserContext details = userTokenAuthenticationToken.getDetails();
        setLoginUserContext(details);

        chain.doFilter(request, response);
    }

    private void setLoginUserContext(LoginUserContext details) {
        LoginUserContextHolder.setContext(details);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        setupResponse(response);
        SecurityContextHolder.clearContext();
        JSONObject.writeJSONString(response.getWriter(), response(ResponseEnums.USER_AUTHENTICATION_FAIL.getCode(),
                failed.getMessage()));
    }

    private void setupResponse(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    private ServerResponse response(String code, String msg) {
        return ServerResponse.error(code, msg);
    }
}
