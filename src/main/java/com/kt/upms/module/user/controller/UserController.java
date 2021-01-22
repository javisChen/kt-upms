package com.kt.upms.module.user.controller;


import com.kt.component.dto.MultiResponse;
import com.kt.component.dto.PageResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.dto.SingleResponse;
import com.kt.component.logger.CatchAndLog;
import com.kt.component.validator.ValidateGroup;
import com.kt.component.web.base.BaseController;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.route.service.IRouteService;
import com.kt.upms.module.user.dto.UserAddDTO;
import com.kt.upms.module.user.dto.UserPageListSearchDTO;
import com.kt.upms.module.user.dto.UserUpdateDTO;
import com.kt.upms.module.user.service.IUserService;
import com.kt.upms.module.user.vo.UserDetailVO;
import com.kt.upms.module.user.vo.UserPageListVO;
import com.kt.upms.module.user.vo.UserRouteVO;
import com.kt.upms.security.login.LoginUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

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
public class UserController extends BaseController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IRouteService iUpmsMenuService;

    /**
     * 查看用户列表
     *
     * @return
     */
    @PostMapping("/users")
    public SingleResponse<PageResponse<UserPageListVO>> list(@RequestBody UserPageListSearchDTO pageRequest) {
        return SingleResponse.ok(PageResponse.build(iUserService.pageList(pageRequest)));
    }

    /**
     * 添加用户
     *
     * @return
     */
    @PostMapping("/user")
    public ServerResponse add(@RequestBody @Validated UserAddDTO userAddDTO) {
        iUserService.saveUser(userAddDTO);
        return SingleResponse.ok();
    }

    /**
     * 编辑用户
     */
    @PutMapping("/user")
    public ServerResponse update(@RequestBody
                                 @Validated({ValidateGroup.Update.class, Default.class}) UserUpdateDTO userUpdateDTO) {
        iUserService.updateUserById(userUpdateDTO);
        return ServerResponse.ok();
    }
    /**
     * 查看用户详情
     */
    @GetMapping("/user")
    public SingleResponse<UserDetailVO> get(Long id) {
        return SingleResponse.ok(iUserService.getUserDetailVOById(id));
    }

    /**
     * 获取用户菜单权限
     */
    @GetMapping("/user/permission/routes")
    public MultiResponse<UserRouteVO> getUserRoutePermission() {
        Authentication context = SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails details = (LoginUserDetails) context.getDetails();
        List<UserRouteVO> userRoutes = iUserService.getUserRoutes(details.getUserId());
        return MultiResponse.ok(userRoutes);
    }

    /**
     * 获取用户页面元素权限
     */
    @GetMapping("/user/permission/elements")
    public MultiResponse<PermissionVO> getUserElementPermission() {
        Authentication context = SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails details = (LoginUserDetails) context.getDetails();
        List<PermissionVO> userRoutes = iUserService.getUserElements(details.getUserId());
        return MultiResponse.ok(userRoutes);
    }

}

