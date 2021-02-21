package com.kt.iam.module.route.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.iam.common.util.Assert;
import com.kt.iam.enums.BizEnums;
import com.kt.iam.enums.DeletedEnums;
import com.kt.iam.enums.PermissionTypeEnums;
import com.kt.iam.enums.RouteStatusEnums;
import com.kt.iam.module.permission.service.IPermissionService;
import com.kt.iam.module.route.converter.RouteBeanConverter;
import com.kt.iam.module.route.dto.RouteModifyParentDTO;
import com.kt.iam.module.route.dto.RouteQueryDTO;
import com.kt.iam.module.route.dto.RouteUpdateDTO;
import com.kt.iam.module.route.persistence.IamPageElement;
import com.kt.iam.module.route.persistence.IamRoute;
import com.kt.iam.module.route.persistence.dao.IamRouteMapper;
import com.kt.iam.module.route.vo.RouteDetailVO;
import com.kt.iam.module.route.vo.RouteElementVO;
import com.kt.iam.module.route.vo.RouteListTreeVO;
import com.kt.iam.module.user.vo.UserPermissionRouteNavVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
public class RouteServiceImpl extends ServiceImpl<IamRouteMapper, IamRoute> implements IRouteService {

    private final static Long DEFAULT_PID = 0L;
    private final static Integer FIRST_LEVEL = 1;

    @Autowired
    private IPermissionService iPermissionService;
    @Autowired
    private IPageElementService iPageElementService;
    @Autowired
    private RouteBeanConverter beanConverter;

    @Override
    public Page<RouteListTreeVO> pageList(RouteQueryDTO params) {
        Page<IamRoute> pageResult = getFirstLevelRoutesByPage(params);
        List<IamRoute> firstLevelRoutes = pageResult.getRecords();
        List<IamRoute> childrenLevelRoutes = getChildrenRoutes();
        List<RouteListTreeVO> vos = recursionRoutes(firstLevelRoutes, childrenLevelRoutes);
        Page<RouteListTreeVO> voPage = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        voPage.setRecords(vos);
        return voPage;
    }

    private Page<IamRoute> getFirstLevelRoutesByPage(RouteQueryDTO params) {
        LambdaQueryWrapper<IamRoute> query = buildListQueryWrapper(params);
        return this.page(new Page<>(params.getCurrent(), params.getSize()), query);
    }

    private LambdaQueryWrapper<IamRoute> buildListQueryWrapper(RouteQueryDTO params) {
        return new LambdaQueryWrapper<IamRoute>()
                .like(StrUtil.isNotBlank(params.getName()), IamRoute::getName, params.getName())
                .eq(params.getApplicationId() != null, IamRoute::getApplicationId, params.getApplicationId())
                .eq(params.getPid() != null, IamRoute::getId, params.getPid())
                .eq(params.getStatus() != null, IamRoute::getStatus, params.getStatus())
                .eq(IamRoute::getPid, DEFAULT_PID)
                .eq(IamRoute::getIsDeleted, DeletedEnums.NOT.getCode())
                .orderByAsc(IamRoute::getSequence);
    }

    private List<IamRoute> getChildrenRoutes() {
        LambdaQueryWrapper<IamRoute> qw = new LambdaQueryWrapper<IamRoute>()
                .ne(IamRoute::getPid, DEFAULT_PID)
                .eq(IamRoute::getIsDeleted, DeletedEnums.NOT.getCode())
                .orderByAsc(IamRoute::getSequence);
        return this.list(qw);
    }

    /**
     * 递归组装路由
     *
     * @param firstLevelRoutes    一级路由
     * @param childrenLevelRoutes 子路由
     */
    private List<RouteListTreeVO> recursionRoutes(List<IamRoute> firstLevelRoutes,
                                                  List<IamRoute> childrenLevelRoutes) {
        List<RouteListTreeVO> vos = CollectionUtil.newArrayList();
        for (IamRoute route : firstLevelRoutes) {
            RouteListTreeVO item = beanConverter.convertToRouteListTreeVO(route);
            item.setChildren(CollectionUtil.newArrayList());
            findChildren(item, childrenLevelRoutes);
            vos.add(item);
        }
        return vos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20000)
    public void saveRoute(RouteUpdateDTO dto) {
        checkBeforeSave(dto);

        IamRoute route = beanConverter.convertForInsert(dto);

        IamRoute parentRoute = null;
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
        iPermissionService.addPermission(route.getId(), PermissionTypeEnums.FRONT_ROUTE);
        // 添加页面元素
        iPageElementService.batchSavePageElement(route.getId(), dto.getElements());

    }

    private void checkBeforeSave(RouteUpdateDTO dto) {
        IamRoute queryRoute = getRouteByName(dto.getName());
        Assert.isTrue(queryRoute != null, BizEnums.ROUTE_ALREADY_EXISTS);
        queryRoute = getRouteByCode(dto.getCode());
        Assert.isTrue(queryRoute != null, BizEnums.ROUTE_CODE_ALREADY_EXISTS);
    }

