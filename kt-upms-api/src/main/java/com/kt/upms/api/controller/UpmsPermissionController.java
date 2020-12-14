package com.kt.upms.api.controller;


import cn.hutool.extra.cglib.CglibUtil;
import com.kt.component.dto.PageRequest;
import com.kt.component.dto.ServerResponse;
import com.kt.component.web.base.BaseController;
import com.kt.model.dto.permission.PermissionAddDTO;
import com.kt.model.dto.permission.PermissionQueryDTO;
import com.kt.model.dto.permission.PermissionUpdateDTO;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.service.IUpmsPermissionService;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/permission")
public class UpmsPermissionController extends BaseController {

    private final IUpmsPermissionService iUpmsPermissionService;

    public UpmsPermissionController(IUpmsPermissionService iUpmsPermissionService) {
        this.iUpmsPermissionService = iUpmsPermissionService;
    }

    @PostMapping("/list")
    public ServerResponse list(@RequestBody PageRequest<PermissionQueryDTO> pageRequest) {
        return ServerResponse.ok(iUpmsPermissionService.pageList(getPage(pageRequest), pageRequest.getParams()));
    }

    @PostMapping("/add")
    public ServerResponse add(@RequestBody @Validated PermissionAddDTO dto) {
        return ServerResponse.ok(iUpmsPermissionService.savePermission(dto));
    }

    @PostMapping("/update")
    public ServerResponse update(@RequestBody @Validated PermissionUpdateDTO dto) {
        iUpmsPermissionService.updatePermissionById(dto);
        return ServerResponse.ok();
    }

    @GetMapping("/{id}")
    public ServerResponse get(@PathVariable("id") String id) {
        UpmsPermission upmsPermission = iUpmsPermissionService.getById(id);
        if (upmsPermission == null) {
            return ServerResponse.ok();
        }
        return ServerResponse.ok(CglibUtil.copy(upmsPermission, PermissionQueryDTO.class));
    }

    @PostMapping("/disable")
    public ServerResponse disable(@RequestBody @Validated PermissionUpdateDTO dto) {
        iUpmsPermissionService.disablePermission(dto);
        return ServerResponse.ok();
    }

    @PostMapping("/enable")
    public ServerResponse enable(@RequestBody @Validated PermissionUpdateDTO dto) {
        iUpmsPermissionService.enablePermission(dto);
        return ServerResponse.ok();
    }
}

