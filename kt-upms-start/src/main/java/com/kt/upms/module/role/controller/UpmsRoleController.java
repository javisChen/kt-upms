package com.kt.upms.module.role.controller;


import com.kt.component.dto.MultiResponse;
import com.kt.component.dto.PageResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.dto.SingleResponse;
import com.kt.component.web.base.BaseController;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.role.dto.RolePermissionUpdateDTO;
import com.kt.upms.module.role.dto.RoleQueryDTO;
import com.kt.upms.module.role.dto.RoleUpdateDTO;
import com.kt.upms.module.role.service.IUpmsRoleService;
import com.kt.upms.module.role.vo.RoleBaseVO;
import com.kt.upms.module.role.vo.RoleListVO;
import com.kt.upms.validgroup.UpmsValidateGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
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
public class UpmsRoleController extends BaseController {

    private final IUpmsRoleService iUpmsRoleService;

    public UpmsRoleController(IUpmsRoleService iUpmsRoleService) {
        this.iUpmsRoleService = iUpmsRoleService;
    }

    @PostMapping("/roles")
    public SingleResponse<PageResponse<RoleListVO>> list(@RequestBody RoleQueryDTO dto) {
        return SingleResponse.ok(PageResponse.build(iUpmsRoleService.pageList(dto)));
    }

    @GetMapping("/roles/all")
    public MultiResponse<RoleListVO> listAll() {
        return MultiResponse.ok(iUpmsRoleService.listAllVos());
    }

    @PostMapping("/role")
    public ServerResponse add(@RequestBody @Validated RoleUpdateDTO dto) {
        iUpmsRoleService.saveRole(dto);
        return ServerResponse.ok();
    }

    @PutMapping("/role")
    public ServerResponse update(@RequestBody @Validated RoleUpdateDTO dto) {
        iUpmsRoleService.updateRoleById(dto);
        return ServerResponse.ok();
    }

    @GetMapping("/role/{id}")
    public SingleResponse<RoleBaseVO> get(@PathVariable("id") String id) {
        RoleBaseVO vo = iUpmsRoleService.getRoleVoById(id);
        return SingleResponse.ok(vo);
    }

    @PutMapping("/role/status")
    public ServerResponse updateStatus(@Validated({UpmsValidateGroup.UpdateStatus.class, Default.class})
                                       @RequestBody RoleUpdateDTO dto) {
        iUpmsRoleService.updateStatus(dto);
        return ServerResponse.ok();
    }

    /**
     * 角色权限授权
     */
    @PostMapping("/role/permission/routes")
    public ServerResponse updateRoleRoutePermissions(@RequestBody RolePermissionUpdateDTO dto) {
        iUpmsRoleService.updateRoleRoutePermissions(dto);
        return ServerResponse.ok();
    }

    /**
     * 获取角色拥有的路由权限
     */
    @GetMapping("/role/permission/routes")
    public MultiResponse<PermissionVO> getRoleRoutePermission(Long roleId) {
        List<PermissionVO> vos = iUpmsRoleService.getRoleRoutePermissionById(roleId);
        return MultiResponse.ok(vos);
    }

    /**
     * 获取角色拥有的页面元素权限
     */
    @GetMapping("/role/permission/elements")
    public MultiResponse<PermissionVO> getRoleElementPermission(Long roleId) {
        List<PermissionVO> vos = iUpmsRoleService.getRoleElementPermissionById(roleId);
        return MultiResponse.ok(vos);
    }
}

