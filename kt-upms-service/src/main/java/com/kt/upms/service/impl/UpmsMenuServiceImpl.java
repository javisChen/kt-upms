package com.kt.upms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.dto.PageResponse;
import com.kt.component.exception.BizException;
import com.kt.model.dto.menu.*;
import com.kt.model.enums.BizEnum;
import com.kt.upms.entity.UpmsMenu;
import com.kt.upms.enums.MenuStatusEnum;
import com.kt.upms.mapper.UpmsMenuMapper;
import com.kt.upms.service.IUpmsMenuService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    private final static Long DEFAULT_PID = 0L;
    private final static Integer LEVEL_ONE = 1;

    @Override
    public PageResponse<UpmsMenu> pageList(Page page, MenuQueryDTO params) {
        LambdaQueryWrapper<UpmsMenu> query = new LambdaQueryWrapper<UpmsMenu>()
                .like(StrUtil.isNotBlank(params.getName()), UpmsMenu::getName, params.getName())
                .eq(params.getPid() != null, UpmsMenu::getId, params.getPid());
        return PageResponse.success(this.page(page, query));
    }

    @Override
    public MenuAddDTO saveMenu(MenuAddDTO dto) {
        UpmsMenu upmsMenu = this.getOne(new LambdaQueryWrapper<UpmsMenu>().eq(UpmsMenu::getName, dto.getName()));
        if (upmsMenu != null) {
            throw new BizException(BizEnum.MENU_ALREADY_EXISTS.getCode(), BizEnum.MENU_ALREADY_EXISTS.getMsg());
        }
        UpmsMenu newMenu = CglibUtil.copy(dto, UpmsMenu.class);
        if (newMenu.getPid().equals(DEFAULT_PID)) {
            newMenu.setLevel(LEVEL_ONE);
            newMenu.setLevelPath(LEVEL_ONE + StrUtil.DOT);
        } else {
            UpmsMenu parentMenu = this.getOne(new LambdaQueryWrapper<UpmsMenu>()
                    .eq(UpmsMenu::getId, newMenu.getPid()));
            if (parentMenu == null) {
                throw new BizException(BizEnum.PARENT_MENU_NOT_EXISTS.getCode(), BizEnum.PARENT_MENU_NOT_EXISTS.getMsg());
            }
            int level = parentMenu.getLevel() + 1;
            newMenu.setLevel(level);
            newMenu.setLevelPath(parentMenu.getLevelPath() + level + StrUtil.DOT);
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
        UpmsMenu updateMenu = CglibUtil.copy(dto, UpmsMenu.class);
        this.updateById(updateMenu);
    }

    @Override
    public void updateMenuStatus(MenuUpdateDTO dto) {
        LambdaQueryWrapper<UpmsMenu> queryWrapper = new LambdaQueryWrapper<UpmsMenu>()
                .eq(UpmsMenu::getId, dto.getId());
        UpmsMenu menu = this.getOne(queryWrapper);
        if (menu != null) {
            this.update(new LambdaUpdateWrapper<UpmsMenu>()
                    .likeRight(UpmsMenu::getLevelPath, menu.getLevelPath())
                    .set(UpmsMenu::getStatus, dto.getStatus()));
        }
    }

    @Override
    public void enableMenu(MenuUpdateDTO dto) {
        updateStatus(dto, MenuStatusEnum.ENABLED);
    }

    @Override
    public void modifyParent(MenuModifyParentDTO dto) {
        LambdaQueryWrapper<UpmsMenu> queryWrapper = new LambdaQueryWrapper<UpmsMenu>()
                .eq(UpmsMenu::getId, dto.getId());
        UpmsMenu childMenu = this.getOne(queryWrapper);
        if (childMenu != null) {
            throw new BizException(BizEnum.MENU_NOT_EXISTS.getCode(),
                    BizEnum.MENU_NOT_EXISTS.getMsg());
        }
        UpmsMenu parentMenu = this.getOne(new LambdaQueryWrapper<UpmsMenu>()
                .eq(UpmsMenu::getId, dto.getPid()));
        if (parentMenu == null) {
            throw new BizException(BizEnum.PARENT_MENU_NOT_EXISTS.getCode(),
                    BizEnum.PARENT_MENU_NOT_EXISTS.getMsg());
        }
        UpmsMenu updateMenu = CglibUtil.copy(dto, UpmsMenu.class);
        this.updateById(updateMenu);
    }

    @Override
    public MenuAllDTO getAllMenus() {
        return new MenuAllDTO(getTree(this.list()));
    }

    public Set<MenuAllDTO.MenuItemDTO> getTree(List<UpmsMenu> list) {
        if (CollectionUtil.isEmpty(list)) {
            return CollectionUtil.newHashSet();
        }
        Set<MenuAllDTO.MenuItemDTO> resultSet = CollectionUtil.newHashSet();
        Set<UpmsMenu> levelOneMenus = list.stream().filter(item -> item.getPid().equals(DEFAULT_PID))
                .collect(Collectors.toSet());
        Set<UpmsMenu> anotherMenus = list.stream().filter(item -> !item.getPid().equals(DEFAULT_PID))
                .collect(Collectors.toSet());

        for (UpmsMenu upmsMenu : levelOneMenus) {
            MenuAllDTO.MenuItemDTO item = CglibUtil.copy(upmsMenu, MenuAllDTO.MenuItemDTO.class);
            item.setChildren(CollectionUtil.newHashSet());
            findChildren(item, anotherMenus);
            resultSet.add(item);
        }
        return resultSet;
    }

    private void findChildren(MenuAllDTO.MenuItemDTO parent, Set<UpmsMenu> list) {
        for (UpmsMenu upmsMenu : list) {
            if (parent.getId().equals(upmsMenu.getPid())) {
                MenuAllDTO.MenuItemDTO item = CglibUtil.copy(upmsMenu, MenuAllDTO.MenuItemDTO.class);
                item.setChildren(new HashSet<>());
                parent.getChildren().add(item);
                findChildren(item, list);
            }
        }
    }

    private void updateStatus(MenuUpdateDTO dto, MenuStatusEnum statusEnum) {
        this.update(new LambdaUpdateWrapper<UpmsMenu>()
                .eq(UpmsMenu::getStatus, dto.getId())
                .set(UpmsMenu::getStatus, statusEnum.getValue()));
    }

}
