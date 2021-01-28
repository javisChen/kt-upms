package com.kt.upms.module.role.controller;


import com.kt.component.dto.MultiResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.web.base.BaseController;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.role.dto.RoleApiPermissionUpdateDTO;
import com.kt.upms.module.role.dto.RoleRoutePermissionUpdateDTO;
import com.kt.upms.module.role.service.IRoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
public class RolePermissionController extends BaseController {

    private final IRoleService iRoleService;

    public RolePermissionController(IRoleService iRoleService) {
        this.iRoleService = iRoleService;
    }

    /**
     * 角色路由权限授权
     */
    @PostMapping("/role/permission/route")
    public ServerResponse updateRoleRoutePermissions(@RequestBody RoleRoutePermissionUpdateDTO dto) {
        iRoleService.updateRoleRoutePermissions(dto);
        return ServerResponse.ok();
    }

    /**
     * 角色api权限授权
     */
    @PostMapping("/role/permission/api")
    public ServerResponse updateRoleApiPermissions(@RequestBody RoleApiPermissionUpdateDTO dto) {
        iRoleService.updateRoleApiPermissions(dto);
        return ServerResponse.ok();
    }

    /**
     * 获取角色拥有的路由权限
     */
    @GetMapping("/role/permission/routes")
    public MultiResponse<PermissionVO> getRoleRoutePermission(Long roleId, Long applicationId) {
        List<PermissionVO> vos = iRoleService.getRoleRoutePermissionById(roleId, applicationId);
        return MultiResponse.ok(vos);
    }

    /**
     * 获取角色拥有的页面元素权限
     */
    @GetMapping("/role/permission/elements")
    public MultiResponse<PermissionVO> getRoleElementPermission(Long roleId, Long applicationId) {
        List<PermissionVO> vos = iRoleService.getRoleElementPermissionById(roleId, applicationId);
        return MultiResponse.ok(vos);
    }

    /**
     * 获取角色拥有的Api权限
     */
    @GetMapping("/role/permission/apis")
    public MultiResponse<PermissionVO> getRoleApiPermission(Long roleId, Long applicationId) {
        List<PermissionVO> vos = iRoleService.getRoleApiPermissionById(roleId, applicationId);
        return MultiResponse.ok(vos);
    }
}

