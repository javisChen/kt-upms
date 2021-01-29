package com.kt.upms.module.permission.controller;


import com.kt.component.dto.SingleResponse;
import com.kt.component.web.base.BaseController;
import com.kt.upms.auth.core.model.AuthRequest;
import com.kt.upms.auth.core.model.AuthResponse;
import com.kt.upms.module.permission.service.IPermissionService;
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
public class PermissionController extends BaseController {

    @Autowired
    private IPermissionService iPermissionService;

    @PostMapping("/permission/check")
    public SingleResponse<AuthResponse> checkPermission(@RequestBody AuthRequest request) {
        AuthResponse response = iPermissionService.checkPermission(request);
        return SingleResponse.ok(response);
    }

}

