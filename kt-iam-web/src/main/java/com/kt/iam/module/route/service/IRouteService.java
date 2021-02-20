package com.kt.iam.module.route.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.iam.module.route.persistence.IamRoute;
import com.kt.iam.module.route.dto.*;
import com.kt.iam.module.route.vo.RouteDetailVO;
import com.kt.iam.module.route.vo.RouteElementVO;
import com.kt.iam.module.route.vo.RouteListTreeVO;
import com.kt.iam.module.user.vo.UserPermissionRouteNavVO;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
public interface IRouteService extends IService<IamRoute> {

    Page<RouteListTreeVO> pageList(RouteQueryDTO pageRequest);

    void saveRoute(RouteUpdateDTO dto);

    void updateRoute(RouteUpdateDTO dto);

    void updateRouteStatus(RouteUpdateDTO dto);

    void modifyParent(RouteModifyParentDTO dto);

    RouteDetailVO getRoute(Long id);

    void deleteRouteById(Long id);

    List<RouteListTreeVO> listAllVOs(RouteQueryDTO dto);

    List<RouteElementVO> listRouteElementsById(Long routeId);

    String getRouteNameById(Long pid);

    Integer countByApplicationId(Long applicationId);

    List<UserPermissionRouteNavVO> getRouteVOSByIds(List<Long> routeIds);
}
