package com.kt.upms.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.model.dto.pageelement.PageElementAddDTO;
import com.kt.model.dto.pageelement.PageElementQueryDTO;
import com.kt.model.dto.pageelement.PageElementUpdateDTO;
import com.kt.model.dto.route.RouteAddDTO;
import com.kt.model.enums.DeletedEnums;
import com.kt.model.vo.pageelement.PageElementVO;
import com.kt.upms.entity.UpmsPageElement;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.mapper.UpmsPageElementMapper;
import com.kt.upms.service.IUpmsPageElementService;
import com.kt.upms.service.IUpmsPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 页面元素表 服务实现类
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
@Service
public class UpmsPageElementServiceImpl extends ServiceImpl<UpmsPageElementMapper, UpmsPageElement>
        implements IUpmsPageElementService {

    @Autowired
    private IUpmsPermissionService iUpmsPermissionService;

    @Override
    public void updatePageElement(PageElementUpdateDTO dto) {
        UpmsPageElement entity = new UpmsPageElement();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        this.updateById(entity);
    }

    @Override
    public void removePageElement(PageElementUpdateDTO dto) {
        UpmsPageElement entity = new UpmsPageElement();
        entity.setId(dto.getId());
        entity.setIsDeleted(dto.getId());
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20000)
    public void savePageElement(PageElementAddDTO dto) {
        UpmsPageElement pageElement = CglibUtil.copy(dto, UpmsPageElement.class);
        this.save(pageElement);
        iUpmsPermissionService.addPermission(pageElement.getId(), PermissionTypeEnums.PAGE_ELEMENT);
    }

    @Override
    public List<PageElementVO> listPageElementVO(PageElementQueryDTO dto) {
        List<UpmsPageElement> list = listPageElements();
        return list.stream().map(this::assemblePageElementVO).collect(Collectors.toList());
    }

    private List<UpmsPageElement> listPageElements() {
        return listPageElements(null);
    }

    private List<UpmsPageElement> listPageElements(Wrapper<UpmsPageElement> queryWrapper) {
        return Optional.ofNullable(this.list(queryWrapper)).orElseGet(ArrayList::new);
    }

    @Override
    public void batchSavePageElement(Long routeId, List<RouteAddDTO.Element> elements) {
        elements.forEach(item -> {
            PageElementAddDTO dto = new PageElementAddDTO();
            dto.setRouteId(routeId);
            dto.setName(item.getName());
            dto.setType(item.getType());
            this.savePageElement(dto);
        });
    }

    @Override
    public List<PageElementVO> getPageElementVOSByRouteId(Long routeId) {
        return getPageElementsByRouteId(routeId).stream().map(this::assemblePageElementVO).collect(Collectors.toList());
    }

    @Override
    public List<UpmsPageElement> getPageElementsByRouteId(Long routeId) {
        LambdaQueryWrapper<UpmsPageElement> qw = new LambdaQueryWrapper<>();
        qw.eq(UpmsPageElement::getRouteId, routeId);
        qw.eq(UpmsPageElement::getIsDeleted, DeletedEnums.NOT.getCode());
        return listPageElements(qw);
    }

    @Override
    public void removePageElementByRouteId(Long routeId) {
        this.update(new LambdaUpdateWrapper<UpmsPageElement>()
                .eq(UpmsPageElement::getRouteId, routeId)
                .set(UpmsPageElement::getIsDeleted, DeletedEnums.YET.getCode())
        );
    }

    private PageElementVO assemblePageElementVO(UpmsPageElement item) {
        PageElementVO vo = new PageElementVO();
        vo.setId(item.getId());
        vo.setRouteId(item.getRouteId());
        vo.setName(item.getName());
        vo.setType(item.getType());
        return vo;
    }
}
