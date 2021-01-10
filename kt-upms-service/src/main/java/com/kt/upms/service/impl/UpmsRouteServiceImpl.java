package com.kt.upms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.route.*;
import com.kt.model.dto.route.UserRoutesDTO.UserRouteItem.Meta;
import com.kt.model.enums.BizEnums;
import com.kt.model.vo.route.RouteDetailVO;
import com.kt.model.vo.route.RouteListTreeVO;
import com.kt.upms.entity.UpmsRoute;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.enums.RouteStatusEnums;
import com.kt.upms.mapper.UpmsRouteMapper;
import com.kt.upms.service.IUpmsPageElementService;
import com.kt.upms.service.IUpmsPermissionService;
import com.kt.upms.service.IUpmsRouteService;
import com.kt.upms.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
@Service
public class UpmsRouteServiceImpl extends ServiceImpl<UpmsRouteMapper, UpmsRoute> implements IUpmsRouteService {

    private final static Long DEFAULT_PID = 0L;
    private final static Integer FIRST_LEVEL = 1;

    private final IUpmsPermissionService iUpmsPermissionService;
    private final IUpmsPageElementService iUpmsPageElementService;

    public UpmsRouteServiceImpl(IUpmsPermissionService iUpmsPermissionService, IUpmsPageElementService iUpmsPageElementService) {
        this.iUpmsPermissionService = iUpmsPermissionService;
        this.iUpmsPageElementService = iUpmsPageElementService;
    }

