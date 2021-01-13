package com.kt.upms.module.route.controller;


import com.kt.component.dto.MultiResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.web.base.BaseController;
import com.kt.upms.module.route.dto.PageElementAddDTO;
import com.kt.upms.module.route.dto.PageElementQueryDTO;
import com.kt.upms.module.route.dto.PageElementUpdateDTO;
import com.kt.upms.module.route.vo.PageElementVO;
import com.kt.upms.module.route.service.IUpmsPageElementService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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
    public MultiResponse<PageElementVO> list(@RequestBody PageElementQueryDTO dto) {
        return MultiResponse.ok(iUpmsPageElementService.listElementVO(dto));
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

    @DeleteMapping("/page-element")
    public ServerResponse removePageElement(@Validated @RequestBody PageElementUpdateDTO dto) {
        iUpmsPageElementService.removePageElement(dto);
        return ServerResponse.ok();
    }
}

