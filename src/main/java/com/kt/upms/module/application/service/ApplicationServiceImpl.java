package com.kt.upms.module.application.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.upms.entity.UpmsApplication;
import com.kt.upms.enums.BizEnums;
import com.kt.upms.enums.DeletedEnums;
import com.kt.upms.mapper.UpmsApplicationMapper;
import com.kt.upms.module.application.converter.ApplicationBeanConverter;
import com.kt.upms.module.application.dto.ApplicationQueryDTO;
import com.kt.upms.module.application.dto.ApplicationUpdateDTO;
import com.kt.upms.module.application.vo.ApplicationBaseVO;
import com.kt.upms.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Service
public class ApplicationServiceImpl extends ServiceImpl<UpmsApplicationMapper, UpmsApplication>
        implements IApplicationService {

    @Autowired
    private ApplicationBeanConverter beanConverter;

    @Override
    public void saveApplication(ApplicationUpdateDTO dto) {
        checkBeforeSave(dto);

        UpmsApplication application = beanConverter.convertForInsert(dto);

        this.save(application);
    }

    @Override
    public void updateApplication(ApplicationUpdateDTO dto) {
        checkBeforeUpdate(dto);

        UpmsApplication application = beanConverter.convertForUpdate(dto);

        this.updateById(application);
    }

    @Override
    public List<ApplicationBaseVO> listVos(ApplicationQueryDTO dto) {
        return this.list().stream().map(beanConverter::convertForApplicationListVO).collect(Collectors.toList());
    }

    @Override
    public String getNameById(Long applicationId) {
        final LambdaQueryWrapper<UpmsApplication> qw = new LambdaQueryWrapper<>();
        qw.select(UpmsApplication::getName);
        qw.eq(UpmsApplication::getId, applicationId);
        qw.eq(UpmsApplication::getIsDeleted, DeletedEnums.NOT.getCode());
        UpmsApplication application = Optional.ofNullable(this.getOne(qw)).orElseGet(UpmsApplication::new);
        return application.getName();
    }

    private void checkBeforeSave(ApplicationUpdateDTO dto) {
        UpmsApplication  application = getApplicationByName(dto.getName());
        Assert.isTrue(application != null, BizEnums.APPLICATION_NAME_ALREADY_EXISTS);
        application = getApplicationByCode(dto.getCode());
        Assert.isTrue(application != null, BizEnums.APPLICATION_CODE_ALREADY_EXISTS);
    }

    private void checkBeforeUpdate(ApplicationUpdateDTO dto) {
        UpmsApplication  application = getApplicationByName(dto.getName());
        boolean condition = application != null && !application.getId().equals(dto.getId());
        Assert.isTrue(condition, BizEnums.APPLICATION_NAME_ALREADY_EXISTS);

        application = getApplicationByCode(dto.getCode());
        condition = application != null && !application.getId().equals(dto.getId());
        Assert.isTrue(condition, BizEnums.APPLICATION_CODE_ALREADY_EXISTS);
    }

    private UpmsApplication getApplicationByName(String name) {
        LambdaQueryWrapper<UpmsApplication> qw = new LambdaQueryWrapper<>();
        qw.eq(UpmsApplication::getName, name);
        return this.getOne(qw);
    }

    private UpmsApplication getApplicationByCode(String code) {
        LambdaQueryWrapper<UpmsApplication> qw = new LambdaQueryWrapper<>();
        qw.eq(UpmsApplication::getCode, code);
        return this.getOne(qw);
    }

}
