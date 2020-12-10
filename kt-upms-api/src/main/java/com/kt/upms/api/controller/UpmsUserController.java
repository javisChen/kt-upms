package com.kt.upms.api.controller;

import com.kt.dto.ServerResponse;
import com.kt.upms.entity.UpmsUser;
import com.kt.upms.service.IUpmsUserService;
import com.kt.validator.ValidateGroup;
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
@RequestMapping("/upms-user")
public class UpmsUserController extends BaseController {

    @Autowired
    private IUpmsUserService upmsUserServiceImpl;

    @PostMapping("/add")
    public ServerResponse add(@Validated(ValidateGroup.Add.class) @RequestBody UpmsUser upmsUser) {
        return ServerResponse.ok(upmsUserServiceImpl.saveAndReturn(upmsUser));
    }

    @PostMapping("/update")
    public ServerResponse update(@RequestBody @Validated(ValidateGroup.Update.class) UpmsUser upmsUser) {
        upmsUserServiceImpl.updateUser(upmsUser);
        return ServerResponse.ok();
    }

    @PostMapping("/delete/{id}")
    public ServerResponse delete(@PathVariable("id") String id) {
        boolean b = upmsUserServiceImpl.removeById(id);
        return ServerResponse.ok(b);
    }

    @PostMapping("/{id}")
    public ServerResponse get(@PathVariable("id") String userId) {
        UpmsUser upmsUser = upmsUserServiceImpl.getById(userId);
        upmsUser.setPassword(null);
        return ServerResponse.ok(upmsUser);
    }

}

