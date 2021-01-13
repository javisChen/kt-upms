package com.kt.upms.module.permission.controller;

import com.kt.component.dto.MultiResponse;
import com.kt.component.web.base.BaseController;
import com.kt.upms.module.permission.service.IUpmsPermissionService;
import com.kt.upms.module.route.vo.RouteElementVO;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
@RestController
@RequestMapping
public class UpmsPermissionController extends BaseController {

    private final IUpmsPermissionService iUpmsPermissionService;

    public UpmsPermissionController(IUpmsPermissionService iUpmsPermissionService) {
        this.iUpmsPermissionService = iUpmsPermissionService;
    }

    /**
     * 获取元素权限
     */
    @GetMapping("/permission/elements")
    public MultiResponse<RouteElementVO> getPermissionsElements(Long routePermissionId) {
        return MultiResponse.ok(iUpmsPermissionService.getPermissionElements(routePermissionId));
    }

}

