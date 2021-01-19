//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.kt.upms.security.login;

import com.kt.upms.module.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AccountPasswordUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserService iUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DefaultUser user = iUserService.getUserInfoByPhone(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        } else {
            return new DefaultUser(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(),
                    user.isCredentialsNonExpired(), user.isAccountNonLocked(), user.getAuthorities());
        }
    }

}
