package com.kt.upms.module.api.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.upms.entity.UpmsApi;
import com.kt.upms.enums.ApiAuthTypeEnums;
import com.kt.upms.enums.BizEnums;
import com.kt.upms.enums.DeletedEnums;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.mapper.UpmsApiMapper;
import com.kt.upms.module.api.converter.ApiBeanConverter;
import com.kt.upms.module.api.dto.ApiQueryDTO;
import com.kt.upms.module.api.dto.ApiUpdateDTO;
import com.kt.upms.module.api.vo.ApiListVO;
import com.kt.upms.module.permission.service.IPermissionService;
import com.kt.upms.util.Assert;
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
public class ApiServiceImpl extends ServiceImpl<UpmsApiMapper, UpmsApi> implements IApiService {

    @Autowired
    private ApiBeanConverter beanConverter;

    @Autowired
    private IPermissionService iPermissionService;

    @Override
    public Integer countByApplicationId(Long applicationId) {
        return this.count(new LambdaQueryWrapper<UpmsApi>().eq(UpmsApi::getApplicationId, applicationId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20000)
    public void saveApplication(ApiUpdateDTO dto) {
        UpmsApi api = getApiByApplicationIdAndMethodAndUrl(dto);
        Assert.isTrue(api != null, BizEnums.API_ALREADY_EXISTS);

        api = beanConverter.convertForUpdate(dto);
        this.save(api);

        iPermissionService.addPermission(api.getId(), PermissionTypeEnums.SER_API);
    }

    /**
     * 根据应用id+httpMethod+Url查询api记录，这三个字段保证一个api的唯一性
     */
    private UpmsApi getApiByApplicationIdAndMethodAndUrl(ApiUpdateDTO dto) {
        LambdaQueryWrapper<UpmsApi> qw = new LambdaQueryWrapper<>();
        qw.eq(UpmsApi::getUrl, dto.getUrl());
        qw.eq(UpmsApi::getMethod, dto.getMethod());
        qw.eq(UpmsApi::getApplicationId, dto.getApplicationId());
        qw.eq(UpmsApi::getIsDeleted, DeletedEnums.NOT.getCode());
        return this.getOne(qw);
    }

    @Override
    public void updateApi(ApiUpdateDTO dto) {
        UpmsApi api = getApiByApplicationIdAndMethodAndUrl(dto);
        Assert.isTrue(api != null && !api.getId().equals(dto.getId()), BizEnums.API_ALREADY_EXISTS);
        api = beanConverter.convertForUpdate(dto);
        this.updateById(api);
    }

    @Override
    public List<ApiListVO> listVos(ApiQueryDTO dto) {
        LambdaQueryWrapper<UpmsApi> qw = new LambdaQueryWrapper<>();
        qw.eq(UpmsApi::getApplicationId, dto.getApplicationId());
        qw.in(dto.getAuthType() != null, UpmsApi::getAuthType, dto.getAuthType());
        qw.eq(UpmsApi::getIsDeleted, DeletedEnums.NOT.getCode());
        return this.list(qw).stream().map(beanConverter::convertToApiListVO).collect(Collectors.toList());
    }

    @Override
    public void removeApi(Long id) {
        LambdaUpdateWrapper<UpmsApi> uw = new LambdaUpdateWrapper<>();
        uw.eq(UpmsApi::getId, id);
        uw.eq(UpmsApi::getIsDeleted, DeletedEnums.NOT.getCode());
        uw.set(UpmsApi::getIsDeleted, DeletedEnums.YET.getCode());
        this.update(uw);
    }

    @Override
    public List<UpmsApi> getNoNeedAuthorizationApis() {
        LambdaQueryWrapper<UpmsApi> qw = new LambdaQueryWrapper<>();
        qw.eq(UpmsApi::getIsDeleted, DeletedEnums.NOT.getCode());
        qw.and(wrapper -> wrapper.eq(UpmsApi::getAuthType, ApiAuthTypeEnums.NEED_AUTHENTICATION.getValue())
                .or()
                .eq(UpmsApi::getAuthType, ApiAuthTypeEnums.NO_AUTHENTICATION_AND_AUTHORIZATION.getValue())
        );
        return this.list(qw);
    }

    @Override
    public List<UpmsApi> getNoNeedAuthenticationApis() {
        LambdaQueryWrapper<UpmsApi> qw = new LambdaQueryWrapper<>();
        qw.eq(UpmsApi::getIsDeleted, DeletedEnums.NOT.getCode());
        qw.eq(UpmsApi::getAuthType, ApiAuthTypeEnums.NO_AUTHENTICATION_AND_AUTHORIZATION.getValue());
        return this.list(qw);
    }

    @Override
    public List<UpmsApi> listAll() {
        LambdaQueryWrapper<UpmsApi> qw = new LambdaQueryWrapper<>();
        qw.eq(UpmsApi::getIsDeleted, DeletedEnums.NOT.getCode());
        return this.list(qw);
    }
}
