package com.kt.iam.module.user.controller;


import com.kt.component.dto.MultiResponse;
import com.kt.component.dto.PageResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.dto.SingleResponse;
import com.kt.component.logger.CatchAndLog;
import com.kt.component.validator.ValidateGroup;
import com.kt.component.web.base.BaseController;
import com.kt.iam.auth.core.context.LoginUserContextHolder;
import com.kt.iam.auth.core.model.AuthRequest;
import com.kt.iam.auth.core.model.AuthResponse;
import com.kt.iam.auth.core.model.LoginUserContext;
import com.kt.iam.module.permission.vo.PermissionVO;
import com.kt.iam.module.user.dto.UserPageListSearchDTO;
import com.kt.iam.module.user.dto.UserUpdateDTO;
import com.kt.iam.module.user.service.IUserPermissionService;
import com.kt.iam.module.user.service.IUserService;
import com.kt.iam.module.user.vo.UserDetailVO;
import com.kt.iam.module.user.vo.UserPageListVO;
import com.kt.iam.module.user.vo.UserPermissionRouteNavVO;
import org.springframework.beans.factory.annotation.Autowired;
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
    private IUserPermissionService iUserPermissionService;

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
     */
    @PostMapping("/user")
    public ServerResponse add(@RequestBody @Validated({ValidateGroup.Add.class, Default.class}) UserUpdateDTO dto) {
        iUserService.saveUser(dto);
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
     * 查看用户详情
     */
    @DeleteMapping("/user")
    public ServerResponse delete(Long id) {
        iUserService.removeUserById(id);
        return SingleResponse.ok();
    }

    /**
     * 查看用户基本信息
     */
    @GetMapping("/user/info")
    public SingleResponse<LoginUserContext> getLoginUserInfo() {
        return SingleResponse.ok(LoginUserContextHolder.getContext());
    }

    /**
     * 获取用户菜单权限
     */
    @GetMapping("/user/permission/routes")
    public MultiResponse<UserPermissionRouteNavVO> getUserRoutePermission() {
        String userCode = LoginUserContextHolder.getContext().getUserCode();
        List<UserPermissionRouteNavVO> userRoutes = iUserPermissionService.getUserRoutes(userCode);
        return MultiResponse.ok(userRoutes);
    }

    /**
     * 获取用户页面元素权限
     */
    @GetMapping("/user/permission/elements")
    public MultiResponse<PermissionVO> getUserElementPermission() {
        String userCode = LoginUserContextHolder.getContext().getUserCode();
        List<PermissionVO> userRoutes = iUserPermissionService.getUserPermissionPageElements(userCode);
        return MultiResponse.ok(userRoutes);
    }

    /**
     * 用户权限校验
     */
    @PostMapping("/user/permission/check")
    public AuthResponse checkPermission(@RequestBody AuthRequest request) {
        return iUserPermissionService.accessCheck(request);
    }

}