    @Override
    public PageResponse<RouteListTreeVO> pageList(RouteQueryDTO params) {
        LambdaQueryWrapper<UpmsRoute> query = new LambdaQueryWrapper<UpmsRoute>()
                .like(StrUtil.isNotBlank(params.getName()), UpmsRoute::getName, params.getName())
                .eq(params.getPid() != null, UpmsRoute::getId, params.getPid())
                .eq(params.getStatus() != null, UpmsRoute::getStatus, params.getStatus())
                .eq(UpmsRoute::getPid, DEFAULT_PID)
                .orderByAsc(UpmsRoute::getSequence)
                ;
        Page<UpmsRoute> pageResult = this.page(new Page<>(params.getCurrent(), params.getSize()), query);

        List<UpmsRoute> levelOneMenus = pageResult.getRecords();
        List<UpmsRoute> anotherMenus = this.list(new LambdaQueryWrapper<UpmsRoute>().ne(UpmsRoute::getPid, DEFAULT_PID));

        List<RouteListTreeVO> vos = CollectionUtil.newArrayList();
        for (UpmsRoute route : levelOneMenus) {
            RouteListTreeVO item = assembleRouteListTreeVO(route);
            item.setChildren(CollectionUtil.newArrayList());
            findChildren(item, anotherMenus);
            vos.add(item);
        }
        return PageResponse.success(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal(), vos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20000)
    public void saveRoute(RouteAddDTO dto) {
        UpmsRoute queryRoute = getMenuByName(dto.getName());
        Assert.isTrue(queryRoute != null, BizEnums.ROUTE_ALREADY_EXISTS);
        queryRoute = getRouteByCode(dto.getCode());
        Assert.isTrue(queryRoute != null, BizEnums.ROUTE_CODE_ALREADY_EXISTS);

        UpmsRoute route = assembleUpmsRoute(dto);
        UpmsRoute parentRoute = null;
        if (this.isFirstLevelRoute(route)) {
            route.setLevel(FIRST_LEVEL);
        } else {
            parentRoute = getRouteById(route.getPid());
            Assert.isTrue(parentRoute == null, BizEnums.PARENT_ROUTE_NOT_EXISTS);
            route.setLevel(parentRoute.getLevel() + 1);
        }
        this.save(route);

        // 新增完路由记录后再更新层级信息
        updateLevelPathAfterSave(route, parentRoute);
        // 添加到权限
        iUpmsPermissionService.addPermission(route.getId(), PermissionTypeEnums.FRONT_ROUTE);
        // 添加页面元素
        iUpmsPageElementService.batchSavePageElement(route.getId(), dto.getElements());

    }

    private void updateLevelPathAfterSave(UpmsRoute route, UpmsRoute parentRoute) {
        Long routeId = route.getId();
        String levelPath = isFirstLevelRoute(route)
                ? routeId + StrUtil.DOT
                : parentRoute.getLevelPath() + routeId + StrUtil.DOT;
        UpmsRoute entity = new UpmsRoute();
        entity.setId(routeId);
        entity.setLevelPath(levelPath);
        this.updateById(entity);
    }

    private UpmsRoute getRouteByCode(String code) {
        return this.getOne(new LambdaQueryWrapper<UpmsRoute>().eq(UpmsRoute::getCode, code));
    }

    private UpmsRoute assembleUpmsRoute(RouteAddDTO dto) {
        return CglibUtil.copy(dto, UpmsRoute.class);
    }

    private UpmsRoute getMenuByName(String name) {
        return this.getOne(new LambdaQueryWrapper<UpmsRoute>().eq(UpmsRoute::getName, name));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20000)
    public void updateRoute(RouteUpdateDTO dto) {
        Long routeId = dto.getId();
        String routeName = dto.getName();
        String routeCode = dto.getCode();

        UpmsRoute queryRoute = getRouteByNameAndNotEqualToId(routeName, routeId);
        Assert.isTrue(queryRoute != null, BizEnums.ROUTE_ALREADY_EXISTS);
        queryRoute = getRouteByCodeAndNotEqualToId(routeCode, routeId);
        Assert.isTrue(queryRoute != null, BizEnums.ROUTE_CODE_ALREADY_EXISTS);

        UpmsRoute updateMenu = assembleUpdateRoute(dto);
        this.updateById(updateMenu);

        if (dto.getPid() != null) {
            updateRouteLevelInfo(routeId, dto.getPid());
        }
        if (dto.getStatus() != null) {
            updateRouteStatus(routeId, dto.getStatus());
        }

        // 先删除元素再重新添加
        iUpmsPageElementService.removePageElementByRouteId(routeId);

        iUpmsPageElementService.batchSavePageElement(routeId, dto.getElements());

    }

    private UpmsRoute assembleUpdateRoute(RouteUpdateDTO dto) {
        UpmsRoute copy = CglibUtil.copy(dto, UpmsRoute.class);
        copy.setPid(null);
        copy.setStatus(null);
        return copy;
    }

    /**
     * 更改路由层级
     *
     * @param fromRouteId 原路由id
     * @param toRouteId   所属路由id
     */
    private void updateRouteLevelInfo(Long fromRouteId, Long toRouteId) {
        UpmsRoute oldRoute = getRouteById(fromRouteId);
        Long oldPid = oldRoute.getPid();
        // 如果pid不一致的话才做更新
        if (!oldPid.equals(toRouteId)) {

            UpmsRoute parentRoute = toRouteId.equals(DEFAULT_PID) ? getDefaultParentRoute() : getRouteById(toRouteId);
            updateRouteLevel(oldRoute, parentRoute);

            List<UpmsRoute> children = getChildrenRouteByLevelPath(oldRoute.getLevelPath());
            updateRouteChildrenLevel(parentRoute, children);
        }
    }

    private UpmsRoute getDefaultParentRoute() {
        UpmsRoute route = new UpmsRoute();
        route.setId(0L);
        route.setPid(0L);
        route.setLevelPath("");
        route.setLevel(0);
        return route;
    }

    private void updateRouteChildrenLevel(UpmsRoute parentRoute, List<UpmsRoute> children) {
        if (CollectionUtil.isNotEmpty(children)) {
            List<UpmsRoute> collect = children.stream().map(item -> {
                UpmsRoute route = new UpmsRoute();
                route.setId(item.getId());
                route.setLevel(parentRoute.getLevel() + 1);
                route.setLevelPath(createChildLevelPath(item.getId(), item.getLevelPath(), parentRoute.getLevelPath()));
                return route;
            }).collect(Collectors.toList());
            this.updateBatchById(collect);
        }
    }

    /**
     * 更新路由的层级信息
     */
    private void updateRouteLevel(UpmsRoute route, UpmsRoute parentRoute) {
        UpmsRoute newRoute = new UpmsRoute();
        newRoute.setId(route.getId());
        newRoute.setPid(parentRoute.getId());
        String routeLevelPath = route.getLevelPath();
        String parentRouteLevelPath = parentRoute.getLevelPath();
        String levelPath = isFirstLevelRoute(route) ? (parentRouteLevelPath + routeLevelPath)
                : createNewLevelPath(route.getId(), routeLevelPath, parentRouteLevelPath);
        newRoute.setLevelPath(levelPath);
        newRoute.setLevel(parentRoute.getLevel() + 1);
        this.updateById(newRoute);
    }

    private boolean isFirstLevelRoute(UpmsRoute route) {
        return DEFAULT_PID.equals(route.getPid()) || FIRST_LEVEL.equals(route.getLevel());
    }

    private String createNewLevelPath(Long oldRouteId, String oldRouteLevelPath, String parentRouteLevelPath) {
        String partOfOldParent = StringUtils.substringBefore(oldRouteLevelPath, String.valueOf(oldRouteId));
        return StringUtils.replace(oldRouteLevelPath, partOfOldParent, parentRouteLevelPath);
    }

    private String createChildLevelPath(Long oldRoutePid, String oldRouteLevelPath,
                                        String newParentRouteLevelPath) {
        // 如果当前路由是"64.65.66." 父路由id是"65"，那么该值就是"64."，把该值替换成当前修改的父级路由的level_path
        String partOfOldParent = StringUtils.substringBefore(oldRouteLevelPath, String.valueOf(oldRoutePid));
        return StringUtils.replace(oldRouteLevelPath, partOfOldParent, newParentRouteLevelPath);
    }


    private List<UpmsRoute> getChildrenRouteByLevelPath(String levelPath) {
        return this.list(new LambdaQueryWrapper<UpmsRoute>()
                .likeRight(UpmsRoute::getLevelPath, levelPath));
    }

    @Override
    public void updateRouteStatus(RouteUpdateDTO dto) {
        updateRouteStatus(dto.getId(), dto.getStatus());
    }

    public void updateRouteStatus(Long routeId, Integer status) {
        UpmsRoute menu = getRouteById(routeId);
        if (menu != null) {
            this.update(new LambdaUpdateWrapper<UpmsRoute>()
                    .likeRight(UpmsRoute::getLevelPath, menu.getLevelPath())
                    .set(UpmsRoute::getStatus, status));
        }
    }

    @Override
    public void modifyParent(RouteModifyParentDTO dto) {
        UpmsRoute childMenu = getRouteById(dto.getId());
        Assert.isTrue(childMenu != null, BizEnums.ROUTE_NOT_EXISTS);

        UpmsRoute parentMenu = getRouteById(dto.getPid());
        Assert.isTrue(parentMenu != null, BizEnums.PARENT_ROUTE_NOT_EXISTS);

        UpmsRoute route = CglibUtil.copy(dto, UpmsRoute.class);
        this.updateById(route);
    }

    public UpmsRoute getRouteById(Long id) {
        LambdaQueryWrapper<UpmsRoute> queryWrapper = new LambdaQueryWrapper<UpmsRoute>()
                .eq(UpmsRoute::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public RouteDetailVO getRoute(Long id) {
        LambdaQueryWrapper<UpmsRoute> queryWrapper = new LambdaQueryWrapper<UpmsRoute>()
                .eq(UpmsRoute::getId, id);
        UpmsRoute route = this.getOne(queryWrapper);
        RouteDetailVO vo = CglibUtil.copy(route, RouteDetailVO.class);
        vo.setElements(iUpmsPageElementService.getPageElementVOSByRouteId(id));
        return vo;
    }

    @Override
    public UserRoutesDTO getUserRoutes() {
        LambdaQueryWrapper<UpmsRoute> qw = new LambdaQueryWrapper<UpmsRoute>()
                .orderByAsc(UpmsRoute::getLevel, UpmsRoute::getSequence);
        return new UserRoutesDTO(this.list(qw).stream()
                .map(this::assembleUserMenuItem)
                .collect(Collectors.toList())
        );
    }

    private UserRoutesDTO.UserRouteItem assembleUserMenuItem(UpmsRoute item) {
        UserRoutesDTO.UserRouteItem userMenuItem = new UserRoutesDTO.UserRouteItem();
        userMenuItem.setName(item.getCode());
        userMenuItem.setParentId(String.valueOf(item.getPid()));
        userMenuItem.setId(String.valueOf(item.getId()));
        userMenuItem.setMeta(assembleMeta(item));
        userMenuItem.setComponent(item.getComponent());
        userMenuItem.setRedirect("");
        userMenuItem.setPath(item.getPath());
        return userMenuItem;
    }

    private UserRoutesDTO.UserRouteItem.Meta assembleMeta(UpmsRoute item) {
        Meta meta = new Meta();
        meta.setIcon(item.getIcon());
        meta.setTitle(item.getName());
        meta.setHideChildren(item.getHideChildren());
        meta.setShow(item.getStatus().equals(RouteStatusEnums.ENABLED.getValue()));
        return meta;
    }

    private void findChildren(RouteListTreeVO parent, List<UpmsRoute> list) {
        for (UpmsRoute route : list) {
            if (parent.getId().equals(route.getPid())) {
                RouteListTreeVO item = assembleRouteListTreeVO(route);
                item.setChildren(CollectionUtil.newArrayList());
                parent.getChildren().add(item);
                findChildren(item, list);
            }
        }
    }

    @Override
    public void deleteRouteById(Long id) {
        UpmsRoute route = getRouteById(id);
        LambdaQueryWrapper<UpmsRoute> wrapper = new LambdaQueryWrapper<UpmsRoute>()
                .likeRight(UpmsRoute::getLevelPath, route.getLevelPath());
        this.remove(wrapper);
    }

    @Override
    public List<RouteListTreeVO> getTree() {
        List<UpmsRoute> pageResult = this.list(new LambdaQueryWrapper<UpmsRoute>().eq(UpmsRoute::getPid, DEFAULT_PID));
        List<UpmsRoute> anotherMenus = this.list(new LambdaQueryWrapper<UpmsRoute>().ne(UpmsRoute::getPid, DEFAULT_PID));

        List<RouteListTreeVO> vos = CollectionUtil.newArrayList();
        for (UpmsRoute route : pageResult) {
            RouteListTreeVO item = assembleRouteListTreeVO(route);
            item.setChildren(CollectionUtil.newArrayList());
            findChildren(item, anotherMenus);
            vos.add(item);
        }
        return vos;
    }

    private RouteListTreeVO assembleRouteListTreeVO(UpmsRoute route) {
        RouteListTreeVO treeNode = new RouteListTreeVO();
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

    private UpmsRoute getRouteByNameAndNotEqualToId(String name, Long id) {
        LambdaQueryWrapper<UpmsRoute> queryWrapper = new LambdaQueryWrapper<UpmsRoute>()
                .eq(UpmsRoute::getName, name)
                .ne(UpmsRoute::getId, id);
        return this.getOne(queryWrapper);
    }

    private UpmsRoute getRouteByCodeAndNotEqualToId(String code, Long id) {
        LambdaQueryWrapper<UpmsRoute> queryWrapper = new LambdaQueryWrapper<UpmsRoute>()
                .eq(UpmsRoute::getCode, code)
                .ne(UpmsRoute::getId, id);
        return this.getOne(queryWrapper);
    }

}
