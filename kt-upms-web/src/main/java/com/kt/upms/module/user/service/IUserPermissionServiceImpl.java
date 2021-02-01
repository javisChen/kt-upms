package com.kt.upms.module.user.service;

import cn.hutool.core.collection.CollectionUtil;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    private boolean hasApiPermission(String applicationCode, Long userId, String url, String method) {
        List<ApiPermissionBO> apiPermissions = getUserApiPermissions(applicationCode, userId);
        return apiPermissions
                .stream()
                .anyMatch(item -> {
                    boolean methodEquals = item.getApiMethod().equalsIgnoreCase(method);
                    boolean urlEquals = antPathMatcher.match(item.getApiUrl(), url);
                    return methodEquals && urlEquals;
                });
    }


    @Override
    public boolean hasApiPermission(String applicationCode, String userCode, String url, String method) {
        UpmsUser user = iUserService.getUserByCode(userCode);
        return hasApiPermission(applicationCode, user, url, method);
    }

    @Override
    public AuthResponse checkPermission(AuthRequest request) {
        boolean checkResult = doCheck(request);
        if (!checkResult) {
            return AuthResponse.fail("No Permission");
        }

        boolean hasApiPermission = hasApiPermission(request.getApplicationCode(), request.getUserCode(),
                request.getUrl(), request.getMethod());
        if (hasApiPermission) {
            return AuthResponse.success();
        }
        return AuthResponse.fail("No Permission");
    }

    private boolean doCheck(AuthRequest request) {
        boolean result = false;
        boolean userInfo = StringUtils.isNotBlank(request.getUserCode()) || request.getUserId() != null;
        boolean url = StringUtils.isNotBlank(request.getUrl());
        boolean method = StringUtils.isNotBlank(request.getMethod());
        boolean applicationCode = StringUtils.isNotBlank(request.getApplicationCode());
        if (userInfo && url && method && applicationCode) {
            result = true;
        }
        return result;
    }

    private boolean hasApiPermission(String applicationCode, UpmsUser user, String url, String method) {
        if (isSuperAdmin(user.getCode())) {
            return true;
        }
        return hasApiPermission(applicationCode, user.getId(), url, method);
    }

    /**
     * 获取用户可访问的api并且需要授权认证的API
     */
    private List<ApiPermissionBO> getUserApiPermissions(String applicationCode, Long userId) {
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
