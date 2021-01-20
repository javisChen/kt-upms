//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.kt.upms.security.login;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.kt.upms.constants.UpmsConsts;
import com.kt.upms.security.common.RedisKeyConst;
import com.kt.upms.security.configuration.SecurityCoreProperties;
import com.kt.upms.security.token.manager.UserTokenManager;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class UserLoginAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider implements InitializingBean {

    @Autowired
    private UserTokenManager userTokenManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private SecurityCoreProperties securityCoreProperties;

    public UserLoginAuthenticationProvider() {

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
        // 认证成功后执行缓存
        LoginUserDetails loginUserDetails = (LoginUserDetails) user;
        String key = RedisKeyConst.USER_ACCESS_TOKEN_KEY_PREFIX + loginUserDetails.getAccessToken();
        loginUserDetails.setExpires(securityCoreProperties.getAuthentication().getExpire());
        userTokenManager.save(key, JSONObject.toJSONString(user), loginUserDetails.getExpires());
        return super.createSuccessAuthentication(principal, authentication, user);
    }

}
