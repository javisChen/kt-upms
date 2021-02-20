package com.kt.iam.module.api.converter;

import com.kt.iam.module.api.persistence.IamApi;
import com.kt.iam.module.api.persistence.IamApiCategory;
import com.kt.iam.module.api.vo.ApiDetailVO;
import com.kt.iam.module.permission.persistence.IamPermission;
import com.kt.iam.enums.PermissionTypeEnums;
import com.kt.iam.module.api.dto.ApiCategoryUpdateDTO;
import com.kt.iam.module.api.dto.ApiUpdateDTO;
import com.kt.iam.module.api.vo.ApiCategoryBaseVO;
import com.kt.iam.module.api.vo.ApiListVO;
import com.kt.iam.module.permission.service.IPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Component
public class ApiBeanConverter {

    @Autowired
    private IPermissionService iPermissionService;

    public IamApi convertForUpdate(ApiUpdateDTO dto) {
        IamApi iamApi = new IamApi();
        iamApi.setId(dto.getId());
        iamApi.setName(dto.getName());
        iamApi.setApplicationId(dto.getApplicationId());
        iamApi.setUrl(dto.getUrl());
        iamApi.setMethod(dto.getMethod());
        iamApi.setAuthType(dto.getAuthType());
        iamApi.setStatus(dto.getStatus());
        iamApi.setCategoryId(dto.getCategoryId());
        // 约定规则，如果url包含了*号就代表是包含了路径参数
        iamApi.setHasPathVariable(StringUtils.contains(dto.getUrl(), "*"));
        return iamApi;
    }

    public ApiListVO convertToApiListVO(IamApi iamApi) {
        IamPermission permission = iPermissionService.getPermission(iamApi.getId(), PermissionTypeEnums.SER_API);
        ApiListVO apiListVO = new ApiListVO();
        apiListVO.setId(iamApi.getId());
        apiListVO.setName(iamApi.getName());
        apiListVO.setUrl(iamApi.getUrl());
        apiListVO.setMethod(iamApi.getMethod());
        apiListVO.setAuthType(iamApi.getAuthType());
        apiListVO.setStatus(iamApi.getStatus());
        apiListVO.setPermissionId(permission.getId());
        apiListVO.setPermissionCode(permission.getCode());
        apiListVO.setCreateTime(iamApi.getGmtCreate());
        apiListVO.setUpdateTime(iamApi.getGmtModified());
        return apiListVO;
    }

    public ApiCategoryBaseVO convertToApiCategoryVO(IamApiCategory category) {
        ApiCategoryBaseVO apiCategoryBaseVO = new ApiCategoryBaseVO();
        apiCategoryBaseVO.setId(category.getId());
        apiCategoryBaseVO.setName(category.getName());
        apiCategoryBaseVO.setApplicationId(category.getApplicationId());
        return apiCategoryBaseVO;
    }

    public IamApiCategory convertToDO(ApiCategoryUpdateDTO dto) {
        IamApiCategory iamApiCategory = new IamApiCategory();
        iamApiCategory.setId(dto.getId());
        iamApiCategory.setName(dto.getName());
        iamApiCategory.setApplicationId(dto.getApplicationId());
        return iamApiCategory;
    }

    public ApiDetailVO convertToApiDetailVO(IamApi iamApi) {
        ApiDetailVO vo = new ApiDetailVO();
        vo.setApplicationId(iamApi.getApplicationId());
        vo.setCategoryId(iamApi.getCategoryId());
        vo.setId(iamApi.getId());
        vo.setName(iamApi.getName());
        vo.setUrl(iamApi.getUrl());
        vo.setMethod(iamApi.getMethod());
        vo.setAuthType(iamApi.getAuthType());
        vo.setStatus(iamApi.getStatus());
        return vo;
    }
}
