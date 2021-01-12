package com.kt.upms.api.controller;

import cn.hutool.extra.cglib.CglibUtil;
import com.kt.component.dto.PageRequest;
import com.kt.component.dto.PageResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.web.base.BaseController;
import com.kt.model.dto.usergroup.*;
import com.kt.model.validgroup.UpmsValidateGroup;
import com.kt.model.vo.usergroup.UserGroupListTreeVO;
import com.kt.model.vo.usergroup.UserGroupVO;
import com.kt.upms.entity.UpmsUserGroup;
import com.kt.upms.service.IUpmsUserGroupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.groups.Default;
import java.util.List;

/**
 * <p>
 * 用户组表 前端控制器
 * </p>
 */
@RestController
@RequestMapping
public class UpmsUserGroupController extends BaseController {

    private final IUpmsUserGroupService iUpmsUserGroupService;

    public UpmsUserGroupController(IUpmsUserGroupService iUpmsUserGroupService) {
        this.iUpmsUserGroupService = iUpmsUserGroupService;
    }

    @PostMapping("/usergroups")
    public ServerResponse<PageResponse<UserGroupListTreeVO>> list(@RequestBody UserGroupQueryDTO dto) {
        return ServerResponse.ok(iUpmsUserGroupService.pageList(dto));
    }

    @GetMapping("/usergroups/all")
    public ServerResponse<List<UserGroupVO>> list() {
        return ServerResponse.ok(iUpmsUserGroupService.listAllVos());
    }

    @PostMapping("/usergroups/tree")
    public ServerResponse getUserGroupsTree(@RequestBody UserGroupQueryDTO dto) {
        return ServerResponse.ok(iUpmsUserGroupService.getTree(dto));
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

    @PostMapping("/usergroup/user")
    public ServerResponse addUserToGroup(@Validated() @RequestBody UserGroupUserAddDTO dto) {
        iUpmsUserGroupService.addOrRemoveUserInUserGroup(dto);
        return ServerResponse.ok();
    }

    @PostMapping("/usergroup/users")
    public ServerResponse getUsersUnderUserGroup(@RequestBody PageRequest<UserGroupUserQueryDTO> dto) {
        PageResponse pageResponse = iUpmsUserGroupService.getUsersUnderUserGroupPageList(getPage(dto), dto.getParams());
        return ServerResponse.ok(pageResponse);
    }

    @PostMapping("/usergroup/role")
    public ServerResponse addRoleToGroup(@Validated() @RequestBody UserGroupRoleAddDTO dto) {
        iUpmsUserGroupService.addOrRemoveRoleInUserGroup(dto);
        return ServerResponse.ok();
    }

    @PostMapping("/usergroup/roles")
    public ServerResponse getRolesUnderUserGroup(@RequestBody PageRequest<UserGroupRoleQueryDTO> dto) {
        PageResponse pageResponse = iUpmsUserGroupService.getRolesUnderUserGroupPageList(getPage(dto), dto.getParams());
        return ServerResponse.ok(pageResponse);
    }

}

