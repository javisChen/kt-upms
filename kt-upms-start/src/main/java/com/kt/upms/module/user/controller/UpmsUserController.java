package com.kt.upms.module.user.controller;


import com.kt.component.dto.PageResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.dto.SingleResponse;
import com.kt.component.logger.CatchAndLog;
import com.kt.component.validator.ValidateGroup;
import com.kt.component.web.base.BaseController;
import com.kt.upms.module.route.dto.UserRoutesDTO;
import com.kt.upms.module.route.service.IUpmsRouteService;
import com.kt.upms.module.user.dto.UserAddDTO;
import com.kt.upms.module.user.dto.UserPageListSearchDTO;
import com.kt.upms.module.user.dto.UserUpdateDTO;
import com.kt.upms.module.user.service.IUpmsUserService;
import com.kt.upms.module.user.vo.UserDetailVO;
import com.kt.upms.module.user.vo.UserPageListVO;
import org.springframework.beans.factory.annotation.Autowired;
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
@CatchAndLog
public class UpmsUserController extends BaseController {

    @Autowired
    private IUpmsUserService iUpmsUserService;
    @Autowired
    private IUpmsRouteService iUpmsMenuService;

    /**
     * 查看用户列表
     * @return
     */
    @PostMapping("/users")
    public SingleResponse<PageResponse<UserPageListVO>> list(@RequestBody UserPageListSearchDTO pageRequest) {
        return SingleResponse.ok(PageResponse.build(iUpmsUserService.pageList(pageRequest)));
    }

    /**
     * 添加用户
     * @return
     */
    @PostMapping("/user")
    public ServerResponse add(@RequestBody @Validated UserAddDTO userAddDTO) {
        iUpmsUserService.saveUser(userAddDTO);
        return SingleResponse.ok();
    }

    /**
     * 编辑用户
     */
    @PutMapping("/user")
    public ServerResponse update(@RequestBody
                                 @Validated({ValidateGroup.Update.class, Default.class}) UserUpdateDTO userUpdateDTO) {
        iUpmsUserService.updateUserById(userUpdateDTO);
        return ServerResponse.ok();
    }

    /**
     * 查看用户详情
     */
    @GetMapping("/user")
    public SingleResponse<UserDetailVO> get(Long id) {
        return SingleResponse.ok(iUpmsUserService.getUserDetailVOById(id));
    }

    /**
     * 获取用户菜单
     */
    @GetMapping("/user/routes")
    public SingleResponse<UserRoutesDTO> getUserRoutes() {
        return SingleResponse.ok(iUpmsMenuService.getUserRoutes());
    }

}

