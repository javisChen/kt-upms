//package com.kt.upms.api.controller;
//
//import cn.hutool.extra.cglib.CglibUtil;
//import com.kt.component.dto.PageRequest;
//import com.kt.component.dto.PageResponse;
//import com.kt.component.dto.ServerResponse;
//import com.kt.component.logger.CatchAndLog;
//import com.kt.component.web.base.BaseController;
//import com.kt.model.dto.menu.*;
//import com.kt.model.validgroup.UpmsValidateGroup;
//import com.kt.upms.entity.UpmsMenu;
//import com.kt.upms.service.IUpmsMenuService;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.groups.Default;
//
//
///**
// * <p>
// * 菜单表 前端控制器
// * </p>
// *
// * @author
// * @since 2020-11-09
// */
//@RestController
//@RequestMapping
//@CatchAndLog
//public class UpmsMenuController extends BaseController {
//
//
//    private final IUpmsMenuService iUpmsMenuService;
//
//    public UpmsMenuController(IUpmsMenuService iUpmsMenuService) {
//        this.iUpmsMenuService = iUpmsMenuService;
//    }
//
//    @PostMapping("/menus")
//    public ServerResponse<PageResponse<RouteQueryDTO>> list(@RequestBody PageRequest<RouteQueryDTO> pageRequest) {
//        return ServerResponse.ok(iUpmsMenuService.pageList(getPage(pageRequest), pageRequest.getParams()));
//    }
//
//    @PostMapping("/menu")
//    public ServerResponse add(@RequestBody @Validated MenuAddDTO dto) {
//        iUpmsMenuService.saveRoute(dto);
//        return ServerResponse.ok();
//    }
//
//    @PutMapping("/menu")
//    public ServerResponse update(@RequestBody @Validated MenuUpdateDTO dto) {
//        iUpmsMenuService.updateRoute(dto);
//        return ServerResponse.ok();
//    }
//
//    @PutMapping("/menu/parent")
//    public ServerResponse move(@RequestBody @Validated MenuModifyParentDTO dto) {
//        iUpmsMenuService.modifyParent(dto);
//        return ServerResponse.ok();
//    }
//
//    @GetMapping("/menus/{id}")
//    public ServerResponse get(@PathVariable("id") String id) {
//        UpmsMenu upmsMenu = iUpmsMenuService.getById(id);
//        if (upmsMenu == null) {
//            return ServerResponse.ok();
//        }
//        return ServerResponse.ok(CglibUtil.copy(upmsMenu, RouteQueryDTO.class));
//    }
//
//    @PutMapping("/menu/status")
//    public ServerResponse updateStatus(@Validated({UpmsValidateGroup.UpdateStatus.class, Default.class})
//                                       @RequestBody MenuUpdateDTO dto) {
//        iUpmsMenuService.updateRouteStatus(dto);
//        return ServerResponse.ok();
//    }
//
//    @PostMapping("/menu/tree")
//    public ServerResponse getRouteTree() {
//        return ServerResponse.ok(iUpmsMenuService.getMenusAnotherTree());
//    }
//
//    @PostMapping("/menus/init")
//    public ServerResponse init(@RequestBody UserMenusDTO userMenusDTO) {
//        for (UserMenusDTO.UserRouteItem menu : userMenusDTO.getMenus()) {
//            MenuAddDTO dto = new MenuAddDTO();
//            dto.setName(menu.getMeta().getTitle());
//            dto.setPid(Long.valueOf(menu.getParentId()));
//            dto.setCode(menu.getName());
//            dto.setComponent(menu.getComponent());
//            dto.setPath(menu.getPath());
//            dto.setIcon(menu.getMeta().getIcon());
//            iUpmsMenuService.saveRoute(dto);
//        }
//        return ServerResponse.ok();
//    }
//
//}
//
