package com.kt.iam.module.api.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.iam.module.api.persistence.IamApiCategory;
import com.kt.iam.enums.BizEnums;
import com.kt.iam.enums.DeletedEnums;
import com.kt.iam.module.api.persistence.dao.IamApiCategoryMapper;
import com.kt.iam.module.api.converter.ApiBeanConverter;
import com.kt.iam.module.api.dto.ApiCategoryUpdateDTO;
import com.kt.iam.module.api.vo.ApiCategoryBaseVO;
import com.kt.iam.common.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Service
public class ApiCategoryServiceImpl extends ServiceImpl<IamApiCategoryMapper, IamApiCategory>
        implements IApiCategoryService {

    @Autowired
    private ApiBeanConverter beanConverter;
    @Autowired
    private IApiService iApiService;

    @Override
    public List<ApiCategoryBaseVO> listVos(Long applicationId) {
        LambdaQueryWrapper<IamApiCategory> qw = new LambdaQueryWrapper<>();
        qw.eq(IamApiCategory::getIsDeleted, DeletedEnums.NOT.getCode());
        qw.eq(IamApiCategory::getApplicationId, applicationId);
        return this.list(qw).stream().map(beanConverter::convertToApiCategoryVO).collect(Collectors.toList());
    }

    @Override
    public void saveApiCategory(ApiCategoryUpdateDTO dto) {
        IamApiCategory one = getApiCategoryByNameAndApplicationId(dto);
        Assert.isTrue(one != null, BizEnums.API_CATEGORY_ALREADY_EXISTS);
        IamApiCategory apiCategory = beanConverter.convertToDO(dto);
        this.save(apiCategory);
    }

    private IamApiCategory getApiCategoryByNameAndApplicationId(ApiCategoryUpdateDTO dto) {
        LambdaQueryWrapper<IamApiCategory> qw = new LambdaQueryWrapper<>();
        qw.eq(IamApiCategory::getIsDeleted, DeletedEnums.NOT.getCode());
        qw.eq(IamApiCategory::getName, dto.getName());
        qw.eq(IamApiCategory::getApplicationId, dto.getApplicationId());
        return this.getOne(qw);
    }

    @Override
    public void updateApiCategory(ApiCategoryUpdateDTO dto) {
        IamApiCategory one = getApiCategoryByNameAndApplicationId(dto);
        Assert.isTrue(one != null && !dto.getId().equals(one.getId()), BizEnums.API_CATEGORY_ALREADY_EXISTS);
        IamApiCategory apiCategory = beanConverter.convertToDO(dto);
        this.updateById(apiCategory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeApiCategory(Long id) {
        LambdaUpdateWrapper<IamApiCategory> qw = new LambdaUpdateWrapper<>();
        qw.eq(IamApiCategory::getId, id);
        qw.eq(IamApiCategory::getIsDeleted, DeletedEnums.NOT.getCode());
        qw.set(IamApiCategory::getIsDeleted, id);
        this.update(qw);

        // 把分类下的api都删除掉
        iApiService.removeByCategoryId(id);
    }
}
