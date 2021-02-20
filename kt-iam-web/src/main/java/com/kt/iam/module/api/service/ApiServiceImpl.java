package com.kt.iam.module.api.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.iam.module.api.persistence.IamApi;
import com.kt.iam.enums.BizEnums;
import com.kt.iam.enums.DeletedEnums;
import com.kt.iam.enums.PermissionTypeEnums;
import com.kt.iam.module.api.persistence.dao.IamApiMapper;
import com.kt.iam.module.api.converter.ApiBeanConverter;
import com.kt.iam.module.api.dto.ApiQueryDTO;
import com.kt.iam.module.api.dto.ApiUpdateDTO;
import com.kt.iam.module.api.vo.ApiDetailVO;
import com.kt.iam.module.api.vo.ApiListVO;
import com.kt.iam.module.permission.service.IPermissionService;
import com.kt.iam.common.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * api表 服务实现类
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
@Service
public class ApiServiceImpl extends ServiceImpl<IamApiMapper, IamApi> implements IApiService {

    @Autowired
    private ApiBeanConverter beanConverter;

    @Autowired
    private IPermissionService iPermissionService;

    @Override
    public Integer countByApplicationId(Long applicationId) {
        return this.count(new LambdaQueryWrapper<IamApi>().eq(IamApi::getApplicationId, applicationId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20000)
    public void saveApplication(ApiUpdateDTO dto) {
        IamApi api = getApiByApplicationIdAndMethodAndUrl(dto);
        Assert.isTrue(api != null, BizEnums.API_ALREADY_EXISTS);

        api = beanConverter.convertForUpdate(dto);
        this.save(api);

        iPermissionService.addPermission(api.getId(), PermissionTypeEnums.SER_API);
    }

    /**
     * 根据应用id+httpMethod+Url查询api记录，这三个字段保证一个api的唯一性
     */
    private IamApi getApiByApplicationIdAndMethodAndUrl(ApiUpdateDTO dto) {
        LambdaQueryWrapper<IamApi> qw = new LambdaQueryWrapper<>();
        qw.eq(IamApi::getUrl, dto.getUrl());
        qw.eq(IamApi::getMethod, dto.getMethod());
        qw.eq(IamApi::getApplicationId, dto.getApplicationId());
        qw.eq(IamApi::getIsDeleted, DeletedEnums.NOT.getCode());
        return this.getOne(qw);
    }

    @Override
    public void updateApi(ApiUpdateDTO dto) {
        IamApi api = getApiByApplicationIdAndMethodAndUrl(dto);
        Assert.isTrue(api != null && !api.getId().equals(dto.getId()), BizEnums.API_ALREADY_EXISTS);
        api = beanConverter.convertForUpdate(dto);
        this.updateById(api);
    }

    @Override
    public List<ApiListVO> listVos(ApiQueryDTO dto) {
        LambdaQueryWrapper<IamApi> qw = new LambdaQueryWrapper<>();
        qw.eq(IamApi::getApplicationId, dto.getApplicationId());
        qw.eq(IamApi::getCategoryId, dto.getCategoryId());
        qw.in(dto.getAuthType() != null, IamApi::getAuthType, dto.getAuthType());
        qw.eq(IamApi::getIsDeleted, DeletedEnums.NOT.getCode());
        return this.list(qw).stream().map(beanConverter::convertToApiListVO).collect(Collectors.toList());
    }

    @Override
    public void removeApi(Long id) {
        LambdaUpdateWrapper<IamApi> uw = new LambdaUpdateWrapper<>();
        uw.eq(IamApi::getId, id);
        uw.eq(IamApi::getIsDeleted, DeletedEnums.NOT.getCode());
        uw.set(IamApi::getIsDeleted, DeletedEnums.YET.getCode());
        this.update(uw);
    }

    @Override
    public List<IamApi> listAll() {
        LambdaQueryWrapper<IamApi> qw = new LambdaQueryWrapper<>();
        qw.eq(IamApi::getIsDeleted, DeletedEnums.NOT.getCode());
        return this.list(qw);
    }

    @Override
    public void removeByCategoryId(Long categoryId) {
        LambdaUpdateWrapper<IamApi> uw = new LambdaUpdateWrapper<>();
        uw.eq(IamApi::getCategoryId, categoryId);
        uw.eq(IamApi::getIsDeleted, DeletedEnums.NOT.getCode());
        uw.set(IamApi::getIsDeleted, DeletedEnums.YET.getCode());
        this.update(uw);
    }

    @Override
    public ApiDetailVO getApplicationVO(Long id) {
        IamApi iamApi = getById(id);
        return beanConverter.convertToApiDetailVO(iamApi);
    }
}
