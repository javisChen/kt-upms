//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.kt.upms.security;

import cn.hutool.crypto.digest.DigestUtil;
import com.kt.upms.constants.UpmsConsts;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DefaultAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider implements InitializingBean {

    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    public DefaultAuthenticationProvider() {

    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.logger.debug("认证失败: 无效的密码");
            throw new BadCredentialsException("无效的密码");
        } else {
            String presentedPassword = authentication.getCredentials().toString();
            if (!this.passwordEncoder.matches(DigestUtil.md5Hex(presentedPassword) + UpmsConsts.USER_SALT, userDetails.getPassword())) {
                this.logger.debug("认证失败: 密码不匹配");
                throw new BadCredentialsException("密码不匹配");
            }
        }
    }

    @Override
    protected void doAfterPropertiesSet() {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        try {
            return this.userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException | InternalAuthenticationServiceException usernameNotFoundException) {
            throw usernameNotFoundException;
        } catch (Exception exception) {
            throw new InternalAuthenticationServiceException(exception.getMessage(), exception);
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        return super.createSuccessAuthentication(principal, authentication, user);
    }


}
