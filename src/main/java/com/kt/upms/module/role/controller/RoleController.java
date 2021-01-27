package com.kt.upms.module.role.controller;


import com.kt.component.dto.MultiResponse;
import com.kt.component.dto.PageResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.dto.SingleResponse;
import com.kt.component.web.base.BaseController;
import com.kt.upms.module.role.dto.RoleQueryDTO;
import com.kt.upms.module.role.dto.RoleUpdateDTO;
import com.kt.upms.module.role.service.IRoleService;
import com.kt.upms.module.role.vo.RoleBaseVO;
import com.kt.upms.module.role.vo.RoleListVO;
import com.kt.upms.validgroup.UpmsValidateGroup;
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

    private final IRoleService iUpmsRoleService;

    public RoleController(IRoleService iUpmsRoleService) {
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

    @GetMapping("/role/")
    public SingleResponse<RoleBaseVO> get(String id) {
        RoleBaseVO vo = iUpmsRoleService.getRoleVoById(id);
        return SingleResponse.ok(vo);
    }

    @DeleteMapping("/role")
    public ServerResponse removeRole(Long id) {
        iUpmsRoleService.removeRoleById(id);
        return ServerResponse.ok();
    }

    @PutMapping("/role/status")
    public ServerResponse updateStatus(@Validated({UpmsValidateGroup.UpdateStatus.class, Default.class})
                                       @RequestBody RoleUpdateDTO dto) {
        iUpmsRoleService.updateStatus(dto);
        return ServerResponse.ok();
    }
}

