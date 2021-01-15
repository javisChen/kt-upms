package com.kt.upms.module.api.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.upms.entity.UpmsApi;
import com.kt.upms.mapper.UpmsApiMapper;
import org.springframework.stereotype.Service;

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

    @Override
    public Integer countByApplicationId(Long applicationId) {
     return this.count(new LambdaQueryWrapper<UpmsApi>().eq(UpmsApi::getApplicationId, applicationId));
    }
}
