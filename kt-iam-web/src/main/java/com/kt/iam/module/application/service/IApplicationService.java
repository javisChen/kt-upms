package com.kt.iam.module.application.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.iam.module.application.persistence.IamApplication;
import com.kt.iam.module.application.dto.ApplicationQueryDTO;
import com.kt.iam.module.application.dto.ApplicationUpdateDTO;
import com.kt.iam.module.application.vo.ApplicationBaseVO;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
public interface IApplicationService extends IService<IamApplication> {

    void updateApplication(ApplicationUpdateDTO dto);

    void saveApplication(ApplicationUpdateDTO dto);

    List<ApplicationBaseVO> listVos(ApplicationQueryDTO dto);

    String getNameById(Long applicationId);

    IamApplication getApplicationByCode(String code);
}
