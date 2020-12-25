//package com.kt.upms.service.impl;
//
//import cn.hutool.core.collection.CollectionUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.extra.cglib.CglibUtil;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.kt.component.dto.PageResponse;
//import com.kt.model.dto.menu.*;
//import com.kt.model.dto.menu.MenuAnotherTreeDTO.TreeNode;
//import com.kt.model.dto.menu.UserMenusDTO.UserRouteItem.Meta;
//import com.kt.model.enums.BizEnums;
//import com.kt.upms.entity.UpmsMenu;
//import com.kt.upms.enums.MenuStatusEnum;
//import com.kt.upms.enums.PermissionTypeEnums;
//import com.kt.upms.mapper.UpmsMenuMapper;
//import com.kt.upms.service.IUpmsMenuService;
//import com.kt.upms.service.IUpmsPermissionService;
//import com.kt.upms.util.Assert;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.CollectionUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * <p>
// * 菜单表 服务实现类
// * </p>
// *
// * @author
// * @since 2020-11-09
// */
//@Service
//public class UpmsMenuServiceImpl extends ServiceImpl<UpmsMenuMapper, UpmsMenu> implements IUpmsMenuService {
//
//    private final static Long DEFAULT_PID = 0L;
//    private final static Integer LEVEL_ONE = 1;
//
//    private final IUpmsPermissionService iUpmsPermissionService;
//
//    public UpmsMenuServiceImpl(IUpmsPermissionService iUpmsPermissionService) {
//        this.iUpmsPermissionService = iUpmsPermissionService;
//    }
//
//    @Override
//    public PageResponse<UpmsMenu> pageList(Page page, RouteQueryDTO params) {
//        LambdaQueryWrapper<UpmsMenu> query = new LambdaQueryWrapper<UpmsMenu>()
//                .like(StrUtil.isNotBlank(params.getName()), UpmsMenu::getName, params.getName())
//                .eq(params.getPid() != null, UpmsMenu::getId, params.getPid());
//        return PageResponse.success(this.page(page, query));
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class, timeout = 20000)
//    public void saveRoute(MenuAddDTO dto) {
//        UpmsMenu queryRoute = getMenuByName(dto.getName());
//        Assert.isTrue(queryRoute != null, BizEnums.MENU_ALREADY_EXISTS);
//        queryRoute = getRouteByCode(dto.getCode());
//        Assert.isTrue(queryRoute != null, BizEnums.MENU_UNIQUE_KEY_ALREADY_EXISTS);
//
//        UpmsMenu route = assembleUpmsRoute(dto);
//        if (route.getPid().equals(DEFAULT_PID)) {
//            route.setLevel(LEVEL_ONE);
//            route.setLevelPath(LEVEL_ONE + StrUtil.DOT);
//        } else {
//            UpmsMenu parentMenu = getRouteById(route.getPid());
//            Assert.isTrue(parentMenu == null, BizEnums.PARENT_MENU_NOT_EXISTS);
//            Assert.isTrue(StringUtils.isBlank(dto.getComponent()), BizEnums.MENU_COMPONENT_IS_REQUIRE);
//
//            int level = parentMenu.getLevel() + 1;
//            route.setLevel(level);
//            route.setLevelPath(parentMenu.getLevelPath() + level + StrUtil.DOT);
//        }
//        this.save(route);
//
//        iUpmsPermissionService.addPermission(route.getId(), PermissionTypeEnums.FRONT_ROUTE);
//
//    }
//
//    private UpmsMenu getRouteByCode(String code) {
//        return this.getOne(new LambdaQueryWrapper<UpmsMenu>().eq(UpmsMenu::getCode, code));
//    }
//
//    private UpmsMenu assembleUpmsRoute(MenuAddDTO dto) {
//        return CglibUtil.copy(dto, UpmsMenu.class);
//    }
//
//    private UpmsMenu getMenuByName(String name) {
//        return this.getOne(new LambdaQueryWrapper<UpmsMenu>().eq(UpmsMenu::getName, name));
//    }
//
//    @Override
//    public void updateRoute(MenuUpdateDTO dto) {
//        LambdaQueryWrapper<UpmsMenu> queryWrapper = new LambdaQueryWrapper<UpmsMenu>()
//                .eq(UpmsMenu::getName, dto.getName())
//                .ne(UpmsMenu::getId, dto.getId());
//        int count = this.count(queryWrapper);
//        Assert.isTrue(count > 0, BizEnums.MENU_ALREADY_EXISTS);
//
//        UpmsMenu updateMenu = CglibUtil.copy(dto, UpmsMenu.class);
//        this.updateById(updateMenu);
//    }
//
//    @Override
//    public void updateRouteStatus(MenuUpdateDTO dto) {
//        UpmsMenu menu = getRouteById(dto.getId());
//        if (menu != null) {
//            this.update(new LambdaUpdateWrapper<UpmsMenu>()
//                    .likeRight(UpmsMenu::getLevelPath, menu.getLevelPath())
//                    .set(UpmsMenu::getStatus, dto.getStatus()));
//        }
//    }
//
//    @Override
//    public void modifyParent(MenuModifyParentDTO dto) {
//        UpmsMenu childMenu = getRouteById(dto.getId());
//        Assert.isTrue(childMenu != null, BizEnums.MENU_NOT_EXISTS);
//
//        UpmsMenu parentMenu = getRouteById(dto.getPid());
//        Assert.isTrue(parentMenu != null, BizEnums.PARENT_MENU_NOT_EXISTS);
//
//        UpmsMenu route = CglibUtil.copy(dto, UpmsMenu.class);
//        this.updateById(route);
//    }
//
//    private UpmsMenu getRouteById(Long id) {
//        LambdaQueryWrapper<UpmsMenu> queryWrapper = new LambdaQueryWrapper<UpmsMenu>()
//                .eq(UpmsMenu::getId, id);
//        return this.getOne(queryWrapper);
//    }
//
//    @Override
//    public UserMenusDTO getAllMenus() {
//        LambdaQueryWrapper<UpmsMenu> qw = new LambdaQueryWrapper<UpmsMenu>()
//                .orderByAsc(UpmsMenu::getLevel, UpmsMenu::getSequence);
//        return new UserMenusDTO(this.list(qw).stream()
//                .map(this::assembleUserRouteItem)
//                .collect(Collectors.toList())
//        );
//    }
//
//    private UserMenusDTO.UserRouteItem assembleUserRouteItem(UpmsMenu item) {
//        UserMenusDTO.UserRouteItem UserRouteItem = new UserMenusDTO.UserRouteItem();
//        UserRouteItem.setName(item.getCode());
//        UserRouteItem.setParentId(String.valueOf(item.getPid()));
//        UserRouteItem.setId(String.valueOf(item.getId()));
//        UserRouteItem.setMeta(assembleMeta(item));
////        UserRouteItem.setComponent(item.getComponent());
//        UserRouteItem.setRedirect("");
////        UserRouteItem.setPath(item.getPath());
//        return UserRouteItem;
//    }
//
//    private Meta assembleMeta(UpmsMenu item) {
//        Meta meta = new Meta();
//        meta.setIcon(item.getIcon());
//        meta.setTitle(item.getName());
//        meta.setHideChildren(item.getHideChildren());
//        meta.setShow(item.getStatus().equals(MenuStatusEnum.ENABLED.getValue()));
//        return meta;
//    }
//
//    @Override
//    public RouteTreeDTO getMenusTree() {
//        RouteTreeDTO dto = new RouteTreeDTO();
//        List<UpmsMenu> list = this.list();
//        if (CollectionUtil.isEmpty(list)) {
//            return dto;
//        }
//        List<UpmsMenu> levelOneMenus = list.stream().filter(item -> item.getPid().equals(DEFAULT_PID))
//                .collect(Collectors.toList());
//        List<UpmsMenu> anotherMenus = list.stream().filter(item -> !item.getPid().equals(DEFAULT_PID))
//                .collect(Collectors.toList());
//
//        List<RouteTreeDTO.TreeNode> resultSet = CollectionUtil.newArrayList();
//        for (UpmsMenu route : levelOneMenus) {
//            RouteTreeDTO.TreeNode item = CglibUtil.copy(route, RouteTreeDTO.TreeNode.class);
//            item.setChildren(CollectionUtil.newArrayList());
//            findChildren(item, anotherMenus);
//            resultSet.add(item);
//        }
//        dto.setRoutes(resultSet);
//        return dto;
//    }
//
//    private void findChildren(RouteTreeDTO.TreeNode parent, List<UpmsMenu> list) {
//        for (UpmsMenu route : list) {
//            if (parent.getId().equals(route.getPid())) {
//                RouteTreeDTO.TreeNode item = CglibUtil.copy(route, RouteTreeDTO.TreeNode.class);
//                item.setChildren(CollectionUtil.newArrayList());
//                parent.getChildren().add(item);
//                findChildren(item, list);
//            }
//        }
//    }
//
//    @Override
//    public MenuAnotherTreeDTO getMenusAnotherTree() {
//        MenuAnotherTreeDTO dto = new MenuAnotherTreeDTO();
//        List<UpmsMenu> list = this.list();
//        if (CollectionUtil.isEmpty(list)) {
//            return dto;
//        }
//        List<UpmsMenu> levelOneMenus = list.stream().filter(item -> item.getPid().equals(DEFAULT_PID))
//                .collect(Collectors.toList());
//        List<UpmsMenu> anotherMenus = list.stream().filter(item -> !item.getPid().equals(DEFAULT_PID))
//                .collect(Collectors.toList());
//
//        List<TreeNode> resultSet = CollectionUtil.newArrayList();
//        for (UpmsMenu route : levelOneMenus) {
//            TreeNode item = assembleAnotherTreeNo(route);
//            findAnotherTreeNodeChildren(item, anotherMenus);
//            resultSet.add(item);
//        }
//        dto.setRoutes(resultSet);
//        return dto;
//    }
//
//    private TreeNode assembleAnotherTreeNo(UpmsMenu route) {
//        TreeNode treeNode = new TreeNode();
//        treeNode.setKey(route.getCode());
//        treeNode.setTitle(route.getName());
//        treeNode.setIcon(route.getIcon());
//        treeNode.setChildren(new ArrayList<TreeNode>());
//        treeNode.setId(route.getId());
//        treeNode.setPid(route.getPid());
//        return treeNode;
//    }
//
//    private void findAnotherTreeNodeChildren(TreeNode parent, List<UpmsMenu> list) {
//        for (UpmsMenu route : list) {
//            if (parent.getId().equals(route.getPid())) {
//                TreeNode item = assembleAnotherTreeNo(route);
//                parent.getChildren().add(item);
//                findAnotherTreeNodeChildren(item, list);
//                if (!CollectionUtils.isEmpty(parent.getChildren())) {
//                    parent.setGroup(true);
//                }
//            }
//        }
//    }
//
//    private void updateStatus(MenuUpdateDTO dto, MenuStatusEnum statusEnum) {
//        this.update(new LambdaUpdateWrapper<UpmsMenu>()
//                .eq(UpmsMenu::getStatus, dto.getId())
//                .set(UpmsMenu::getStatus, statusEnum.getValue()));
//    }
//
//}
