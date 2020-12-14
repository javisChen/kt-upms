package com.kt.upms.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.dto.PageResponse;
import com.kt.component.exception.BizException;
import com.kt.model.dto.menu.MenuAddDTO;
import com.kt.model.dto.menu.MenuQueryDTO;
import com.kt.model.dto.menu.MenuUpdateDTO;
import com.kt.model.enums.BizEnum;
import com.kt.upms.entity.UpmsMenu;
import com.kt.upms.enums.MenuStatusEnum;
import com.kt.upms.mapper.UpmsMenuMapper;
import com.kt.upms.service.IUpmsMenuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@Service
public class UpmsMenuServiceImpl extends ServiceImpl<UpmsMenuMapper, UpmsMenu> implements IUpmsMenuService {

    private final static Integer DEFAULT_PID = 0;
    private final static Integer LEVEL_ONE = 1;

    @Override
    public PageResponse<MenuQueryDTO> pageList(Page page, MenuQueryDTO params) {
        return null;
    }

    @Override
    public MenuAddDTO saveMenu(MenuAddDTO dto) {
        UpmsMenu upmsMenu = this.getOne(new LambdaQueryWrapper<UpmsMenu>()
                .eq(UpmsMenu::getName, dto.getName()));

        if (upmsMenu != null) {
            throw new BizException(BizEnum.MENU_ALREADY_EXISTS.getCode(), BizEnum.MENU_ALREADY_EXISTS.getMsg());
        }
        UpmsMenu newMenu = CglibUtil.copy(dto, UpmsMenu.class);
        if (newMenu.getPid().equals(DEFAULT_PID)) {
            newMenu.setLevel(LEVEL_ONE);
            newMenu.setLevelPath(String.valueOf(LEVEL_ONE));
        } else {
            UpmsMenu parentMenu = this.getOne(new LambdaQueryWrapper<UpmsMenu>()
                    .eq(UpmsMenu::getId, newMenu.getPid()));
            if (parentMenu == null) {
                throw new BizException(BizEnum.PARENT_MENU_NOT_EXISTS.getCode(), BizEnum.PARENT_MENU_NOT_EXISTS.getMsg());
            }
            int level = parentMenu.getLevel() + 1;
            newMenu.setLevel(level);
            newMenu.setLevelPath(parentMenu.getLevelPath() + StrUtil.DOT + level);
        }
        this.save(newMenu);
        return dto;
    }

    @Override
    public void updateMenu(MenuUpdateDTO dto) {
        LambdaQueryWrapper<UpmsMenu> queryWrapper = new LambdaQueryWrapper<UpmsMenu>()
                .eq(UpmsMenu::getName, dto.getName())
                .ne(UpmsMenu::getId, dto.getId());
        UpmsMenu upmsMenu = this.getOne(queryWrapper);
        if (upmsMenu != null) {
            throw new BizException(BizEnum.MENU_ALREADY_EXISTS.getCode(),
                    BizEnum.MENU_ALREADY_EXISTS.getMsg());
        }
        UpmsMenu updateUpmsMenu = CglibUtil.copy(dto, UpmsMenu.class);
        this.updateById(updateUpmsMenu);
    }

    @Override
    public void disableMenu(MenuUpdateDTO dto) {
        // todo 禁用菜单需要把子菜单也同时禁用
    }

    @Override
    public void enableMenu(MenuUpdateDTO dto) {
        updateUserStatus(dto, MenuStatusEnum.ENABLED);
    }


    private void updateUserStatus(MenuUpdateDTO dto, MenuStatusEnum menuStatusEnum) {
        this.update(new LambdaUpdateWrapper<UpmsMenu>()
                .eq(UpmsMenu::getStatus, dto.getId())
                .set(UpmsMenu::getStatus, menuStatusEnum.getValue()));
    }
}
