package com.kt.iam.module.route.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.iam.module.route.persistence.IamPageElement;
import com.kt.iam.module.route.dto.PageElementUpdateDTO;
import com.kt.iam.module.route.dto.RouteUpdateDTO;
import com.kt.iam.module.route.vo.PageElementVO;

import java.util.List;

/**
 * <p>
 * 页面元素表 服务类
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
public interface IPageElementService extends IService<IamPageElement> {

    void savePageElement(PageElementUpdateDTO dto);

    void batchSavePageElement(Long routeId, List<RouteUpdateDTO.Element> elements);

    List<PageElementVO> getPageElementVOSByRouteId(Long id);

    List<IamPageElement> getPageElementsByRouteId(Long routeId);

    void removePageElementByRouteId(Long routeId);

    List<IamPageElement> listElementsByRouteId(Long routeId);

    List<IamPageElement> getPageElementsByIds(List<Long> elementIds);
}
