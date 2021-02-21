package com.kt.iam.module.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.iam.module.api.persistence.IamApi;
import com.kt.iam.module.api.dto.ApiQueryDTO;
import com.kt.iam.module.api.dto.ApiUpdateDTO;
import com.kt.iam.module.api.vo.ApiDetailVO;
import com.kt.iam.module.api.vo.ApiListVO;

import java.util.List;

/**
 * <p>
 * api表 服务类
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
public interface IApiService extends IService<IamApi> {

    Integer countByApplicationId(Long applicationId);

    void saveApplication(ApiUpdateDTO dto);

    void updateApi(ApiUpdateDTO dto);

    List<ApiListVO> listVos(ApiQueryDTO dto);

    void removeApi(Long dto);

    List<IamApi> listAll();

    void removeByCategoryId(Long id);

    ApiDetailVO getApplicationVO(Long id);
}
