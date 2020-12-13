package com.kt.upms.api.controller;

import cn.hutool.extra.cglib.CglibUtil;
import com.kt.component.dto.PageRequest;
import com.kt.component.dto.ServerResponse;
import com.kt.model.dto.UserAddDTO;
import com.kt.model.dto.UserQueryDTO;
import com.kt.model.dto.UserUpdateDTO;
import com.kt.upms.entity.UpmsUser;
import com.kt.upms.service.IUpmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IUpmsUserService iUpmsUserService;

    @PostMapping("/list")
    public ServerResponse list(@RequestBody PageRequest<UserQueryDTO> pageRequest) {
        return ServerResponse.ok(iUpmsUserService.pageList(getPage(pageRequest), pageRequest.getParams()));
    }

    @PostMapping("/add")
    public ServerResponse add(@RequestBody @Validated UserAddDTO userAddDTO) {
        return ServerResponse.ok(iUpmsUserService.save(userAddDTO));
    }

    @PostMapping("/update")
    public ServerResponse update(@RequestBody @Validated UserUpdateDTO userUpdateDTO) {
        iUpmsUserService.updateUser(userUpdateDTO);
        return ServerResponse.ok();
    }

    @PostMapping("/disable/{id}")
    public ServerResponse disable(@PathVariable("id") String id) {
        iUpmsUserService.removeById(id);
        return ServerResponse.ok();
    }

    @PostMapping("/{id}")
    public ServerResponse get(@PathVariable("id") String userId) {
        UpmsUser upmsUser = iUpmsUserService.getById(userId);
        upmsUser.setPassword(null);
        CglibUtil.copy(upmsUser, UserUpdateDTO.class);
        return ServerResponse.ok(upmsUser);
    }

}

