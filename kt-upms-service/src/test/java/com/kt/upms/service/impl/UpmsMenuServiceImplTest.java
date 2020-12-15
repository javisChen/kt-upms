package com.kt.upms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kt.model.dto.menu.MenuAllDTO;
import com.kt.upms.entity.UpmsMenu;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
public class UpmsMenuServiceImplTest {

    @Before
    public void setUp() throws Exception {
    }

    /**
     * 测试菜单树获取
     */
    @Test
    public void testGetMenuTree() {
        List<UpmsMenu> menus = new ArrayList<>();
        menus.add(new UpmsMenu(1L, "用户管理", 0L, "1", 1, "", ""));
        menus.add(new UpmsMenu(2L, "新增用户", 1L, "1.2", 2, "/user/add", ""));
        menus.add(new UpmsMenu(3L, "修改用户", 1L, "1.2", 2, "/user/update", ""));

        menus.add(new UpmsMenu(4L, "菜单管理", 0L, "0", 1, "", ""));
        menus.add(new UpmsMenu(5L, "新增菜单", 4L, "1", 1, "/user/add", ""));
        menus.add(new UpmsMenu(6L, "修改菜单", 4L, "1.2", 2, "/user/update", ""));
        menus.add(new UpmsMenu(7L, "删除菜单", 4L, "1.2", 2, "/user/delete", ""));

        menus.add(new UpmsMenu(8L, "系统管理", 0L, "0", 1, "", ""));
        menus.add(new UpmsMenu(9L, "代码生成", 8L, "1", 1, "/user/add", ""));
        menus.add(new UpmsMenu(10L, "工程生成", 9L, "1.2", 2, "/user/update", ""));
        menus.add(new UpmsMenu(11L, "实体生成", 9L, "1.2", 2, "/user/delete", ""));
        menus.add(new UpmsMenu(12L, "日志管理", 8L, "1", 1, "/user/add", ""));
        menus.add(new UpmsMenu(13L, "日志查看", 12L, "1.2", 2, "/user/update", ""));

        Set<MenuAllDTO.MenuItemDTO> tree = new UpmsMenuServiceImpl().getTree(menus);
        System.out.println(JSONObject.toJSONString(tree, SerializerFeature.PrettyFormat));
    }
}