package com.kt.upms.module.api.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.upms.entity.UpmsApiCategory;
import com.kt.upms.enums.DeletedEnums;
import com.kt.upms.mapper.UpmsApiCategoryMapper;
import com.kt.upms.module.api.converter.ApiBeanConverter;
import com.kt.upms.module.api.dto.ApiCategoryUpdateDTO;
import com.kt.upms.module.api.vo.ApiCategoryBaseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Service
public class ApiCategoryServiceImpl extends ServiceImpl<UpmsApiCategoryMapper, UpmsApiCategory>
        implements IApiCategoryService {

    @Autowired
    private ApiBeanConverter beanConverter;
    @Override
    public List<ApiCategoryBaseVO> listVos(Long applicationId) {
        LambdaQueryWrapper<UpmsApiCategory> qw = new LambdaQueryWrapper<>();
        qw.eq(UpmsApiCategory::getIsDeleted, DeletedEnums.NOT.getCode());
        qw.eq(UpmsApiCategory::getApplicationId, applicationId);
        return this.list(qw).stream().map(beanConverter::convertToApiCategoryVO).collect(Collectors.toList());
    }

    @Override
    public void saveApiCategory(ApiCategoryUpdateDTO dto) {
        UpmsApiCategory apiCategory = beanConverter.convertToDO(dto);
        this.save(apiCategory);
    }

    @Override
    public void updateApiCategory(ApiCategoryUpdateDTO dto) {
        UpmsApiCategory apiCategory = beanConverter.convertToDO(dto);
        this.updateById(apiCategory);
    }

    @Override
    public void removeApiCategory(Long id) {
        LambdaUpdateWrapper<UpmsApiCategory> qw = new LambdaUpdateWrapper<>();
        qw.eq(UpmsApiCategory::getId, id);
        qw.eq(UpmsApiCategory::getIsDeleted, DeletedEnums.NOT.getCode());
        qw.set(UpmsApiCategory::getIsDeleted, DeletedEnums.YET.getCode());
        this.update(qw);
    }
}
