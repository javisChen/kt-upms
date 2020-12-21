package com.kt.upms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.dto.PageResponse;
import com.kt.model.dto.menu.*;
import com.kt.model.enums.BizEnums;
import com.kt.upms.entity.UpmsMenu;
import com.kt.upms.enums.MenuStatusEnum;
import com.kt.upms.enums.PermissionTypeEnums;
import com.kt.upms.mapper.UpmsMenuMapper;
import com.kt.upms.service.IUpmsMenuService;
import com.kt.upms.service.IUpmsPermissionService;
import com.kt.upms.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final IUpmsPermissionService iUpmsPermissionService;

    public UpmsMenuServiceImpl(IUpmsPermissionService iUpmsPermissionService) {
        this.iUpmsPermissionService = iUpmsPermissionService;
    }

    @Override
    public PageResponse<UpmsMenu> pageList(Page page, MenuQueryDTO params) {
        LambdaQueryWrapper<UpmsMenu> query = new LambdaQueryWrapper<UpmsMenu>()
                .like(StrUtil.isNotBlank(params.getName()), UpmsMenu::getName, params.getName())
                .eq(params.getPid() != null, UpmsMenu::getId, params.getPid());
        return PageResponse.success(this.page(page, query));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 20000)
    public MenuAddDTO saveMenu(MenuAddDTO dto) {
        UpmsMenu upmsMenu = getMenuByName(dto.getName());

        Assert.isTrue(upmsMenu != null, BizEnums.MENU_ALREADY_EXISTS);

        UpmsMenu newMenu = CglibUtil.copy(dto, UpmsMenu.class);
        if (newMenu.getPid().equals(DEFAULT_PID)) {
            newMenu.setLevel(LEVEL_ONE);
            newMenu.setLevelPath(LEVEL_ONE + StrUtil.DOT);
        } else {
            UpmsMenu parentMenu = getMenuById(newMenu.getPid());
            Assert.isTrue(parentMenu == null, BizEnums.PARENT_MENU_NOT_EXISTS);
            int level = parentMenu.getLevel() + 1;
            newMenu.setLevel(level);
            newMenu.setLevelPath(parentMenu.getLevelPath() + level + StrUtil.DOT);
        }
        this.save(newMenu);

        iUpmsPermissionService.addPermission(newMenu.getId(), PermissionTypeEnums.MENU);

        return dto;
    }

    private UpmsMenu getMenuByName(String name) {
        return this.getOne(new LambdaQueryWrapper<UpmsMenu>().eq(UpmsMenu::getName, name));
    }

    @Override
    public void updateMenu(MenuUpdateDTO dto) {
        LambdaQueryWrapper<UpmsMenu> queryWrapper = new LambdaQueryWrapper<UpmsMenu>()
                .eq(UpmsMenu::getName, dto.getName())
                .ne(UpmsMenu::getId, dto.getId());
        int count = this.count(queryWrapper);
        Assert.isTrue(count > 0, BizEnums.MENU_ALREADY_EXISTS);

        UpmsMenu updateMenu = CglibUtil.copy(dto, UpmsMenu.class);
        this.updateById(updateMenu);
    }

    @Override
    public void updateMenuStatus(MenuUpdateDTO dto) {
        UpmsMenu menu = getMenuById(dto.getId());
        if (menu != null) {
            this.update(new LambdaUpdateWrapper<UpmsMenu>()
                    .likeRight(UpmsMenu::getLevelPath, menu.getLevelPath())
                    .set(UpmsMenu::getStatus, dto.getStatus()));
        }
    }

    @Override
    public void modifyParent(MenuModifyParentDTO dto) {
        UpmsMenu childMenu = getMenuById(dto.getId());
        Assert.isTrue(childMenu != null, BizEnums.MENU_NOT_EXISTS);

        UpmsMenu parentMenu = getMenuById(dto.getPid());
        Assert.isTrue(parentMenu != null, BizEnums.PARENT_MENU_NOT_EXISTS);

        UpmsMenu updateMenu = CglibUtil.copy(dto, UpmsMenu.class);
        this.updateById(updateMenu);
    }

    private UpmsMenu getMenuById(Long id) {
        LambdaQueryWrapper<UpmsMenu> queryWrapper = new LambdaQueryWrapper<UpmsMenu>()
                .eq(UpmsMenu::getId, id);
        return this.getOne(queryWrapper);
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
