package com.kt.upms.module.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.upms.entity.UpmsApi;

/**
 * <p>
 * api表 服务类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface IApiService extends IService<UpmsApi> {

    Integer countByApplicationId(Long applicationId);
}
