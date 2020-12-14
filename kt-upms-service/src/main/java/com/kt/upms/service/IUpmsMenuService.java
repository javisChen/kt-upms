package com.kt.upms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.menu.MenuAddDTO;
import com.kt.model.dto.menu.MenuQueryDTO;
import com.kt.model.dto.menu.MenuUpdateDTO;
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

    PageResponse<MenuQueryDTO> pageList(Page page, MenuQueryDTO params);

    MenuAddDTO saveMenu(MenuAddDTO dto);

    void updateMenu(MenuUpdateDTO dto);

    void disableMenu(MenuUpdateDTO dto);

    void enableMenu(MenuUpdateDTO dto);
}
