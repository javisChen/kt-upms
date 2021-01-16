package com.kt.upms.module.api.controller;


import com.kt.component.dto.MultiResponse;
import com.kt.component.dto.ServerResponse;
import com.kt.component.validator.ValidateGroup;
import com.kt.component.web.base.BaseController;
import com.kt.upms.module.api.dto.ApiQueryDTO;
import com.kt.upms.module.api.dto.ApiUpdateDTO;
import com.kt.upms.module.api.service.IApiService;
import com.kt.upms.module.api.vo.ApiListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

/**
 * <p>
 * api表 前端控制器
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
@RestController
@RequestMapping
public class ApiController extends BaseController {

    @Autowired
    private IApiService iApiService;

    @PostMapping("/apis")
    public MultiResponse<ApiListVO> list(@RequestBody ApiQueryDTO dto) {
        return MultiResponse.ok(iApiService.listVos(dto));
    }

    @PostMapping("/api")
    public ServerResponse saveApi(@Validated({ValidateGroup.Add.class, Default.class})
                                  @RequestBody ApiUpdateDTO dto) {
        iApiService.saveApplication(dto);
        return ServerResponse.ok();
    }

    @PutMapping("/api")
    public ServerResponse updateApi(@Validated({ValidateGroup.Update.class, Default.class})
                                    @RequestBody ApiUpdateDTO dto) {
        iApiService.updateApi(dto);
        return ServerResponse.ok();
    }

    @DeleteMapping("/api/{id}")
    public ServerResponse deleteApi(@PathVariable Long id) {
        iApiService.removeApi(id);
        return ServerResponse.ok();
    }
}

