package com.kt.upms.api.controller;


import cn.hutool.extra.cglib.CglibUtil;
import com.kt.component.dto.ServerResponse;
import com.kt.component.web.base.BaseController;
import com.kt.model.dto.page.PageQueryDTO;
import com.kt.model.dto.pageelement.PageElementAddDTO;
import com.kt.model.dto.pageelement.PageElementQueryDTO;
import com.kt.model.dto.pageelement.PageElementUpdateDTO;
import com.kt.model.vo.pageelement.PageElementListVO;
import com.kt.upms.entity.UpmsPageElement;
import com.kt.upms.service.IUpmsPageElementService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


/**
 * <p>
 * 页面元素表 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@RestController
@RequestMapping
public class UpmsPageElementController extends BaseController {

    private final IUpmsPageElementService iUpmsPageElementService;

    public UpmsPageElementController(IUpmsPageElementService iUpmsPageElementService) {
        this.iUpmsPageElementService = iUpmsPageElementService;
    }

    @PostMapping("/page-elements")
    public ServerResponse<List<PageElementListVO>> list(@RequestBody PageElementQueryDTO dto) {
        return ServerResponse.ok(iUpmsPageElementService.listPageElement(dto));
    }

    @PostMapping("/page-element")
    public ServerResponse add(@RequestBody @Validated PageElementAddDTO dto) {
        iUpmsPageElementService.savePageElement(dto);
        return ServerResponse.ok();
    }

    @PutMapping("/page-element")
    public ServerResponse update(@RequestBody @Validated PageElementUpdateDTO dto) {
        iUpmsPageElementService.updatePageElement(dto);
        return ServerResponse.ok();
    }

    @GetMapping("/page-element/{id}")
    public ServerResponse get(@PathVariable("id") String id) {
        UpmsPageElement page = Optional.ofNullable(iUpmsPageElementService.getById(id)).orElseGet(UpmsPageElement::new);
        return ServerResponse.ok(CglibUtil.copy(page, PageQueryDTO.class));
    }

    @DeleteMapping("/page-element")
    public ServerResponse removePageElement(@Validated @RequestBody PageElementUpdateDTO dto) {
        iUpmsPageElementService.removePageElement(dto);
        return ServerResponse.ok();
    }
}

