package com.kt.upms.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    @GetMapping("/hello/{id}/user/{sid}")
    public String hello(HttpServletRequest request, @PathVariable Integer id, @PathVariable Integer sid) {
        return "hello";
    }

    @PreAuthorize("hasP('user123')")
    @PostMapping("/admin/hello")
    public String admin() {
        return "admin";
    }

//    @PreAuthorize("hasRole('user123')")
    @PostMapping("/user/hello")
    public Authentication user() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
