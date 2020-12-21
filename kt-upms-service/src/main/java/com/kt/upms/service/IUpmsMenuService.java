package com.kt.upms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.menu.*;
import com.kt.upms.entity.UpmsMenu;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface IUpmsMenuService extends IService<UpmsMenu> {

    PageResponse<UpmsMenu> pageList(Page page, MenuQueryDTO params);

    MenuAddDTO saveMenu(MenuAddDTO dto);

    void updateMenu(MenuUpdateDTO dto);

    void updateMenuStatus(MenuUpdateDTO dto);

    void modifyParent(MenuModifyParentDTO dto);

    MenuAllDTO getAllMenus();

}
