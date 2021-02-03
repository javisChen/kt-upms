package com.kt.upms.module.user.service;

import cn.hutool.core.collection.CollectionUtil;
import com.kt.upms.auth.core.check.AuthCheck;
import com.kt.upms.auth.core.model.AuthRequest;
import com.kt.upms.auth.core.model.AuthResponse;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.module.permission.bo.ApiPermissionBO;
import com.kt.upms.module.permission.persistence.UpmsPermission;
import com.kt.upms.module.permission.service.IPermissionService;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.role.service.IRoleService;
import com.kt.upms.module.route.service.IRouteService;
import com.kt.upms.module.user.common.UserConst;
import com.kt.upms.module.user.converter.UserBeanConverter;
import com.kt.upms.module.user.persistence.UpmsUser;
import com.kt.upms.module.user.vo.UserPermissionRouteNavVO;
import com.kt.upms.module.usergroup.service.IUserGroupService;
import com.kt.upms.security.access.ApiAccessChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

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
    @Autowired
    private ApiAccessChecker apiAccessChecker;
    @Autowired
    private AuthCheck remoteAuthCheck;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

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
        return UserConst.SUPER_ADMIN.equals(userCode);
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
        roleIds.addAll(userGroupsRoleIds);
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

    @Override
    public boolean checkHasApiPermission(String applicationCode, String userCode, String url, String method) {
        UpmsUser user = iUserService.getUserByCode(userCode);
        if (isSuperAdmin(user.getCode())) {
            return true;
        }
        Long userId = user.getId();
        Set<ApiPermissionBO> apiPermissions = getUserApiPermissions(applicationCode, userId);
        return apiPermissions.stream().anyMatch(item -> matchApi(url, method, item));
    }

    private boolean matchApi(String url, String method, ApiPermissionBO item) {
        boolean methodEquals = item.getApiMethod().equalsIgnoreCase(method);
        boolean urlEquals = antPathMatcher.match(item.getApiUrl(), url);
        return methodEquals && urlEquals;
    }

    @Override
    public AuthResponse checkApiPermission(AuthRequest request) {
        boolean hasApiPermission = checkHasApiPermission(request.getApplicationCode(), request.getUserCode(),
                request.getRequestUri(), request.getMethod());
        if (hasApiPermission) {
            return AuthResponse.success();
        }
        return AuthResponse.fail("No Permission");
    }

    @Override
    public AuthResponse accessCheck(AuthRequest request) {
        return remoteAuthCheck.checkPermission(request);
    }

    /**
     * 获取用户可访问的api并且需要授权认证的API
     */
    private Set<ApiPermissionBO> getUserApiPermissions(String applicationCode, Long userId) {
        Set<Long> roleIdSet = getUserAllRoles(userId);
        return iPermissionService.getApiPermissionByRoleIdsAndApplicationCode(applicationCode, roleIdSet);
    }

    private List<ApiPermissionBO> getApiPermissionByPermissionIds(List<Long> permissionIds) {
        if (CollectionUtil.isEmpty(permissionIds)) {
            return CollectionUtil.newArrayList();
        }
        return iPermissionService.getApiPermissionByIds(permissionIds);
    }
}
