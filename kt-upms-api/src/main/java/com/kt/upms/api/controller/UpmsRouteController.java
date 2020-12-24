package com.kt.upms.api.controller;

import cn.hutool.extra.cglib.CglibUtil;
import com.kt.component.dto.PageRequest;
import com.kt.component.dto.PageResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.logger.CatchAndLog;
import com.kt.component.web.base.BaseController;
import com.kt.model.dto.menu.*;
import com.kt.model.validgroup.UpmsValidateGroup;
import com.kt.upms.entity.UpmsRoute;
import com.kt.upms.service.IUpmsRouteService;
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


    private final IUpmsRouteService iUpmsRouteService;

    public UpmsRouteController(IUpmsRouteService iUpmsRouteService) {
        this.iUpmsRouteService = iUpmsRouteService;
    }

    @PostMapping("/routes")
    public ServerResponse<PageResponse<RouteQueryDTO>> list(@RequestBody PageRequest<RouteQueryDTO> pageRequest) {
        return ServerResponse.ok(iUpmsRouteService.pageList(getPage(pageRequest), pageRequest.getParams()));
    }

    @PostMapping("/route")
    public ServerResponse add(@RequestBody @Validated RouteAddDTO dto) {
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

    @GetMapping("/routes/{id}")
    public ServerResponse get(@PathVariable("id") String id) {
        UpmsRoute upmsMenu = iUpmsRouteService.getById(id);
        if (upmsMenu == null) {
            return ServerResponse.ok();
        }
        return ServerResponse.ok(CglibUtil.copy(upmsMenu, RouteQueryDTO.class));
    }

    @PutMapping("/route/status")
    public ServerResponse updateStatus(@Validated({UpmsValidateGroup.UpdateStatus.class, Default.class})
                                       @RequestBody RouteUpdateDTO dto) {
        iUpmsRouteService.updateRouteStatus(dto);
        return ServerResponse.ok();
    }

    @PostMapping("/routes/init")
    public ServerResponse init(@RequestBody UserRoutesDTO userMenusDTO) {
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

