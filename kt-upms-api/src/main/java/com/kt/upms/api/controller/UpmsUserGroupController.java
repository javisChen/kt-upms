package com.kt.upms.api.controller;


import cn.hutool.extra.cglib.CglibUtil;
import com.kt.component.dto.PageRequest;
import com.kt.component.dto.ServerResponse;
import com.kt.component.web.base.BaseController;
import com.kt.model.dto.usergroup.UserGroupAddDTO;
import com.kt.model.dto.usergroup.UserGroupQueryDTO;
import com.kt.model.dto.usergroup.UserGroupUpdateDTO;
import com.kt.upms.entity.UpmsUserGroup;
import com.kt.upms.service.IUpmsUserGroupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 用户组表 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@RestController
@RequestMapping("/user-group")
public class UpmsUserGroupController extends BaseController {

    private final IUpmsUserGroupService iUpmsUserGroupService;

    public UpmsUserGroupController(IUpmsUserGroupService iUpmsUserGroupService) {
        this.iUpmsUserGroupService = iUpmsUserGroupService;
    }

    @PostMapping("/list")
    public ServerResponse list(@RequestBody PageRequest<UserGroupQueryDTO> pageRequest) {
        return ServerResponse.ok(iUpmsUserGroupService.pageList(getPage(pageRequest), pageRequest.getParams()));
    }

    @PostMapping("/add")
    public ServerResponse add(@RequestBody @Validated UserGroupAddDTO userGroupAddDTO) {
        return ServerResponse.ok(iUpmsUserGroupService.saveUserGroup(userGroupAddDTO));
    }

    @PostMapping("/update")
    public ServerResponse update(@RequestBody @Validated UserGroupUpdateDTO userGroupUpdateDTO) {
        iUpmsUserGroupService.updateUserGroupById(userGroupUpdateDTO);
        return ServerResponse.ok();
    }

    @GetMapping("/{id}")
    public ServerResponse get(@PathVariable("id") String userGroupId) {
        UpmsUserGroup upmsUserGroup = iUpmsUserGroupService.getById(userGroupId);
        if (upmsUserGroup == null) {
            return ServerResponse.ok();
        }
        return ServerResponse.ok(CglibUtil.copy(upmsUserGroup, UserGroupQueryDTO.class));
    }

    @PostMapping("/disable")
    public ServerResponse disable(@RequestBody @Validated UserGroupUpdateDTO dto) {
        iUpmsUserGroupService.disableUserGroup(dto);
        return ServerResponse.ok();
    }

    @PostMapping("/enable")
    public ServerResponse enable(@RequestBody @Validated UserGroupUpdateDTO dto) {
        iUpmsUserGroupService.enableUserGroup(dto);
        return ServerResponse.ok();
    }
}

