package com.kt.upms.module.usergroup.controller;

import com.kt.component.dto.MultiResponse;
import com.kt.component.dto.PageResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.dto.SingleResponse;
import com.kt.component.web.base.BaseController;
import com.kt.upms.module.usergroup.dto.UserGroupAddDTO;
import com.kt.upms.module.usergroup.dto.UserGroupQueryDTO;
import com.kt.upms.module.usergroup.dto.UserGroupUpdateDTO;
import com.kt.upms.module.usergroup.service.IUserGroupService;
import com.kt.upms.module.usergroup.vo.UserGroupTreeVO;
import com.kt.upms.module.usergroup.vo.UserGroupListTreeVO;
import com.kt.upms.module.usergroup.vo.UserGroupBaseVO;
import com.kt.upms.validgroup.UpmsValidateGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

/**
 * <p>
 * 用户组表 前端控制器
 * </p>
 */
@RestController
@RequestMapping
public class UserGroupController extends BaseController {

    private final IUserGroupService iUserGroupService;

    public UserGroupController(IUserGroupService iUserGroupService) {
        this.iUserGroupService = iUserGroupService;
    }

    @PostMapping("/usergroups")
    public SingleResponse<PageResponse<UserGroupListTreeVO>> list(@RequestBody UserGroupQueryDTO dto) {
        return SingleResponse.ok(PageResponse.build(iUserGroupService.pageList(dto)));
    }

    @GetMapping("/usergroups/all")
    public MultiResponse<UserGroupBaseVO> list() {
        return MultiResponse.ok(iUserGroupService.listAllVos());
    }

    @PostMapping("/usergroups/tree")
    public MultiResponse<UserGroupTreeVO> getUserGroupsTree(@RequestBody UserGroupQueryDTO dto) {
        return MultiResponse.ok(iUserGroupService.getTree(dto));
    }

    @PostMapping("/usergroup")
    public ServerResponse add(@RequestBody @Validated UserGroupAddDTO userGroupAddDTO) {
        iUserGroupService.saveUserGroup(userGroupAddDTO);
        return ServerResponse.ok();
    }

    @PutMapping("/usergroup")
    public ServerResponse update(@RequestBody @Validated UserGroupUpdateDTO userGroupUpdateDTO) {
        iUserGroupService.updateUserGroupById(userGroupUpdateDTO);
        return ServerResponse.ok();
    }

//    @GetMapping("/usergroup/{id}")
//    public ServerResponse get(@PathVariable("id") String userGroupId) {
//        UpmsUserGroup upmsUserGroup = iUpmsUserGroupService.getById(userGroupId);
//        if (upmsUserGroup == null) {
//            return ServerResponse.ok();
//        }
//        UserGroupQueryDTO copy = CglibUtil.copy(upmsUserGroup, UserGroupQueryDTO.class);
//        return ServerResponse.ok(copy);
//    }

    @PutMapping("/usergroup/status")
    public ServerResponse updateStatus(@Validated({UpmsValidateGroup.UpdateStatus.class, Default.class})
                                       @RequestBody UserGroupUpdateDTO dto) {
        iUserGroupService.updateStatus(dto);
        return ServerResponse.ok();
    }

}

