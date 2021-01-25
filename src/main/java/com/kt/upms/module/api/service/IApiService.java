package com.kt.upms.module.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.upms.entity.UpmsApi;
import com.kt.upms.module.api.dto.ApiQueryDTO;
import com.kt.upms.module.api.dto.ApiUpdateDTO;
import com.kt.upms.module.api.vo.ApiListVO;

import java.util.List;

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

    void saveApplication(ApiUpdateDTO dto);

    void updateApi(ApiUpdateDTO dto);

    List<ApiListVO> listVos(ApiQueryDTO dto);

    void removeApi(Long dto);

    /**
     * 获取无需授权的api
     */
    List<UpmsApi> getNoNeedAuthorizationApis();

    /**
     * 获取无需认证的api
     */
    List<UpmsApi> getNoNeedAuthenticationApis();

    List<UpmsApi> listAll();

}
