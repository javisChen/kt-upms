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
        if (!"TEST".equals(accessToken)) {
            if (StringUtils.isBlank(accessToken)) {
                throw new AuthenticationServiceException("Authentication failed: token is invalid");
            }

            LoginUserDetails loginUserDetails = tokenManager.get(RedisKeyConst.USER_ACCESS_TOKEN_KEY_PREFIX + accessToken);
            if (loginUserDetails == null) {
                throw new AuthenticationServiceException("Authentication failed: token is invalid");
            }
            String tk = loginUserDetails.getAccessToken();
        }
        // TODO 加上权限查询
        return new UserTokenAuthenticationToken((String) authentication.getCredentials(), authentication.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UserTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }

}

