package com.kt.upms.module.route.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kt.component.dto.MultiResponse;
import com.kt.component.dto.PageResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.dto.SingleResponse;
import com.kt.component.logger.CatchAndLog;
import com.kt.component.web.base.BaseController;
import com.kt.upms.module.route.dto.RouteModifyParentDTO;
import com.kt.upms.module.route.dto.RouteQueryDTO;
import com.kt.upms.module.route.dto.RouteUpdateDTO;
import com.kt.upms.module.route.dto.UserRoutesDTO;
import com.kt.upms.module.route.service.IUpmsRouteService;
import com.kt.upms.module.route.vo.RouteDetailVO;
import com.kt.upms.module.route.vo.RouteElementVO;
import com.kt.upms.module.route.vo.RouteListTreeVO;
import com.kt.upms.validgroup.UpmsValidateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;


/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
@RestController
@RequestMapping
@CatchAndLog
public class UpmsRouteController extends BaseController {

    @Autowired
    private IUpmsRouteService iUpmsRouteService;

    @PostMapping("/routes")
    public SingleResponse<PageResponse<RouteListTreeVO>> list(@RequestBody RouteQueryDTO dto) {
        Page<RouteListTreeVO> routeListTreeVOPage = iUpmsRouteService.pageList(dto);
        return SingleResponse.ok(PageResponse.build(routeListTreeVOPage));
    }

    @PostMapping("/routes/tree")
    public MultiResponse<RouteListTreeVO> getTree() {
        return MultiResponse.ok(iUpmsRouteService.getTree());
    }

    @PostMapping("/route")
    public ServerResponse add(@RequestBody @Validated RouteUpdateDTO dto) {
        iUpmsRouteService.saveRoute(dto);
        return ServerResponse.ok();
    }

    @PutMapping("/route")
    public ServerResponse update(@RequestBody @Validated RouteUpdateDTO dto) {
        iUpmsRouteService.updateRoute(dto);
        return ServerResponse.ok();
    }

    @PutMapping("/route/parent")
    public ServerResponse move(@RequestBody @Validated RouteModifyParentDTO dto) {
        iUpmsRouteService.modifyParent(dto);
        return ServerResponse.ok();
    }

    @GetMapping("/route/{id}")
    public SingleResponse<RouteDetailVO> get(@PathVariable("id") String id) {
        RouteDetailVO vo = iUpmsRouteService.getRoute(Long.valueOf(id));
        return SingleResponse.ok(vo);
    }

    @PutMapping("/route/status")
    public ServerResponse updateStatus(@Validated({UpmsValidateGroup.UpdateStatus.class, Default.class})
                                       @RequestBody RouteUpdateDTO dto) {
        iUpmsRouteService.updateRouteStatus(dto);
        return ServerResponse.ok();
    }

    @DeleteMapping("/route/{id}")
    public ServerResponse deleteRoute(@PathVariable String id) {
        iUpmsRouteService.deleteRouteById(Long.valueOf(id));
        return ServerResponse.ok();
    }

    @GetMapping("/route/{routeId}/elements")
    public MultiResponse<RouteElementVO> getRouteElements(@PathVariable Long routeId) {
        return MultiResponse.ok(iUpmsRouteService.listRouteElementsById(routeId));
    }

    @PostMapping("/routes/init")
    public ServerResponse init(@RequestBody UserRoutesDTO userMenusDTO) {
        for (UserRoutesDTO.UserRouteItem menu : userMenusDTO.getRoutes()) {
            RouteUpdateDTO dto = new RouteUpdateDTO();
            dto.setName(menu.getMeta().getTitle());
            dto.setPid(Long.valueOf(menu.getParentId()));
            dto.setCode(menu.getName());
            dto.setComponent(menu.getComponent());
            dto.setPath(menu.getPath());
            dto.setIcon(menu.getMeta().getIcon());
            iUpmsRouteService.saveRoute(dto);
        }
        return ServerResponse.ok();
    }
}

