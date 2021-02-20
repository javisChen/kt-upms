package com.kt.iam.module.application.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.iam.common.util.Assert;
import com.kt.iam.enums.BizEnums;
import com.kt.iam.enums.DeletedEnums;
import com.kt.iam.module.application.converter.ApplicationBeanConverter;
import com.kt.iam.module.application.dto.ApplicationQueryDTO;
import com.kt.iam.module.application.dto.ApplicationUpdateDTO;
import com.kt.iam.module.application.persistence.IamApplication;
import com.kt.iam.module.application.persistence.dao.IamApplicationMapper;
import com.kt.iam.module.application.vo.ApplicationBaseVO;
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
public class ApplicationServiceImpl extends ServiceImpl<IamApplicationMapper, IamApplication>
        implements IApplicationService {

    @Autowired
    private ApplicationBeanConverter beanConverter;

    @Override
    public void saveApplication(ApplicationUpdateDTO dto) {
        checkBeforeSave(dto);

        IamApplication application = beanConverter.convertForInsert(dto);

        this.save(application);
    }

    @Override
    public void updateApplication(ApplicationUpdateDTO dto) {
        checkBeforeUpdate(dto);

        IamApplication application = beanConverter.convertForUpdate(dto);

        this.updateById(application);
    }

    @Override
    public List<ApplicationBaseVO> listVos(ApplicationQueryDTO dto) {
        return this.list().stream().map(beanConverter::convertForApplicationListVO).collect(Collectors.toList());
    }

    @Override
    public String getNameById(Long applicationId) {
        final LambdaQueryWrapper<IamApplication> qw = new LambdaQueryWrapper<>();
        qw.select(IamApplication::getName);
        qw.eq(IamApplication::getId, applicationId);
        qw.eq(IamApplication::getIsDeleted, DeletedEnums.NOT.getCode());
        IamApplication application = Optional.ofNullable(this.getOne(qw)).orElseGet(IamApplication::new);
        return application.getName();
    }

    private void checkBeforeSave(ApplicationUpdateDTO dto) {
        IamApplication application = getApplicationByName(dto.getName());
        Assert.isTrue(application != null, BizEnums.APPLICATION_NAME_ALREADY_EXISTS);
        application = getApplicationByCode(dto.getCode());
        Assert.isTrue(application != null, BizEnums.APPLICATION_CODE_ALREADY_EXISTS);
    }

    private void checkBeforeUpdate(ApplicationUpdateDTO dto) {
        IamApplication application = getApplicationByName(dto.getName());
        boolean condition = application != null && !application.getId().equals(dto.getId());
        Assert.isTrue(condition, BizEnums.APPLICATION_NAME_ALREADY_EXISTS);

        application = getApplicationByCode(dto.getCode());
        condition = application != null && !application.getId().equals(dto.getId());
        Assert.isTrue(condition, BizEnums.APPLICATION_CODE_ALREADY_EXISTS);
    }

    private IamApplication getApplicationByName(String name) {
        LambdaQueryWrapper<IamApplication> qw = new LambdaQueryWrapper<>();
        qw.eq(IamApplication::getName, name);
        return this.getOne(qw);
    }

    @Override
    public IamApplication getApplicationByCode(String code) {
        LambdaQueryWrapper<IamApplication> qw = new LambdaQueryWrapper<>();
        qw.eq(IamApplication::getCode, code);
        qw.eq(IamApplication::getIsDeleted, DeletedEnums.NOT.getCode());
        return this.getOne(qw);
    }

}
