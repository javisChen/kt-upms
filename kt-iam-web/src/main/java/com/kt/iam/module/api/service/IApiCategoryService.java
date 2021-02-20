package com.kt.iam.module.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.iam.module.api.persistence.IamApiCategory;
import com.kt.iam.module.api.dto.ApiCategoryUpdateDTO;
import com.kt.iam.module.api.vo.ApiCategoryBaseVO;

import java.util.List;

/**
 * <p>
 * api表 服务类
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
public interface IApiCategoryService extends IService<IamApiCategory> {


    List<ApiCategoryBaseVO> listVos(Long applicationId);

    void saveApiCategory(ApiCategoryUpdateDTO dto);

    void updateApiCategory(ApiCategoryUpdateDTO dto);

    void removeApiCategory(Long id);
}
