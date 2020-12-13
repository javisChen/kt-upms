package com.kt.upms.api.controller;

import cn.hutool.extra.cglib.CglibUtil;
import com.kt.component.dto.PageRequest;
import com.kt.component.dto.ServerResponse;
import com.kt.model.dto.UserAddDTO;
import com.kt.model.dto.UserQueryDTO;
import com.kt.model.dto.UserUpdateDTO;
import com.kt.upms.entity.UpmsUser;
import com.kt.upms.service.IUpmsUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Javis
 * @since 2020-11-09
 */
@RestController
@RequestMapping("/user")
public class UpmsUserController extends BaseController {

    private final IUpmsUserService iUpmsUserService;

    public UpmsUserController(IUpmsUserService iUpmsUserService) {
        this.iUpmsUserService = iUpmsUserService;
    }

    /**
     * 查看用户列表
     */
    @PostMapping("/list")
    public ServerResponse list(@RequestBody PageRequest<UserQueryDTO> pageRequest) {
        return ServerResponse.ok(iUpmsUserService.pageList(getPage(pageRequest), pageRequest.getParams()));
    }

    /**
     * 添加用户
     */
    @PostMapping("/add")
    public ServerResponse add(@RequestBody @Validated UserAddDTO userAddDTO) {
        return ServerResponse.ok(iUpmsUserService.save(userAddDTO));
    }

    /**
     * 编辑用户
     */
    @PostMapping("/update")
    public ServerResponse update(@RequestBody @Validated UserUpdateDTO userUpdateDTO) {
        iUpmsUserService.updateUserById(userUpdateDTO);
        return ServerResponse.ok();
    }

    /**
     * 禁用用户
     */
    @PostMapping("/disable")
    public ServerResponse disable(@RequestBody @Validated UserUpdateDTO userUpdateDTO) {
        iUpmsUserService.disableUser(userUpdateDTO);
        return ServerResponse.ok();
    }

    /**
     * 启用用户
     */
    @PostMapping("/enable")
    public ServerResponse enable(@RequestBody @Validated UserUpdateDTO userUpdateDTO) {
        iUpmsUserService.enableUser(userUpdateDTO);
        return ServerResponse.ok();
    }

    /**
     * 查看用户详情
     */
    @GetMapping("/{id}")
    public ServerResponse get(@PathVariable("id") String userId) {
        UpmsUser upmsUser = iUpmsUserService.getById(userId);
        upmsUser.setPassword(null);
        return ServerResponse.ok(CglibUtil.copy(upmsUser, UserUpdateDTO.class));
    }

}

