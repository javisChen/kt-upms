package com.kt.upms.module.route.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.upms.entity.UpmsPageElement;
import com.kt.upms.module.route.dto.PageElementAddDTO;
import com.kt.upms.module.route.dto.PageElementQueryDTO;
import com.kt.upms.module.route.dto.PageElementUpdateDTO;
import com.kt.upms.module.route.dto.RouteAddDTO;
import com.kt.upms.module.route.vo.PageElementVO;

import java.util.List;

/**
 * <p>
 * 页面元素表 服务类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface IUpmsPageElementService extends IService<UpmsPageElement> {

    void updatePageElement(PageElementUpdateDTO dto);

    void removePageElement(PageElementUpdateDTO dto);

    void savePageElement(PageElementAddDTO dto);

    List<PageElementVO> listElementVO(PageElementQueryDTO dto);

    void batchSavePageElement(Long routeId, List<RouteAddDTO.Element> elements);

    List<PageElementVO> getPageElementVOSByRouteId(Long id);

    List<UpmsPageElement> getPageElementsByRouteId(Long routeId);

    void removePageElementByRouteId(Long routeId);
}
