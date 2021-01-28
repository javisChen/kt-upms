//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.kt.upms.security.token;

import com.kt.upms.security.model.LoginUserContext;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserTokenAuthenticationToken extends AbstractAuthenticationToken {

        private String principal;
        private String credentials;
        private LoginUserContext details;

        public UserTokenAuthenticationToken(String principal, String credentials,
                                            Collection<? extends GrantedAuthority> authorities,
                                            LoginUserContext details) {
            super(authorities);
            this.credentials = credentials;
            this.principal = principal;
            this.setDetails(details);
        }

        public UserTokenAuthenticationToken(String credentials, Collection<? extends GrantedAuthority> authorities) {
            super(authorities);
            this.credentials = credentials;
        }

    @Override
    public LoginUserContext getDetails() {
        return details;
    }

    public void setDetails(LoginUserContext details) {
        this.details = details;
    }

    public void setCredentials(String credentials) {
            this.credentials = credentials;
        }


        @Override
        public String getCredentials() {
            return credentials;
        }

        @Override
        public String getPrincipal() {
            return this.principal;
        }

        @Override
        public void eraseCredentials() {
        }
}
