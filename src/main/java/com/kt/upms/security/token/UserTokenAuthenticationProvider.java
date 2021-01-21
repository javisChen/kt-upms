/*
 * 版权所有.(c)2008-2019.广州市森锐科技股份有限公司
 */
package com.kt.upms.security.token;


import com.kt.upms.security.common.RedisKeyConst;
import com.kt.upms.security.login.LoginUserDetails;
import com.kt.upms.security.token.manager.UserTokenManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class UserTokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserTokenManager tokenManager;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String accessToken = (String) authentication.getCredentials();
        if (!"ADMIN".equals(accessToken)) {
            if (StringUtils.isBlank(accessToken)) {
                throw new AuthenticationServiceException("Authentication failed: token is invalid");
            }

            LoginUserDetails userDetails = tokenManager.get(RedisKeyConst.USER_ACCESS_TOKEN_KEY_PREFIX + accessToken);
            if (userDetails == null) {
                throw new AuthenticationServiceException("Authentication failed: token is invalid");
            }
            return new UserTokenAuthenticationToken(userDetails.getUserId(), accessToken,
                    authentication.getAuthorities(), userDetails);
        }

        // TODO 加上权限查询
        return new UserTokenAuthenticationToken(0L, (String) authentication.getCredentials(),
                authentication.getAuthorities(), null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UserTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }

}

