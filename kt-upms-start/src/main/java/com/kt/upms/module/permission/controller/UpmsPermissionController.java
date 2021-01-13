package com.kt.upms.module.permission.controller;

import cn.hutool.extra.cglib.CglibUtil;
import com.kt.component.dto.MultiResponse;
import com.kt.component.dto.PageRequest;
import com.kt.component.dto.ServerResponse;
import com.kt.component.web.base.BaseController;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.module.permission.service.IUpmsPermissionService;
import com.kt.upms.module.permission.vo.PermissionElementVO;
import com.kt.upms.module.route.dto.PermissionQueryDTO;
import com.kt.upms.module.route.dto.PermissionUpdateDTO;
import com.kt.upms.validgroup.UpmsValidateGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

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
    public MultiResponse<PermissionElementVO> getPermissionsElements(Long routePermissionId) {
        return MultiResponse.ok(iUpmsPermissionService.getPermissionElements(routePermissionId));
    }

}

