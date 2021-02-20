package com.kt.iam.auth.core.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class LoginUserDetails extends User {

    private Long userId;
    private String userCode;
    private String accessToken;
    private Long expires;
    private Boolean isSuperAdmin;

    public Boolean getSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(Boolean superAdmin) {
        isSuperAdmin = superAdmin;
    }

    public String getUserCode() {
        return userCode;
    }

    public Boolean getIsSuperAdmin() {
        return isSuperAdmin;
    }

    public void setIsSuperAdmin(Boolean isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

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

    public LoginUserDetails(Boolean isSuperAdmin, Long userId, String userCode, String username, String password,
                            Collection<? extends GrantedAuthority> authorities) {
        super(username, StringUtils.defaultString(password, ""), authorities);
        this.userId = userId;
        this.userCode = userCode;
        this.isSuperAdmin = isSuperAdmin;
    }

    public LoginUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
                            Long userId, String userCode, String accessToken, Long expires, Boolean isSuperAdmin) {
        super(username, password, authorities);
        this.userId = userId;
        this.userCode = userCode;
        this.accessToken = accessToken;
        this.expires = expires;
        this.isSuperAdmin = isSuperAdmin;
    }

    public LoginUserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
                            boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
                            Long userId, String userCode, String accessToken, Long expires, Boolean isSuperAdmin) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.userCode = userCode;
        this.accessToken = accessToken;
        this.expires = expires;
        this.isSuperAdmin = isSuperAdmin;
    }
}
