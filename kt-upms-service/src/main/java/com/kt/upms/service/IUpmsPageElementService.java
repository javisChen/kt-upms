package com.kt.upms.service;

import com.kt.model.dto.route.RouteAddDTO;
import com.kt.model.dto.pageelement.PageElementAddDTO;
import com.kt.model.dto.pageelement.PageElementQueryDTO;
import com.kt.model.dto.pageelement.PageElementUpdateDTO;
import com.kt.model.vo.pageelement.PageElementVO;
import com.kt.upms.entity.UpmsPageElement;
import com.baomidou.mybatisplus.extension.service.IService;

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

    List<PageElementVO> listPageElement(PageElementQueryDTO dto);

    void batchSavePageElement(Long routeId, List<RouteAddDTO.Element> elements);

    List<PageElementVO> getPageElementsByRouteId(Long id);

    void removePageElementByRouteId(Long routeId);
}
