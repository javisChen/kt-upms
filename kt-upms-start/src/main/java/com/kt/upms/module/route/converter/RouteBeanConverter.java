package com.kt.upms.module.route.converter;

import com.kt.upms.entity.UpmsPageElement;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.entity.UpmsRoute;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.module.permission.service.IUpmsPermissionService;
import com.kt.upms.module.route.dto.RouteUpdateDTO;
import com.kt.upms.module.route.vo.RouteElementVO;
import com.kt.upms.module.route.vo.RouteListTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class RouteBeanConverter {

    @Autowired
    private IUpmsPermissionService iUpmsPermissionService;

    public RouteListTreeVO assembleRouteListTreeVO(UpmsRoute route) {
        UpmsPermission permission = iUpmsPermissionService.getPermissionByResourceIdAndType(route.getId(),
                PermissionTypeEnums.FRONT_ROUTE);
        RouteListTreeVO treeNode = new RouteListTreeVO();
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
        treeNode.setHideChildren(route.getHideChildren());
        treeNode.setCreateTime(route.getGmtCreate());
        treeNode.setUpdateTime(route.getGmtModified());
        return treeNode;
    }

    public UpmsRoute convertToDataObject(RouteUpdateDTO dto) {
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
        return upmsRoute;
    }

    public RouteElementVO convertForRouteElementVO(UpmsPageElement obj) {
        UpmsPermission permission = iUpmsPermissionService.getPermission(obj.getId(), PermissionTypeEnums.PAGE_ELEMENT);
        RouteElementVO vo = new RouteElementVO();
        vo.setId(obj.getId());
        vo.setRouteId(obj.getRouteId());
        vo.setName(obj.getName());
        vo.setPermissionId(permission.getId());
        vo.setPermissionCode(permission.getCode());
        vo.setType(obj.getType());
        return vo;
    }
}
