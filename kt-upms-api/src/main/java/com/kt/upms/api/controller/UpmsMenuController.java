package com.kt.upms.api.controller;


import cn.hutool.extra.cglib.CglibUtil;
import com.kt.component.dto.PageRequest;
import com.kt.component.dto.PageResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.web.base.BaseController;
import com.kt.model.dto.menu.MenuAddDTO;
import com.kt.model.dto.menu.MenuQueryDTO;
import com.kt.model.dto.menu.MenuUpdateDTO;
import com.kt.upms.entity.UpmsMenu;
import com.kt.upms.service.IUpmsMenuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@RestController
@RequestMapping("/menu")
public class UpmsMenuController extends BaseController {


    private final IUpmsMenuService iUpmsMenuService;

    public UpmsMenuController(IUpmsMenuService iUpmsMenuService) {
        this.iUpmsMenuService = iUpmsMenuService;
    }

    @PostMapping("/list")
    public ServerResponse<PageResponse<MenuQueryDTO>> list(@RequestBody PageRequest<MenuQueryDTO> pageRequest) {
        return ServerResponse.ok(iUpmsMenuService.pageList(getPage(pageRequest), pageRequest.getParams()));
    }

    @PostMapping("/add")
    public ServerResponse add(@RequestBody @Validated MenuAddDTO dto) {
        return ServerResponse.ok(iUpmsMenuService.saveMenu(dto));
    }

    @PostMapping("/update")
    public ServerResponse update(@RequestBody @Validated MenuUpdateDTO dto) {
        iUpmsMenuService.updateMenu(dto);
        return ServerResponse.ok();
    }

    @GetMapping("/{id}")
    public ServerResponse get(@PathVariable("id") String id) {
        UpmsMenu upmsMenu = iUpmsMenuService.getById(id);
        if (upmsMenu == null) {
            return ServerResponse.ok();
        }
        return ServerResponse.ok(CglibUtil.copy(upmsMenu, MenuQueryDTO.class));
    }

    @PostMapping("/disable")
    public ServerResponse disable(@RequestBody @Validated MenuUpdateDTO dto) {
        iUpmsMenuService.disableMenu(dto);
        return ServerResponse.ok();
    }

    @PostMapping("/enable")
    public ServerResponse enable(@RequestBody @Validated MenuUpdateDTO dto) {
        iUpmsMenuService.enableMenu(dto);
        return ServerResponse.ok();
    }
}

