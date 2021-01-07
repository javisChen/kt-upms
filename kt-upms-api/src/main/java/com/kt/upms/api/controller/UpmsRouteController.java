package com.kt.upms.api.controller;

import com.kt.component.dto.PageResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.logger.CatchAndLog;
import com.kt.component.web.base.BaseController;
import com.kt.model.dto.route.*;
import com.kt.model.validgroup.UpmsValidateGroup;
import com.kt.model.vo.route.RouteDetailVO;
import com.kt.model.vo.route.RouteListTreeVO;
import com.kt.upms.service.IUpmsRouteService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;


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

    private final IUpmsRouteService iUpmsRouteService;

    public UpmsRouteController(IUpmsRouteService iUpmsRouteService) {
        this.iUpmsRouteService = iUpmsRouteService;
    }

    @PostMapping("/routes")
    public ServerResponse<PageResponse<RouteListTreeVO>> list(@RequestBody RouteQueryDTO dto) {
        return ServerResponse.ok(iUpmsRouteService.pageList(dto));
    }

    @PostMapping("/routes/tree")
    public ServerResponse<List<RouteListTreeVO>> getTree() {
        return ServerResponse.ok(iUpmsRouteService.getTree());
    }

    @PostMapping("/route")
    public ServerResponse<Object> add(@RequestBody @Validated RouteAddDTO dto) {
        iUpmsRouteService.saveRoute(dto);
        return ServerResponse.ok();
    }

    @PutMapping("/route")
    public ServerResponse<Object> update(@RequestBody @Validated RouteUpdateDTO dto) {
        iUpmsRouteService.updateRoute(dto);
        return ServerResponse.ok();
    }

    @PutMapping("/route/parent")
    public ServerResponse<Object> move(@RequestBody @Validated RouteModifyParentDTO dto) {
        iUpmsRouteService.modifyParent(dto);
        return ServerResponse.ok();
    }

    @GetMapping("/route/{id}")
    public ServerResponse<RouteDetailVO> get(@PathVariable("id") String id) {
        RouteDetailVO vo = iUpmsRouteService.getRoute(Long.valueOf(id));
        return ServerResponse.ok(vo);
    }

    @PutMapping("/route/status")
    public ServerResponse<Object> updateStatus(@Validated({UpmsValidateGroup.UpdateStatus.class, Default.class})
                                               @RequestBody RouteUpdateDTO dto) {
        iUpmsRouteService.updateRouteStatus(dto);
        return ServerResponse.ok();
    }

    @DeleteMapping("/route/{id}")
    public ServerResponse<Object> deleteRoute(@PathVariable String id) {
        iUpmsRouteService.deleteRouteById(Long.valueOf(id));
        return ServerResponse.ok();
    }

    @PostMapping("/routes/init")
    public ServerResponse<Object> init(@RequestBody UserRoutesDTO userMenusDTO) {
        for (UserRoutesDTO.UserRouteItem menu : userMenusDTO.getRoutes()) {
            RouteAddDTO dto = new RouteAddDTO();
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

