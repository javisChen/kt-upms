package com.kt.upms.service.impl;
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
import com.kt.upms.entity.UpmsRoute;
import com.kt.upms.enums.MenuStatusEnum;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.mapper.UpmsRouteMapper;
import com.kt.upms.service.IUpmsRouteService;
import com.kt.upms.service.IUpmsPermissionService;
import com.kt.upms.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private UpmsRoute getRouteById(Long id) {
        LambdaQueryWrapper<UpmsRoute> queryWrapper = new LambdaQueryWrapper<UpmsRoute>()
                .eq(UpmsRoute::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public UserRoutesDTO getAllRoutes() {
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
        return userMenuItem;
    }

    private UserRoutesDTO.UserRouteItem.Meta assembleMeta(UpmsRoute item) {
        Meta meta = new Meta();
        meta.setIcon(item.getIcon());
        meta.setTitle(item.getName());
        meta.setHideChildren(item.getHideChildren());
        meta.setShow(item.getStatus().equals(MenuStatusEnum.ENABLED.getValue()));
        return meta;
    }

//    public Set<UserMenusDTO.UserMenuItem> getTree(List<UpmsMenu> list) {
//        if (CollectionUtil.isEmpty(list)) {
//            return CollectionUtil.newHashSet();
//        }
//        Set<UserMenusDTO.UserMenuItem> resultSet = CollectionUtil.newHashSet();
//        Set<UpmsMenu> levelOneMenus = list.stream().filter(item -> item.getPid().equals(DEFAULT_PID))
//                .collect(Collectors.toSet());
//        Set<UpmsMenu> anotherMenus = list.stream().filter(item -> !item.getPid().equals(DEFAULT_PID))
//                .collect(Collectors.toSet());
//
//        for (UpmsMenu upmsMenu : levelOneMenus) {
//            UserMenusDTO.UserMenuItem item = CglibUtil.copy(upmsMenu, UserMenusDTO.UserMenuItem.class);
//            item.setChildren(CollectionUtil.newHashSet());
//            findChildren(item, anotherMenus);
//            resultSet.add(item);
//        }
//        return resultSet;
//    }

//    private void findChildren(UserMenusDTO.UserMenuItem parent, Set<UpmsMenu> list) {
//        for (UpmsMenu upmsMenu : list) {
//            if (parent.getId().equals(upmsMenu.getPid())) {
//                UserMenusDTO.UserMenuItem item = CglibUtil.copy(upmsMenu, UserMenusDTO.UserMenuItem.class);
//                item.setChildren(new HashSet<>());
//                parent.getChildren().add(item);
//                findChildren(item, list);
//            }
//        }
//    }

    private void updateStatus(RouteUpdateDTO dto, MenuStatusEnum statusEnum) {
        this.update(new LambdaUpdateWrapper<UpmsRoute>()
                .eq(UpmsRoute::getStatus, dto.getId())
                .set(UpmsRoute::getStatus, statusEnum.getValue()));
    }

}
