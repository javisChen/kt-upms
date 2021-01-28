package com.kt.upms.module.application.converter;

import com.kt.upms.module.application.persistence.UpmsApplication;
import com.kt.upms.module.api.service.IApiService;
import com.kt.upms.module.application.dto.ApplicationUpdateDTO;
import com.kt.upms.module.application.vo.ApplicationBaseVO;
import com.kt.upms.module.application.vo.ApplicationListVO;
import com.kt.upms.module.route.service.IRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Component
public class ApplicationBeanConverter {

    @Autowired
    private IApiService iApiService;

    @Autowired
    private IRouteService iRouteService;

    public ApplicationBaseVO convertForApplicationListVO(UpmsApplication application) {
        ApplicationListVO vo = new ApplicationListVO();
        Long applicationId = application.getId();
        vo.setId(applicationId);
        vo.setName(application.getName());
        vo.setCode(application.getCode());
        vo.setStatus(application.getStatus());
        vo.setType(application.getType());
        vo.setApiCount(iApiService.countByApplicationId(applicationId));
        vo.setRouteCount(iRouteService.countByApplicationId(applicationId));
        return vo;
    }

    public UpmsApplication convertForInsert(ApplicationUpdateDTO dto) {
        return convertForEntity(dto);
    }

    public UpmsApplication convertForUpdate(ApplicationUpdateDTO dto) {
        return convertForEntity(dto);
    }

    private UpmsApplication convertForEntity(ApplicationUpdateDTO dto) {
        UpmsApplication application = new UpmsApplication();
        application.setId(dto.getId());
        application.setName(dto.getName());
        application.setCode(dto.getCode());
        application.setStatus(dto.getStatus());
        application.setType(dto.getType());
        return application;
    }
}
