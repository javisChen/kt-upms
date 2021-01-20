//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.kt.upms.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserTokenAuthenticationToken extends AbstractAuthenticationToken {

    private String credentials;
    private Long userId;

    public UserTokenAuthenticationToken(Long userId, String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.credentials = credentials;
        this.userId = userId;
        this.setAuthenticated(true);
    }

    public UserTokenAuthenticationToken(String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.credentials = credentials;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public void eraseCredentials() {
    }
}