    private void updateLevelPathAfterSave(IamRoute route, IamRoute parentRoute) {
        Long routeId = route.getId();
        String levelPath = isFirstLevelRoute(route)
                ? routeId + StrUtil.DOT
                : parentRoute.getLevelPath() + routeId + StrUtil.DOT;
        IamRoute entity = new IamRoute();
        entity.setId(routeId);
        entity.setLevelPath(levelPath);
        this.updateById(entity);
    }

    private IamRoute getRouteByCode(String code) {
        return this.getOne(new LambdaQueryWrapper<IamRoute>().eq(IamRoute::getCode, code));
    }

    private IamRoute getRouteByName(String name) {
        return this.getOne(new LambdaQueryWrapper<IamRoute>().eq(IamRoute::getName, name));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20000)
    public void updateRoute(RouteUpdateDTO dto) {
        Long routeId = dto.getId();
        String routeName = dto.getName();
        String routeCode = dto.getCode();

        IamRoute queryRoute = getRouteByNameAndNotEqualToId(routeName, routeId);
        Assert.isTrue(queryRoute != null, BizEnums.ROUTE_ALREADY_EXISTS);
        queryRoute = getRouteByCodeAndNotEqualToId(routeCode, routeId);
        Assert.isTrue(queryRoute != null, BizEnums.ROUTE_CODE_ALREADY_EXISTS);

        IamRoute updateMenu = beanConverter.convertForUpdate(dto);
        this.updateById(updateMenu);

        if (dto.getPid() != null) {
            updateRouteLevelInfo(routeId, dto.getPid());
        }

        if (dto.getStatus() != null) {
            updateRouteStatus(routeId, dto.getStatus());
        }

        // 先删除元素再重新添加
        iPageElementService.removePageElementByRouteId(routeId);

        iPageElementService.batchSavePageElement(routeId, dto.getElements());

    }

    /**
     * 更改路由层级
     *
     * @param fromRouteId 原路由id
     * @param toRouteId   所属路由id
     */
    private void updateRouteLevelInfo(Long fromRouteId, Long toRouteId) {
        IamRoute oldRoute = getRouteById(fromRouteId);
        Long oldPid = oldRoute.getPid();
        // 如果pid不一致的话才做更新
        if (!oldPid.equals(toRouteId)) {

            IamRoute parentRoute = toRouteId.equals(DEFAULT_PID) ? getDefaultParentRoute() : getRouteById(toRouteId);
            updateRouteLevel(oldRoute, parentRoute);

            List<IamRoute> children = getChildrenRouteByLevelPath(oldRoute.getLevelPath());
            updateRouteChildrenLevel(parentRoute, children);
        }
    }

    private IamRoute getDefaultParentRoute() {
        IamRoute route = new IamRoute();
        route.setId(0L);
        route.setPid(0L);
        route.setLevelPath("");
        route.setLevel(0);
        return route;
    }

