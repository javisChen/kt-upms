package com.kt.upms.module.auth.controller;


import com.kt.component.dto.ServerResponse;
import com.kt.component.web.base.BaseController;
import com.kt.upms.module.auth.dto.AuthKickDTO;
import com.kt.upms.module.auth.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
@RestController
@RequestMapping
public class AuthController extends BaseController {

    @Autowired
    private IAuthService iAuthService;

    /**
     * 登出
     */
    @PostMapping("/auth/logout")
    public ServerResponse logout() {
        iAuthService.logout(getRequest());
        return ServerResponse.ok();
    }

    /**
     * 踢出（强制用户下线）
     */
    @PostMapping("/auth/kick")
    public ServerResponse kick(@RequestBody AuthKickDTO dto) {
        iAuthService.kick(dto);
        return ServerResponse.ok();
    }
}

