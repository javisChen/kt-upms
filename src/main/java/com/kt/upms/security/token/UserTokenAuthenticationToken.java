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

    public UserTokenAuthenticationToken(String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.credentials = credentials;
        this.setAuthenticated(true);
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
