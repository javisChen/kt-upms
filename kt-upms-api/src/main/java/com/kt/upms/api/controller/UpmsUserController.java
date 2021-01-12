package com.kt.upms.api.controller;

import com.kt.component.dto.PageResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.logger.CatchAndLog;
import com.kt.component.web.base.BaseController;
import com.kt.model.dto.user.UserAddDTO;
import com.kt.model.dto.user.UserQueryDTO;
import com.kt.model.dto.user.UserUpdateDTO;
import com.kt.model.vo.user.UserDetailVO;
import com.kt.model.vo.user.UserListVO;
import com.kt.upms.service.IUpmsRouteService;
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
@RequestMapping
@CatchAndLog
public class UpmsUserController extends BaseController {

    @Autowired
    private IUpmsUserService iUpmsUserService;
    @Autowired
    private IUpmsRouteService iUpmsMenuService;

    /**
     * 查看用户列表
     */
    @PostMapping("/users")
    public ServerResponse<PageResponse<UserListVO>> list(@RequestBody UserQueryDTO pageRequest) {
        return ServerResponse.ok(iUpmsUserService.pageList(pageRequest));
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
     * 查看用户详情
     */
    @GetMapping("/user")
    public ServerResponse<UserDetailVO> get(Long id) {
        return ServerResponse.ok(iUpmsUserService.getUserDetailVOById(id));
    }

    /**
     * 获取用户菜单
     */
    @GetMapping("/user/routes")
    public ServerResponse getUserRoutes() {
        return ServerResponse.ok(iUpmsMenuService.getUserRoutes());
    }

}

