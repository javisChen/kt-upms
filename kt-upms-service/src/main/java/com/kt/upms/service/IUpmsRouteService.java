package com.kt.upms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.menu.*;
import com.kt.model.vo.route.RouteListTreeVO;
import com.kt.upms.entity.UpmsRoute;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface IUpmsRouteService extends IService<UpmsRoute> {

    PageResponse<RouteListTreeVO> pageList(RouteQueryDTO pageRequest);

    void saveRoute(RouteAddDTO dto);

    void updateRoute(RouteUpdateDTO dto);

    void updateRouteStatus(RouteUpdateDTO dto);

    void modifyParent(RouteModifyParentDTO dto);

    UpmsRoute getRouteById(Long id);

    UserRoutesDTO getUserRoutes();

    void deleteRouteById(Long id);

    List<RouteListTreeVO> getTree();
}
