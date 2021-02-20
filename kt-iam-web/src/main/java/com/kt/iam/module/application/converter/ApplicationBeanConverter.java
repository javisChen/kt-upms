package com.kt.iam.module.application.converter;

import com.kt.iam.module.application.persistence.IamApplication;
import com.kt.iam.module.api.service.IApiService;
import com.kt.iam.module.application.dto.ApplicationUpdateDTO;
import com.kt.iam.module.application.vo.ApplicationBaseVO;
import com.kt.iam.module.application.vo.ApplicationListVO;
import com.kt.iam.module.route.service.IRouteService;
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

    public ApplicationBaseVO convertForApplicationListVO(IamApplication application) {
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

    public IamApplication convertForInsert(ApplicationUpdateDTO dto) {
        return convertForEntity(dto);
    }

    public IamApplication convertForUpdate(ApplicationUpdateDTO dto) {
        return convertForEntity(dto);
    }

    private IamApplication convertForEntity(ApplicationUpdateDTO dto) {
        IamApplication application = new IamApplication();
        application.setId(dto.getId());
        application.setName(dto.getName());
        application.setCode(dto.getCode());
        application.setStatus(dto.getStatus());
        application.setType(dto.getType());
        return application;
    }
}
