package com.kt.upms.module.route.converter;

import com.kt.upms.entity.UpmsPageElement;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.entity.UpmsRoute;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.module.application.service.IApplicationService;
import com.kt.upms.module.permission.service.IPermissionService;
import com.kt.upms.module.route.dto.RouteUpdateDTO;
import com.kt.upms.module.route.service.IUpmsPageElementService;
import com.kt.upms.module.route.service.IRouteService;
import com.kt.upms.module.route.vo.RouteDetailVO;
import com.kt.upms.module.route.vo.RouteElementVO;
import com.kt.upms.module.route.vo.RouteListTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class RouteBeanConverter {

    @Autowired
    private IPermissionService iPermissionService;

    @Autowired
    private IApplicationService iApplicationService;

    @Autowired
    private IUpmsPageElementService iUpmsPageElementService;

    @Autowired
    private IRouteService iRouteService;

    public RouteListTreeVO assembleRouteListTreeVO(UpmsRoute route) {
        UpmsPermission permission = iPermissionService.getPermissionByResourceIdAndType(route.getId(),
                PermissionTypeEnums.FRONT_ROUTE);
        Long applicationId = route.getApplicationId();
        String applicationName = getApplicationName(applicationId);
        RouteListTreeVO treeNode = new RouteListTreeVO();
        treeNode.setLevel(route.getLevel());
        treeNode.setPermissionCode(permission.getCode());
        treeNode.setPermissionId(permission.getId());
        treeNode.setCode(route.getCode());
        treeNode.setName(route.getName());
        treeNode.setIcon(route.getIcon());
        treeNode.setChildren(new ArrayList<>());
        treeNode.setId(route.getId());
        treeNode.setPid(route.getPid());
        treeNode.setComponent(route.getComponent());
        treeNode.setPath(route.getPath());
        treeNode.setSequence(route.getSequence());
        treeNode.setStatus(route.getStatus());
        treeNode.setLevelPath(route.getLevelPath());
        treeNode.setType(route.getType());
        treeNode.setApplicationId(applicationId);
        treeNode.setApplicationName(applicationName);
        treeNode.setHideChildren(route.getHideChildren());
        treeNode.setCreateTime(route.getGmtCreate());
        treeNode.setUpdateTime(route.getGmtModified());
        return treeNode;
    }

    public UpmsRoute convertForInsert(RouteUpdateDTO dto) {
        UpmsRoute upmsRoute = new UpmsRoute();
        upmsRoute.setName(dto.getName());
        upmsRoute.setCode(dto.getCode());
        upmsRoute.setComponent(dto.getComponent());
        upmsRoute.setType(dto.getType());
        upmsRoute.setHideChildren(dto.getHideChildren());
        upmsRoute.setPid(dto.getPid());
        upmsRoute.setSequence(dto.getSequence());
        upmsRoute.setPath(dto.getPath());
        upmsRoute.setIcon(dto.getIcon());
        upmsRoute.setStatus(dto.getStatus());
        upmsRoute.setId(dto.getId());
        upmsRoute.setApplicationId(dto.getApplicationId());
        return upmsRoute;
    }

    public RouteElementVO convertForRouteElementVO(UpmsPageElement obj) {
        UpmsPermission permission = iPermissionService.getPermission(obj.getId(), PermissionTypeEnums.PAGE_ELEMENT);
        RouteElementVO vo = new RouteElementVO();
        vo.setId(obj.getId());
        vo.setRouteId(obj.getRouteId());
        vo.setName(obj.getName());
        vo.setPermissionId(permission.getId());
        vo.setPermissionCode(permission.getCode());
        vo.setType(obj.getType());
        return vo;
    }

    public UpmsRoute convertForUpdate(RouteUpdateDTO dto) {
        UpmsRoute upmsRoute = new UpmsRoute();
        upmsRoute.setId(dto.getId());
        upmsRoute.setName(dto.getName());
        upmsRoute.setCode(dto.getCode());
        upmsRoute.setComponent(dto.getComponent());
        upmsRoute.setType(dto.getType());
        upmsRoute.setHideChildren(dto.getHideChildren());
        upmsRoute.setSequence(dto.getSequence());
        upmsRoute.setPath(dto.getPath());
        upmsRoute.setIcon(dto.getIcon());
        upmsRoute.setStatus(dto.getStatus());
        upmsRoute.setApplicationId(dto.getApplicationId());
        return upmsRoute;
    }

    public RouteDetailVO convertToRouteDetailVO(Long id, UpmsRoute route) {
        Long applicationId = route.getApplicationId();
        String applicationName = getApplicationName(applicationId);
        RouteDetailVO vo = new RouteDetailVO();
        vo.setParentRouteName(iRouteService.getRouteNameById(route.getPid()));
        vo.setId(route.getId());
        vo.setPid(route.getPid());
        vo.setApplicationId(applicationId);
        vo.setApplicationName(applicationName);
        vo.setSequence(route.getSequence());
        vo.setCode(route.getCode());
        vo.setName(route.getName());
        vo.setIcon(route.getIcon());
        vo.setComponent(route.getComponent());
        vo.setLevelPath(route.getLevelPath());
        vo.setStatus(route.getStatus());
        vo.setPath(route.getPath());
        vo.setType(route.getType());
        vo.setHideChildren(route.getHideChildren());
        vo.setElements(iUpmsPageElementService.getPageElementVOSByRouteId(id));
        return vo;
    }

    private String getApplicationName(Long applicationId) {
        return iApplicationService.getNameById(applicationId);
    }
}
