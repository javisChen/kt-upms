package com.kt.upms.api.controller;


import cn.hutool.extra.cglib.CglibUtil;
import com.kt.component.dto.PageRequest;
import com.kt.component.dto.PageResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.web.base.BaseController;
import com.kt.model.dto.route.RouteQueryDTO;
import com.kt.model.dto.page.PageAddDTO;
import com.kt.model.dto.page.PageQueryDTO;
import com.kt.model.dto.page.PageUpdateDTO;
import com.kt.model.validgroup.UpmsValidateGroup;
import com.kt.upms.entity.UpmsPage;
import com.kt.upms.service.IUpmsPageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

/**
 * <p>
 * 页面表 前端控制器
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
@RestController
@RequestMapping
public class UpmsPageController extends BaseController {

    private final IUpmsPageService iUpmsPageService;

    public UpmsPageController(IUpmsPageService iUpmsPageService) {
        this.iUpmsPageService = iUpmsPageService;
    }

    @PostMapping("/pages")
    public ServerResponse<PageResponse<RouteQueryDTO>> list(@RequestBody PageRequest<PageQueryDTO> pageRequest) {
        return ServerResponse.ok(iUpmsPageService.pageList(getPage(pageRequest), pageRequest.getParams()));
    }

    @PostMapping("/page")
    public ServerResponse add(@RequestBody @Validated PageAddDTO dto) {
        return ServerResponse.ok(iUpmsPageService.savePage(dto));
    }

    @PutMapping("/page")
    public ServerResponse update(@RequestBody @Validated PageUpdateDTO dto) {
        iUpmsPageService.updatePage(dto);
        return ServerResponse.ok();
    }

    @GetMapping("/page/{id}")
    public ServerResponse get(@PathVariable("id") String id) {
        UpmsPage page = iUpmsPageService.getById(id);
        if (page == null) {
            return ServerResponse.ok();
        }
        return ServerResponse.ok(CglibUtil.copy(page, PageQueryDTO.class));
    }

    @PutMapping("/page/status")
    public ServerResponse updateStatus(@Validated({UpmsValidateGroup.UpdateStatus.class, Default.class})
                                       @RequestBody PageUpdateDTO dto) {
        iUpmsPageService.updateStatus(dto);
        return ServerResponse.ok();
    }

}

