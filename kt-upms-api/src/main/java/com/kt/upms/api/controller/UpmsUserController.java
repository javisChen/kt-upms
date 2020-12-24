package com.kt.upms.api.controller;

import cn.hutool.extra.cglib.CglibUtil;
import com.kt.component.dto.PageRequest;
import com.kt.component.dto.ServerResponse;
import com.kt.component.web.base.BaseController;
import com.kt.model.dto.user.UserAddDTO;
import com.kt.model.dto.user.UserQueryDTO;
import com.kt.model.dto.user.UserUpdateDTO;
import com.kt.model.validgroup.UpmsValidateGroup;
import com.kt.upms.entity.UpmsUser;
import com.kt.upms.service.IUpmsRouteService;
import com.kt.upms.service.IUpmsUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Javis
 * @since 2020-11-09
 */
@RestController
@RequestMapping
public class UpmsUserController extends BaseController {

    private final IUpmsUserService iUpmsUserService;
    private final IUpmsRouteService iUpmsMenuService;

    public UpmsUserController(IUpmsUserService iUpmsUserService, IUpmsRouteService iUpmsMenuService) {
        this.iUpmsUserService = iUpmsUserService;
        this.iUpmsMenuService = iUpmsMenuService;
    }

    /**
     * 查看用户列表
     */
    @PostMapping("/users")
    public ServerResponse list(@RequestBody PageRequest<UserQueryDTO> pageRequest) {
        return ServerResponse.ok(iUpmsUserService.pageList(getPage(pageRequest), pageRequest.getParams()));
    }

    /**
     * 添加用户
     */
    @PostMapping("/user")
    public ServerResponse add(@RequestBody @Validated UserAddDTO userAddDTO) {
        iUpmsUserService.saveUser(userAddDTO);
        return ServerResponse.ok();
    }

    /**
     * 编辑用户
     */
    @PutMapping("/user")
    public ServerResponse update(@RequestBody @Validated UserUpdateDTO userUpdateDTO) {
        iUpmsUserService.updateUserById(userUpdateDTO);
        return ServerResponse.ok();
    }

    /**
     * 修改用户状态
     */
    @PutMapping("/user/status")
    public ServerResponse updateStatus(@Validated({UpmsValidateGroup.UpdateStatus.class, Default.class})
                                       @RequestBody UserUpdateDTO userUpdateDTO) {
        iUpmsUserService.updateStatus(userUpdateDTO);
        return ServerResponse.ok();
    }

    /**
     * 查看用户详情
     */
    @GetMapping("/user/{id}")
    public ServerResponse get(@PathVariable("id") String userId) {
        UpmsUser upmsUser = iUpmsUserService.getById(userId);
        if (upmsUser == null) {
            return ServerResponse.ok();
        }
        upmsUser.setPassword(null);
        return ServerResponse.ok(CglibUtil.copy(upmsUser, UserUpdateDTO.class));
    }

    /**
     * 获取用户菜单
     */
    @GetMapping("/user/routes")
    public ServerResponse getUserRoutes() {
        return ServerResponse.ok(iUpmsMenuService.getAllRoutes());
    }

}

