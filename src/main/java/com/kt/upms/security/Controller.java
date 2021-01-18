package com.kt.upms.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@RestController
@RequestMapping("/demo")
public class Controller {

    @GetMapping
    public Authentication demo() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    //    @PreAuthorize("hasAuthority('pms:product:create')")
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/admin/hello")
    public String admin() {
        return "admin";
    }

    @PreAuthorize("hasRole('user')")
    @PostMapping("/user/hello")
    public Authentication user() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
