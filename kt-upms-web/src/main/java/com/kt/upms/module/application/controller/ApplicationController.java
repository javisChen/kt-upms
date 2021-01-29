package com.kt.upms.module.application.controller;


import com.kt.component.dto.MultiResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.validator.ValidateGroup;
import com.kt.component.web.base.BaseController;
import com.kt.upms.module.application.dto.ApplicationQueryDTO;
import com.kt.upms.module.application.dto.ApplicationUpdateDTO;
import com.kt.upms.module.application.service.IApplicationService;
import com.kt.upms.module.application.vo.ApplicationBaseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
@RestController
@RequestMapping
public class ApplicationController extends BaseController {

    @Autowired
    private IApplicationService iUpmsApplicationService;

    @PostMapping("/applications")
    public MultiResponse<ApplicationBaseVO> list(@RequestBody ApplicationQueryDTO dto) {
        return MultiResponse.ok(iUpmsApplicationService.listVos(dto));
    }

    @PostMapping("/application")
    public ServerResponse saveApplication(@Validated({ValidateGroup.Add.class, Default.class})
                                          @RequestBody ApplicationUpdateDTO dto) {
        iUpmsApplicationService.saveApplication(dto);
        return ServerResponse.ok();
    }

    @PutMapping("/application")
    public ServerResponse updateApplication(@Validated({ValidateGroup.Update.class, Default.class})
                                            @RequestBody ApplicationUpdateDTO dto) {
        iUpmsApplicationService.updateApplication(dto);
        return ServerResponse.ok();
    }

}

