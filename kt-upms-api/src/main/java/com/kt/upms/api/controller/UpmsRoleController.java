package com.kt.upms.api.controller;


import cn.hutool.extra.cglib.CglibUtil;
import com.kt.component.dto.PageRequest;
import com.kt.component.dto.ServerResponse;
import com.kt.model.dto.role.RoleAddDTO;
import com.kt.model.dto.role.RoleQueryDTO;
import com.kt.model.dto.role.RoleUpdateDTO;
import com.kt.upms.entity.UpmsRole;
import com.kt.upms.service.IUpmsRoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@RestController
@RequestMapping("/role")
public class UpmsRoleController extends BaseController {


    private final IUpmsRoleService iUpmsRoleService;

    public UpmsRoleController(IUpmsRoleService iUpmsRoleService) {
        this.iUpmsRoleService = iUpmsRoleService;
    }


    @PostMapping("/list")
    public ServerResponse list(@RequestBody PageRequest<RoleQueryDTO> pageRequest) {
        return ServerResponse.ok(iUpmsRoleService.pageList(getPage(pageRequest), pageRequest.getParams()));
    }

    @PostMapping("/add")
    public ServerResponse add(@RequestBody @Validated RoleAddDTO dto) {
        return ServerResponse.ok(iUpmsRoleService.saveRole(dto));
    }

    @PostMapping("/update")
    public ServerResponse update(@RequestBody @Validated RoleUpdateDTO dto) {
        iUpmsRoleService.updateRoleById(dto);
        return ServerResponse.ok();
    }

    @GetMapping("/{id}")
    public ServerResponse get(@PathVariable("id") String id) {
        UpmsRole upmsRole = iUpmsRoleService.getById(id);
        if (upmsRole == null) {
            return ServerResponse.ok();
        }
        return ServerResponse.ok(CglibUtil.copy(upmsRole, RoleQueryDTO.class));
    }
}

