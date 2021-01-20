package com.kt.upms.security.login;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
public class LoginUserDetails extends User {

    private Long userId;
    private String accessToken;
    private Long expires;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public LoginUserDetails(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
    }


}
