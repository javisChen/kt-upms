package com.kt.upms.service.impl;
import cn.hutool.core.collection.CollectionUtil;
import com.kt.model.dto.menu.UserRoutesDTO.UserRouteItem.Meta;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.menu.*;
import com.kt.model.enums.BizEnums;
import com.kt.model.vo.route.RouteAnotherTreeVO;
import com.kt.upms.entity.UpmsRoute;
import com.kt.upms.enums.RouteStatusEnum;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.mapper.UpmsRouteMapper;
import com.kt.upms.service.IUpmsRouteService;
import com.kt.upms.service.IUpmsPermissionService;
import com.kt.upms.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    private final static Integer LEVEL_ONE = 1;

    private final IUpmsPermissionService iUpmsPermissionService;

    public UpmsRouteServiceImpl(IUpmsPermissionService iUpmsPermissionService) {
        this.iUpmsPermissionService = iUpmsPermissionService;
    }

    @Override
    public PageResponse<UpmsRoute> pageList(Page page, RouteQueryDTO params) {
        LambdaQueryWrapper<UpmsRoute> query = new LambdaQueryWrapper<UpmsRoute>()
                .like(StrUtil.isNotBlank(params.getName()), UpmsRoute::getName, params.getName())
                .eq(params.getPid() != null, UpmsRoute::getId, params.getPid());
        return PageResponse.success(this.page(page, query));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20000)
    public void saveRoute(RouteAddDTO dto) {
        UpmsRoute queryRoute = getMenuByName(dto.getName());
        Assert.isTrue(queryRoute != null, BizEnums.MENU_ALREADY_EXISTS);
        queryRoute = getRouteByCode(dto.getCode());
        Assert.isTrue(queryRoute != null, BizEnums.MENU_UNIQUE_KEY_ALREADY_EXISTS);

        UpmsRoute route = assembleUpmsRoute(dto);
        if (route.getPid().equals(DEFAULT_PID)) {
            route.setLevel(LEVEL_ONE);
            route.setLevelPath(LEVEL_ONE + StrUtil.DOT);
        } else {
            UpmsRoute parentMenu = getRouteById(route.getPid());
            Assert.isTrue(parentMenu == null, BizEnums.PARENT_MENU_NOT_EXISTS);
            Assert.isTrue(StringUtils.isBlank(dto.getComponent()), BizEnums.MENU_COMPONENT_IS_REQUIRE);

            int level = parentMenu.getLevel() + 1;
            route.setLevel(level);
            route.setLevelPath(parentMenu.getLevelPath() + level + StrUtil.DOT);
        }
        this.save(route);

        iUpmsPermissionService.addPermission(route.getId(), PermissionTypeEnums.FRONT_ROUTE);

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
    public void updateRoute(RouteUpdateDTO dto) {
        LambdaQueryWrapper<UpmsRoute> queryWrapper = new LambdaQueryWrapper<UpmsRoute>()
                .eq(UpmsRoute::getName, dto.getName())
                .ne(UpmsRoute::getId, dto.getId());
        int count = this.count(queryWrapper);
        Assert.isTrue(count > 0, BizEnums.MENU_ALREADY_EXISTS);

        UpmsRoute updateMenu = CglibUtil.copy(dto, UpmsRoute.class);
        this.updateById(updateMenu);
    }

    @Override
    public void updateRouteStatus(RouteUpdateDTO dto) {
        UpmsRoute menu = getRouteById(dto.getId());
        if (menu != null) {
            this.update(new LambdaUpdateWrapper<UpmsRoute>()
                    .likeRight(UpmsRoute::getLevelPath, menu.getLevelPath())
                    .set(UpmsRoute::getStatus, dto.getStatus()));
        }
    }

    @Override
    public void modifyParent(RouteModifyParentDTO dto) {
        UpmsRoute childMenu = getRouteById(dto.getId());
        Assert.isTrue(childMenu != null, BizEnums.MENU_NOT_EXISTS);

        UpmsRoute parentMenu = getRouteById(dto.getPid());
        Assert.isTrue(parentMenu != null, BizEnums.PARENT_MENU_NOT_EXISTS);

        UpmsRoute route = CglibUtil.copy(dto, UpmsRoute.class);
        this.updateById(route);
    }

    @Override
    public UpmsRoute getRouteById(Long id) {
        LambdaQueryWrapper<UpmsRoute> queryWrapper = new LambdaQueryWrapper<UpmsRoute>()
                .eq(UpmsRoute::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public UserRoutesDTO getAllRoutes() {
        LambdaQueryWrapper<UpmsRoute> qw = buildQueryWrapper();
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
        meta.setShow(item.getStatus().equals(RouteStatusEnum.ENABLED.getValue()));
        return meta;
    }

    @Override
    public RouteTreeDTO getRouteTree() {
        RouteTreeDTO dto = new RouteTreeDTO();
        List<UpmsRoute> list = this.list();
        if (CollectionUtil.isEmpty(list)) {
            return dto;
        }
        List<UpmsRoute> levelOneMenus = list.stream().filter(item -> item.getPid().equals(DEFAULT_PID))
                .collect(Collectors.toList());
        List<UpmsRoute> anotherMenus = list.stream().filter(item -> !item.getPid().equals(DEFAULT_PID))
                .collect(Collectors.toList());

        List<RouteTreeDTO.TreeNode> resultSet = CollectionUtil.newArrayList();
        for (UpmsRoute route : levelOneMenus) {
            RouteTreeDTO.TreeNode item = CglibUtil.copy(route, RouteTreeDTO.TreeNode.class);
            item.setChildren(CollectionUtil.newArrayList());
            findChildren(item, anotherMenus);
            resultSet.add(item);
        }
        dto.setRoutes(resultSet);
        return dto;
    }

    private void findChildren(RouteTreeDTO.TreeNode parent, List<UpmsRoute> list) {
        for (UpmsRoute route : list) {
            if (parent.getId().equals(route.getPid())) {
                RouteTreeDTO.TreeNode item = CglibUtil.copy(route, RouteTreeDTO.TreeNode.class);
                item.setChildren(CollectionUtil.newArrayList());
                parent.getChildren().add(item);
                findChildren(item, list);
            }
        }
    }

    @Override
    public RouteAnotherTreeVO getRouteAnotherTree() {
        RouteAnotherTreeVO dto = new RouteAnotherTreeVO();
        LambdaQueryWrapper<UpmsRoute> qw = buildQueryWrapper();
        List<UpmsRoute> list = this.list(qw);
        if (CollectionUtil.isEmpty(list)) {
            return dto;
        }
        List<UpmsRoute> levelOneMenus = list.stream().filter(item -> item.getPid().equals(DEFAULT_PID))
                .collect(Collectors.toList());
        List<UpmsRoute> anotherMenus = list.stream().filter(item -> !item.getPid().equals(DEFAULT_PID))
                .collect(Collectors.toList());

        List<RouteAnotherTreeVO.TreeNode> resultSet = CollectionUtil.newArrayList();
        for (UpmsRoute route : levelOneMenus) {
            RouteAnotherTreeVO.TreeNode item = assembleAnotherTreeNo(route);
            findAnotherTreeNodeChildren(item, anotherMenus);
            resultSet.add(item);
        }
        dto.setRoutes(resultSet);
        return dto;
    }

    private LambdaQueryWrapper<UpmsRoute> buildQueryWrapper() {
        return new LambdaQueryWrapper<UpmsRoute>()
                .orderByAsc(UpmsRoute::getLevel, UpmsRoute::getSequence);
    }

    private RouteAnotherTreeVO.TreeNode assembleAnotherTreeNo(UpmsRoute route) {
        RouteAnotherTreeVO.TreeNode treeNode = new RouteAnotherTreeVO.TreeNode();
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
        return treeNode;
    }

    private void findAnotherTreeNodeChildren(RouteAnotherTreeVO.TreeNode parent, List<UpmsRoute> list) {
        for (UpmsRoute route : list) {
            if (parent.getId().equals(route.getPid())) {
                RouteAnotherTreeVO.TreeNode item = assembleAnotherTreeNo(route);
                parent.getChildren().add(item);
                findAnotherTreeNodeChildren(item, list);
                if (!CollectionUtils.isEmpty(parent.getChildren())) {
                    parent.setGroup(true);
                }
            }
        }
    }

    private void updateStatus(RouteUpdateDTO dto, RouteStatusEnum statusEnum) {
        this.update(new LambdaUpdateWrapper<UpmsRoute>()
                .eq(UpmsRoute::getStatus, dto.getId())
                .set(UpmsRoute::getStatus, statusEnum.getValue()));
    }

}
