package com.kt.upms.module.user.service;

import com.kt.upms.module.permission.persistence.UpmsPermission;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.user.vo.UserPermissionRouteNavVO;

import java.util.List;

public interface IUserPermissionService {

    /**
     * 获取用户所拥有的的权限
     *
     * @param userId              用户id
     * @param permissionTypeEnums 权限类型
     */
    List<UpmsPermission> getUserPermissions(Long userId, PermissionTypeEnums permissionTypeEnums);

    /**
     * 获取指定类型下的所有权限
     *
     * @param permissionTypeEnums 权限类型
     */
    List<UpmsPermission> getUserPermissions(PermissionTypeEnums permissionTypeEnums);

    /**
     * 获取用户路由权限（前端菜单展示）
     *
     * @param userId 用户id
     */
    List<UserPermissionRouteNavVO> getUserRoutes(long userId);

    /**
     * 获取用户路由权限（前端菜单展示）
     *
     * @param userCode 用户code
     */
    List<UserPermissionRouteNavVO> getUserRoutes(String userCode);

    /**
     * 获取用户页面元素权限
     *
     * @param userId 用户id
     */
    List<PermissionVO> getUserPermissionPageElements(long userId);


    /**
     * 获取用户页面元素权限
     *
     * @param userCode 用户code
     */
    List<PermissionVO> getUserPermissionPageElements(String userCode);

    /**
     * 判断是否超级管理员
     *
     * @param userCode 用户编码
     */
    boolean isSuperAdmin(String userCode);
}
