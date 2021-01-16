package com.kt.upms.module.api.converter;

import com.kt.upms.entity.UpmsApi;
import com.kt.upms.module.api.dto.ApiUpdateDTO;
import com.kt.upms.module.api.vo.ApiListVO;
import org.springframework.stereotype.Component;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Component
public class ApiBeanConverter {

    public UpmsApi convertForUpdate(ApiUpdateDTO dto) {
        UpmsApi upmsApi = new UpmsApi();
        upmsApi.setId(dto.getId());
        upmsApi.setName(dto.getName());
        upmsApi.setApplicationId(dto.getApplicationId());
        upmsApi.setUrl(dto.getUrl());
        upmsApi.setMethod(dto.getMethod());
        upmsApi.setAuthType(dto.getAuthType());
        upmsApi.setStatus(dto.getStatus());
        return upmsApi;
    }

    public ApiListVO convertToApiListVO(UpmsApi upmsApi) {
        ApiListVO apiListVO = new ApiListVO();
        apiListVO.setId(upmsApi.getId());
        apiListVO.setName(upmsApi.getName());
        apiListVO.setApplicationId(upmsApi.getApplicationId());
        apiListVO.setUrl(upmsApi.getUrl());
        apiListVO.setMethod(upmsApi.getMethod());
        apiListVO.setAuthType(upmsApi.getAuthType());
        apiListVO.setStatus(upmsApi.getStatus());
        apiListVO.setCreateTime(upmsApi.getGmtCreate());
        apiListVO.setUpdateTime(upmsApi.getGmtModified());
        return apiListVO;
    }
}
