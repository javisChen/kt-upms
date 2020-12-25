//package com.kt.upms.service;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.IService;
//import com.kt.component.dto.PageResponse;
//import com.kt.model.dto.menu.*;
//import com.kt.upms.entity.UpmsMenu;
//
///**
// * <p>
// * 菜单表 服务类
// * </p>
// *
// * @author
// * @since 2020-11-09
// */
//public interface IUpmsMenuService extends IService<UpmsMenu> {
//
//    PageResponse<UpmsMenu> pageList(Page page, RouteQueryDTO params);
//
//    void saveRoute(MenuAddDTO dto);
//
//    void updateRoute(MenuUpdateDTO dto);
//
//    void updateRouteStatus(MenuUpdateDTO dto);
//
//    void modifyParent(MenuModifyParentDTO dto);
//
//    UserMenusDTO getAllMenus();
//
//    RouteTreeDTO getMenusTree();
//
//    MenuAnotherTreeDTO getMenusAnotherTree();
//}
