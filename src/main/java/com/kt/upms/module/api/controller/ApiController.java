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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

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

    @GetMapping("/api/init")
    @Transactional(rollbackFor = Exception.class, timeout = 20000)
    public ServerResponse init() {
        Map<String, Integer> map = new HashMap<>(5);
        map.put("GET", 1);
        map.put("POST", 2);
        map.put("PUT", 3);
        map.put("DELETE", 4);
        map.put("PATCH", 5);
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : requestMappingHandlerMapping.getHandlerMethods().entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            ApiUpdateDTO dto = new ApiUpdateDTO();
            dto.setId(1L);
            dto.setApplicationId(1L);
            Set<String> patterns = requestMappingInfo.getPatternsCondition().getPatterns();
            String url = patterns.iterator().next();
            dto.setName(url);
            Set<RequestMethod> methods = requestMappingInfo.getMethodsCondition().getMethods();
            dto.setUrl(url);
            RequestMethod next = null;
            if (!methods.iterator().hasNext()) {
                continue;
            }
            next = methods.iterator().next();
            dto.setMethod(map.get(next.name()));
            dto.setAuthType(1);
            dto.setStatus(0);
            iApiService.saveApplication(dto);
        }
        return ServerResponse.ok();
    }
}