    private void updateRouteChildrenLevel(IamRoute parentRoute, List<IamRoute> children) {
        if (CollectionUtil.isNotEmpty(children)) {
            List<IamRoute> collect = children.stream().map(item -> {
                IamRoute route = new IamRoute();
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
    private void updateRouteLevel(IamRoute route, IamRoute parentRoute) {
        IamRoute newRoute = new IamRoute();
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

    private boolean isFirstLevelRoute(IamRoute route) {
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


    private List<IamRoute> getChildrenRouteByLevelPath(String levelPath) {
        return this.list(new LambdaQueryWrapper<IamRoute>()
                .likeRight(IamRoute::getLevelPath, levelPath));
    }

    @Override
    public void updateRouteStatus(RouteUpdateDTO dto) {
        updateRouteStatus(dto.getId(), dto.getStatus());
    }

    /**
     * 更新一个路由状态，要同时把它下面的子路由状态都统一更改
     */
    public void updateRouteStatus(Long routeId, Integer status) {
        IamRoute menu = getRouteById(routeId);
        if (menu != null) {
            this.update(new LambdaUpdateWrapper<IamRoute>()
                    .likeRight(IamRoute::getLevelPath, menu.getLevelPath())
                    .set(IamRoute::getStatus, status));
        }
    }

    @Override
    public void modifyParent(RouteModifyParentDTO dto) {
        IamRoute childMenu = getRouteById(dto.getId());
        Assert.isTrue(childMenu != null, BizEnums.ROUTE_NOT_EXISTS);

        IamRoute parentMenu = getRouteById(dto.getPid());
        Assert.isTrue(parentMenu != null, BizEnums.PARENT_ROUTE_NOT_EXISTS);

        IamRoute route = CglibUtil.copy(dto, IamRoute.class);
        this.updateById(route);
    }

    @Override
    public RouteDetailVO getRoute(Long id) {
        IamRoute route = getRouteById(id);
        return beanConverter.convertToRouteDetailVO(id, route);
    }

    private IamRoute getRouteById(Long id) {
        LambdaQueryWrapper<IamRoute> queryWrapper = new LambdaQueryWrapper<IamRoute>()
                .eq(IamRoute::getId, id);
        return Optional.ofNullable(this.getOne(queryWrapper)).orElseGet(IamRoute::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRouteById(Long id) {
        IamRoute route = getRouteById(id);
        List<Long> ids = getChildRoutes(route).stream().map(IamRoute::getId).collect(Collectors.toList());
        removeRouteByIds(ids);
        iPermissionService.removeByResourceIds(ids);
    }

    private void removeRouteByIds(List<Long> ids) {
        for (Long item : ids) {
            LambdaUpdateWrapper<IamRoute> qw = new LambdaUpdateWrapper<>();
            qw.eq(IamRoute::getIsDeleted, DeletedEnums.NOT.getCode());
            qw.eq(IamRoute::getId, item);
            qw.set(IamRoute::getIsDeleted, item);
            this.update(qw);
        }
    }

    private List<IamRoute> getChildRoutes(IamRoute route) {
        LambdaQueryWrapper<IamRoute> wrapper = new LambdaQueryWrapper<IamRoute>()
                .likeRight(IamRoute::getLevelPath, route.getLevelPath());
        return this.list(wrapper);
    }

    @Override
    public List<RouteListTreeVO> listAllVOs(RouteQueryDTO dto) {
        RouteQueryDTO params = new RouteQueryDTO();
        params.setApplicationId(dto.getApplicationId());
        LambdaQueryWrapper<IamRoute> qw = buildListQueryWrapper(params);
        List<IamRoute> firstLevelRoutes = this.list(qw);
        List<IamRoute> childrenRoutes = getChildrenRoutes();
        return recursionRoutes(firstLevelRoutes, childrenRoutes);
    }

    @Override
    public List<RouteElementVO> listRouteElementsById(Long routeId) {
        List<IamPageElement> elements = this.iPageElementService.listElementsByRouteId(routeId);
        return elements.stream().map(beanConverter::convertForRouteElementVO).collect(Collectors.toList());
    }

    @Override
    public String getRouteNameById(Long pid) {
        LambdaQueryWrapper<IamRoute> queryWrapper = new LambdaQueryWrapper<IamRoute>()
                .eq(IamRoute::getId, pid);
        return Optional.ofNullable(this.getOne(queryWrapper)).orElseGet(IamRoute::new).getName();
    }

    @Override
    public Integer countByApplicationId(Long applicationId) {
        return this.count(new LambdaQueryWrapper<IamRoute>().eq(IamRoute::getApplicationId, applicationId));
    }

    @Override
    public List<UserPermissionRouteNavVO> getRouteVOSByIds(List<Long> routeIds) {
        LambdaQueryWrapper<IamRoute> qw = new LambdaQueryWrapper<>();
        qw.in(IamRoute::getId, routeIds);
        qw.orderByAsc(IamRoute::getLevel, IamRoute::getSequence);
        return this.list(qw).stream()
                .map(this::convertToUserRouteVO)
                .collect(Collectors.toList());
    }

    private UserPermissionRouteNavVO convertToUserRouteVO(IamRoute item) {
        UserPermissionRouteNavVO userMenuItem = new UserPermissionRouteNavVO();
        userMenuItem.setName(item.getCode());
        userMenuItem.setParentId(item.getPid());
        userMenuItem.setId(String.valueOf(item.getId()));
        userMenuItem.setMeta(assembleMeta(item));
        userMenuItem.setComponent(item.getComponent());
        userMenuItem.setRedirect("");
        userMenuItem.setPath(item.getPath());
        return userMenuItem;
    }

    private UserPermissionRouteNavVO.Meta assembleMeta(IamRoute item) {
        UserPermissionRouteNavVO.Meta meta = new UserPermissionRouteNavVO.Meta();
        meta.setIcon(item.getIcon());
        meta.setTitle(item.getName());
        meta.setHideChildren(item.getHideChildren());
        meta.setShow(item.getStatus().equals(RouteStatusEnums.ENABLED.getValue()));
        return meta;
    }

    private void findChildren(RouteListTreeVO parent, List<IamRoute> list) {
        for (IamRoute route : list) {
            if (parent.getId().equals(route.getPid())) {
                RouteListTreeVO item = beanConverter.convertToRouteListTreeVO(route);
                item.setChildren(CollectionUtil.newArrayList());
                parent.getChildren().add(item);
                findChildren(item, list);
            }
        }
    }

    private IamRoute getRouteByNameAndNotEqualToId(String name, Long id) {
        LambdaQueryWrapper<IamRoute> queryWrapper = new LambdaQueryWrapper<IamRoute>()
                .eq(IamRoute::getName, name)
                .ne(IamRoute::getId, id);
        return this.getOne(queryWrapper);
    }

    private IamRoute getRouteByCodeAndNotEqualToId(String code, Long id) {
        LambdaQueryWrapper<IamRoute> queryWrapper = new LambdaQueryWrapper<IamRoute>()
                .eq(IamRoute::getCode, code)
                .ne(IamRoute::getId, id);
        return this.getOne(queryWrapper);
    }

}
