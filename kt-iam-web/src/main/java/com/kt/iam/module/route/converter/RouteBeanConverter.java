package com.kt.iam.module.route.converter;

import com.kt.iam.module.route.persistence.IamPageElement;
import com.kt.iam.module.permission.persistence.IamPermission;
import com.kt.iam.module.route.persistence.IamRoute;
import com.kt.iam.enums.PermissionTypeEnums;
import com.kt.iam.module.application.service.IApplicationService;
import com.kt.iam.module.permission.service.IPermissionService;
import com.kt.iam.module.route.dto.RouteUpdateDTO;
import com.kt.iam.module.route.service.IPageElementService;
import com.kt.iam.module.route.service.IRouteService;
import com.kt.iam.module.route.vo.RouteDetailVO;
import com.kt.iam.module.route.vo.RouteElementVO;
import com.kt.iam.module.route.vo.RouteListTreeVO;
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
    private IPageElementService iPageElementService;

    @Autowired
    private IRouteService iRouteService;

    public RouteListTreeVO convertToRouteListTreeVO(IamRoute route) {
        IamPermission permission = iPermissionService.getPermissionByResourceIdAndType(route.getId(),
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

    public IamRoute convertForInsert(RouteUpdateDTO dto) {
        IamRoute iamRoute = new IamRoute();
        iamRoute.setName(dto.getName());
        iamRoute.setCode(dto.getCode());
        iamRoute.setComponent(dto.getComponent());
        iamRoute.setType(dto.getType());
        iamRoute.setHideChildren(dto.getHideChildren());
        iamRoute.setPid(dto.getPid());
        iamRoute.setSequence(dto.getSequence());
        iamRoute.setPath(dto.getPath());
        iamRoute.setIcon(dto.getIcon());
        iamRoute.setStatus(dto.getStatus());
        iamRoute.setId(dto.getId());
        iamRoute.setApplicationId(dto.getApplicationId());
        return iamRoute;
    }

    public RouteElementVO convertForRouteElementVO(IamPageElement obj) {
        IamPermission permission = iPermissionService.getPermission(obj.getId(), PermissionTypeEnums.PAGE_ELEMENT);
        RouteElementVO vo = new RouteElementVO();
        vo.setId(obj.getId());
        vo.setRouteId(obj.getRouteId());
        vo.setName(obj.getName());
        vo.setPermissionId(permission.getId());
        vo.setPermissionCode(permission.getCode());
        vo.setType(obj.getType());
        return vo;
    }

    public IamRoute convertForUpdate(RouteUpdateDTO dto) {
        IamRoute iamRoute = new IamRoute();
        iamRoute.setId(dto.getId());
        iamRoute.setName(dto.getName());
        iamRoute.setCode(dto.getCode());
        iamRoute.setComponent(dto.getComponent());
        iamRoute.setType(dto.getType());
        iamRoute.setHideChildren(dto.getHideChildren());
        iamRoute.setSequence(dto.getSequence());
        iamRoute.setPath(dto.getPath());
        iamRoute.setIcon(dto.getIcon());
        iamRoute.setStatus(dto.getStatus());
        iamRoute.setApplicationId(dto.getApplicationId());
        return iamRoute;
    }

    public RouteDetailVO convertToRouteDetailVO(Long id, IamRoute route) {
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
        vo.setElements(iPageElementService.getPageElementVOSByRouteId(id));
        return vo;
    }

    private String getApplicationName(Long applicationId) {
        return iApplicationService.getNameById(applicationId);
    }
}
