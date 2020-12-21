package com.kt.upms.api.controller;

import cn.hutool.extra.cglib.CglibUtil;
import com.kt.component.dto.PageRequest;
import com.kt.component.dto.PageResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.logger.CatchAndLog;
import com.kt.component.web.base.BaseController;
import com.kt.model.dto.menu.*;
import com.kt.model.validgroup.UpmsValidateGroup;
import com.kt.upms.entity.UpmsMenu;
import com.kt.upms.service.IUpmsMenuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
public class UpmsMenuController extends BaseController {


    private final IUpmsMenuService iUpmsMenuService;

    public UpmsMenuController(IUpmsMenuService iUpmsMenuService) {
        this.iUpmsMenuService = iUpmsMenuService;
    }

    @PostMapping("/menus")
    public ServerResponse<PageResponse<MenuQueryDTO>> list(@RequestBody PageRequest<MenuQueryDTO> pageRequest) {
        return ServerResponse.ok(iUpmsMenuService.pageList(getPage(pageRequest), pageRequest.getParams()));
    }

    @PostMapping("/menu")
    public ServerResponse add(@RequestBody @Validated MenuAddDTO dto) {
        return ServerResponse.ok(iUpmsMenuService.saveMenu(dto));
    }

    @PutMapping("/menu")
    public ServerResponse update(@RequestBody @Validated MenuUpdateDTO dto) {
        iUpmsMenuService.updateMenu(dto);
        return ServerResponse.ok();
    }

    @PutMapping("/menu/parent")
    public ServerResponse move(@RequestBody @Validated MenuModifyParentDTO dto) {
        iUpmsMenuService.modifyParent(dto);
        return ServerResponse.ok();
    }

    @GetMapping("/menus/{id}")
    public ServerResponse get(@PathVariable("id") String id) {
        UpmsMenu upmsMenu = iUpmsMenuService.getById(id);
        if (upmsMenu == null) {
            return ServerResponse.ok();
        }
        return ServerResponse.ok(CglibUtil.copy(upmsMenu, MenuQueryDTO.class));
    }

    @PutMapping("/menu/status")
    public ServerResponse updateStatus(@Validated({UpmsValidateGroup.UpdateStatus.class, Default.class})
                                       @RequestBody MenuUpdateDTO dto) {
        iUpmsMenuService.updateMenuStatus(dto);
        return ServerResponse.ok();
    }

    @PostMapping("/menus/tree")
    public ServerResponse allMenu() {
        MenuAllDTO menuAllDTO = iUpmsMenuService.getAllMenus();
        return ServerResponse.ok(menuAllDTO);
    }

    @PostMapping("/menus/test/demo")
    public ServerResponse test(List<MultipartFile> file, Integer id) {
        return ServerResponse.ok();
    }
}

