package com.kt.upms.module.user.service;

import cn.hutool.core.collection.CollectionUtil;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.entity.UpmsUser;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.module.permission.service.IPermissionService;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.role.service.IRoleService;
import com.kt.upms.module.route.service.IRouteService;
import com.kt.upms.module.user.converter.UserBeanConverter;
import com.kt.upms.module.user.vo.UserPermissionRouteNavVO;
import com.kt.upms.module.usergroup.service.IUserGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户权限相关
 */
@Service
@Slf4j
public class IUserPermissionServiceImpl implements IUserPermissionService {

    private final String superAdmin = "SuperAdmin";

    @Autowired
    private IPermissionService iPermissionService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IRoleService iRoleService;
    @Autowired
    private IUserGroupService iUserGroupService;
    @Autowired
    private UserBeanConverter beanConverter;
    @Autowired
    private IRouteService iRouteService;

    @Override
    public List<UpmsPermission> getUserPermissions(Long userId, PermissionTypeEnums permissionTypeEnums) {
        Set<Long> roleIdSet = getUserAllRoles(userId);
        return iPermissionService.getPermissionByRoleIds(roleIdSet, permissionTypeEnums);
    }

    @Override
    public List<UpmsPermission> getUserPermissions(PermissionTypeEnums permissionTypeEnums) {
        return iPermissionService.getAllPermissionsByType(permissionTypeEnums);
    }

    @Override
    public List<UserPermissionRouteNavVO> getUserRoutes(long userId) {
        List<UpmsPermission> userRoutePermissions = getUserPermissions(userId, PermissionTypeEnums.FRONT_ROUTE);
        return getUserRoutesByPermissionIds(userRoutePermissions);
    }

    @Override
    public List<UserPermissionRouteNavVO> getUserRoutes(String userCode) {
        List<UpmsPermission> permissions;
        // 超管直接赋予所有权限
        if (isSuperAdmin(userCode)) {
            permissions = iPermissionService.getAllPermissionsByType(PermissionTypeEnums.FRONT_ROUTE);
        } else {
            UpmsUser user = iUserService.getUserByCode(userCode);
            permissions = getUserPermissions(user.getId(), PermissionTypeEnums.FRONT_ROUTE);
        }
        return getUserRoutesByPermissionIds(permissions);
    }

    @Override
    public boolean isSuperAdmin(String userCode) {
        return superAdmin.equals(userCode);
    }

    @Override
    public List<PermissionVO> getUserPermissionPageElements(long userId) {
        List<UpmsPermission> permissions = getUserPermissions(userId, PermissionTypeEnums.PAGE_ELEMENT);
        return permissions.stream().map(beanConverter::convertToPermissionVO).collect(Collectors.toList());
    }

    @Override
    public List<PermissionVO> getUserPermissionPageElements(String userCode) {
        List<UpmsPermission> permissions;
        // 超管直接赋予所有权限
        if (isSuperAdmin(userCode)) {
            permissions = iPermissionService.getAllPermissionsByType(PermissionTypeEnums.PAGE_ELEMENT);
        } else {
            UpmsUser user = iUserService.getUserByCode(userCode);
            permissions = getUserPermissions(user.getId(), PermissionTypeEnums.PAGE_ELEMENT);
        }
        return permissions.stream().map(beanConverter::convertToPermissionVO).collect(Collectors.toList());
    }

    /**
     * 获取用户的所有角色
     */
    private Set<Long> getUserAllRoles(Long userId) {
        List<Long> roleIds = getRoleIdsByUserId(userId);
        List<Long> userGroupIds = getUserGroupIdsByUserId(userId);
        List<Long> userGroupsRoleIds = iRoleService.getRoleIdsByUserGroupIds(userGroupIds);
        log.debug("用户拥有的角色 --------> {}", roleIds);
        log.debug("用户所归属的用户组 --------> {}", userGroupIds);
        roleIds.addAll(userGroupsRoleIds);
        log.debug("用户组拥有的角色 --------> {}", roleIds);
        Set<Long> roleIdSet = new HashSet<>(roleIds);
        log.debug("角色交集 --------> {}", roleIdSet);
        return roleIdSet;
    }

    private List<Long> getUserGroupIdsByUserId(Long userId) {
        return iUserGroupService.getUserGroupIdsByUserId(userId);
    }

    private List<Long> getRoleIdsByUserId(Long userId) {
        return iRoleService.getRoleIdsByUserId(userId);
    }

    private List<UserPermissionRouteNavVO> getUserRoutesByPermissionIds(List<UpmsPermission> userRoutePermissions) {
        if (CollectionUtil.isEmpty(userRoutePermissions)) {
            return CollectionUtil.newArrayList();
        }
        List<Long> routeIds = userRoutePermissions.stream()
                .map(UpmsPermission::getResourceId).collect(Collectors.toList());
        return iRouteService.getRouteVOSByIds(routeIds);
    }
}
