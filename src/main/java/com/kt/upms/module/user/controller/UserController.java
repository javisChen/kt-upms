package com.kt.upms.module.user.controller;


import com.kt.component.dto.MultiResponse;
import com.kt.component.dto.PageResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.dto.SingleResponse;
import com.kt.component.logger.CatchAndLog;
import com.kt.component.validator.ValidateGroup;
import com.kt.component.web.base.BaseController;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.user.dto.UserPageListSearchDTO;
import com.kt.upms.module.user.dto.UserUpdateDTO;
import com.kt.upms.module.user.service.IUserPermissionService;
import com.kt.upms.module.user.service.IUserService;
import com.kt.upms.module.user.vo.UserDetailVO;
import com.kt.upms.module.user.vo.UserPageListVO;
import com.kt.upms.module.user.vo.UserPermissionRouteNavVO;
import com.kt.upms.security.login.LoginUserDetails;
import com.kt.upms.security.token.UserTokenAuthenticationToken;
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
    public ServerResponse add(@RequestBody @Validated({ValidateGroup.Add.class}) UserUpdateDTO dto) {
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
    public SingleResponse<LoginUserDetails> getLoginUserInfo() {
        UserTokenAuthenticationToken token = (UserTokenAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return SingleResponse.ok(token.getDetails());
    }

    /**
     * 获取用户菜单权限
     */
    @GetMapping("/user/permission/routes")
    public MultiResponse<UserPermissionRouteNavVO> getUserRoutePermission() {
        Authentication context = SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails details = (LoginUserDetails) context.getDetails();
        List<UserPermissionRouteNavVO> userRoutes = iUserPermissionService.getUserRoutes(details.getUserCode());
        return MultiResponse.ok(userRoutes);
    }

    /**
     * 获取用户页面元素权限
     */
    @GetMapping("/user/permission/elements")
    public MultiResponse<PermissionVO> getUserElementPermission() {
        Authentication context = SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails details = (LoginUserDetails) context.getDetails();
        List<PermissionVO> userRoutes = iUserPermissionService.getUserPermissionPageElements(details.getUserCode());
        return MultiResponse.ok(userRoutes);
    }

}

