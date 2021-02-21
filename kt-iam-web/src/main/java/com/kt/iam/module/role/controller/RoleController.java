package com.kt.iam.module.role.controller;


import com.kt.component.dto.MultiResponse;
import com.kt.component.dto.PageResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.dto.SingleResponse;
import com.kt.component.validator.ValidateGroup;
import com.kt.component.web.base.BaseController;
import com.kt.iam.module.role.dto.RoleQueryDTO;
import com.kt.iam.module.role.dto.RoleUpdateDTO;
import com.kt.iam.module.role.service.IRoleService;
import com.kt.iam.module.role.vo.RoleBaseVO;
import com.kt.iam.module.role.vo.RoleListVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

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
public class RoleController extends BaseController {

    private final IRoleService iRoleService;

    public RoleController(IRoleService iRoleService) {
        this.iRoleService = iRoleService;
    }

    @PostMapping("/roles")
    public SingleResponse<PageResponse<RoleListVO>> list(@RequestBody RoleQueryDTO dto) {
        return SingleResponse.ok(PageResponse.build(iRoleService.pageList(dto)));
    }

    @GetMapping("/roles/all")
    public MultiResponse<RoleListVO> listAll() {
        return MultiResponse.ok(iRoleService.listAllVos());
    }

    @PostMapping("/role")
    public ServerResponse add(@RequestBody @Validated RoleUpdateDTO dto) {
        iRoleService.saveRole(dto);
        return ServerResponse.ok();
    }

    @PutMapping("/role")
    public ServerResponse update(@RequestBody @Validated RoleUpdateDTO dto) {
        iRoleService.updateRoleById(dto);
        return ServerResponse.ok();
    }

    @GetMapping("/role")
    public SingleResponse<RoleBaseVO> get(String id) {
        RoleBaseVO vo = iRoleService.getRoleVoById(id);
        return SingleResponse.ok(vo);
    }

    @DeleteMapping("/role")
    public ServerResponse removeRole(Long id) {
        iRoleService.removeRoleById(id);
        return ServerResponse.ok();
    }

    @PutMapping("/role/status")
    public ServerResponse updateStatus(@Validated({ValidateGroup.Update.class, Default.class})
                                       @RequestBody RoleUpdateDTO dto) {
        iRoleService.updateStatus(dto);
        return ServerResponse.ok();
    }
}

