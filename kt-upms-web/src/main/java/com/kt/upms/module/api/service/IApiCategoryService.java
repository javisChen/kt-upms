package com.kt.upms.module.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.upms.module.api.persistence.UpmsApiCategory;
import com.kt.upms.module.api.dto.ApiCategoryUpdateDTO;
import com.kt.upms.module.api.vo.ApiCategoryBaseVO;

import java.util.List;

/**
 * <p>
 * api表 服务类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface IApiCategoryService extends IService<UpmsApiCategory> {


    List<ApiCategoryBaseVO> listVos(Long applicationId);

    void saveApiCategory(ApiCategoryUpdateDTO dto);

    void updateApiCategory(ApiCategoryUpdateDTO dto);

    void removeApiCategory(Long id);
}
