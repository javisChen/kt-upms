package com.kt.iam.module.application.controller;


import com.kt.component.dto.MultiResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.validator.ValidateGroup;
import com.kt.component.web.base.BaseController;
import com.kt.iam.module.application.dto.ApplicationQueryDTO;
import com.kt.iam.module.application.dto.ApplicationUpdateDTO;
import com.kt.iam.module.application.service.IApplicationService;
import com.kt.iam.module.application.vo.ApplicationBaseVO;
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
    private IApplicationService iApplicationService;

    @PostMapping("/applications")
    public MultiResponse<ApplicationBaseVO> list(@RequestBody ApplicationQueryDTO dto) {
        return MultiResponse.ok(iApplicationService.listVos(dto));
    }

    @PostMapping("/application")
    public ServerResponse saveApplication(@Validated({ValidateGroup.Add.class, Default.class})
                                          @RequestBody ApplicationUpdateDTO dto) {
        iApplicationService.saveApplication(dto);
        return ServerResponse.ok();
    }

    @PutMapping("/application")
    public ServerResponse updateApplication(@Validated({ValidateGroup.Update.class, Default.class})
                                            @RequestBody ApplicationUpdateDTO dto) {
        iApplicationService.updateApplication(dto);
        return ServerResponse.ok();
    }

}

