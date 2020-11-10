package com.kt.upms.controller;


import com.kt.model.http.ServerResponse;
import com.kt.upms.entity.UpmsUser;
import com.kt.upms.service.IUpmsUserService;
import com.kt.web.base.BaseController;
import com.kt.web.validation.ValidateGroup;
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
    private IUpmsUserService iUpmsUserService;

    @PostMapping("/add")
    public ServerResponse add(@RequestBody @Validated(ValidateGroup.Add.class) UpmsUser upmsUser) {
        iUpmsUserService.save(upmsUser);
        upmsUser.setPassword(null);
        return ServerResponse.ok(upmsUser);
    }

    @PostMapping("/update")
    public ServerResponse update(@RequestBody @Validated(ValidateGroup.Update.class) UpmsUser upmsUser) {
        iUpmsUserService.updateById(upmsUser);
        return ServerResponse.ok();
    }

    @PostMapping("/delete/{id}")
    public ServerResponse delete(@PathVariable("id") String id) {
        boolean b = iUpmsUserService.removeById(id);
        return ServerResponse.ok(b);
    }

    @PostMapping("/{id}")
    public ServerResponse get(@PathVariable("id") String userId) {
        UpmsUser upmsUser = iUpmsUserService.getById(userId);
        upmsUser.setPassword(null);
        return ServerResponse.ok(upmsUser);
    }

}

