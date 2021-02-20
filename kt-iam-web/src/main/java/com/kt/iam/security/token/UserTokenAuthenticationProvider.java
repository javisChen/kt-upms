package com.kt.iam.security.token;


import com.kt.iam.auth.core.cache.UserTokenCache;
import com.kt.iam.auth.core.model.LoginUserContext;
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
    private UserTokenCache userTokenCache;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String accessToken = (String) authentication.getCredentials();
        if (StringUtils.isBlank(accessToken)) {
            throw new AuthenticationServiceException("Authentication failed: token is invalid");
        }

        LoginUserContext userDetails = userTokenCache.get(accessToken);
        if (userDetails == null) {
            throw new AuthenticationServiceException("Authentication failed: token is invalid");
        }
        return new UserTokenAuthenticationToken(userDetails.getUserCode(), accessToken,
                authentication.getAuthorities(), userDetails);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UserTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }

}

