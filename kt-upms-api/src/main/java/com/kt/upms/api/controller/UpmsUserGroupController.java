package com.kt.upms.api.controller;


import cn.hutool.extra.cglib.CglibUtil;
import com.kt.component.dto.PageRequest;
import com.kt.component.dto.ServerResponse;
import com.kt.component.web.base.BaseController;
import com.kt.model.dto.usergroup.UserGroupAddDTO;
import com.kt.model.dto.usergroup.UserGroupQueryDTO;
import com.kt.model.dto.usergroup.UserGroupUpdateDTO;
import com.kt.model.validgroup.UpmsValidateGroup;
import com.kt.upms.entity.UpmsUserGroup;
import com.kt.upms.service.IUpmsUserGroupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;


/**
 * <p>
 * 用户组表 前端控制器
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
@RestController
@RequestMapping
public class UpmsUserGroupController extends BaseController {

    private final IUpmsUserGroupService iUpmsUserGroupService;

    public UpmsUserGroupController(IUpmsUserGroupService iUpmsUserGroupService) {
        this.iUpmsUserGroupService = iUpmsUserGroupService;
    }

    @PostMapping("/usergroups")
    public ServerResponse list(@RequestBody PageRequest<UserGroupQueryDTO> pageRequest) {
        return ServerResponse.ok(iUpmsUserGroupService.pageList(getPage(pageRequest), pageRequest.getParams()));
    }

    @PostMapping("/usergroup")
    public ServerResponse add(@RequestBody @Validated UserGroupAddDTO userGroupAddDTO) {
        return ServerResponse.ok(iUpmsUserGroupService.saveUserGroup(userGroupAddDTO));
    }

    @PutMapping("/usergroup")
    public ServerResponse update(@RequestBody @Validated UserGroupUpdateDTO userGroupUpdateDTO) {
        iUpmsUserGroupService.updateUserGroupById(userGroupUpdateDTO);
        return ServerResponse.ok();
    }

    @GetMapping("/usergroup/{id}")
    public ServerResponse get(@PathVariable("id") String userGroupId) {
        UpmsUserGroup upmsUserGroup = iUpmsUserGroupService.getById(userGroupId);
        if (upmsUserGroup == null) {
            return ServerResponse.ok();
        }
        return ServerResponse.ok(CglibUtil.copy(upmsUserGroup, UserGroupQueryDTO.class));
    }

    @PutMapping("/usergroup/status")
    public ServerResponse updateStatus(@Validated({UpmsValidateGroup.UpdateStatus.class, Default.class})
                                       @RequestBody UserGroupUpdateDTO dto) {
        iUpmsUserGroupService.updateStatus(dto);
        return ServerResponse.ok();
    }

}

