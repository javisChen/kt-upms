package com.kt.model.dto.menu;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuAllDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<MenuItemDTO> menuList;

    @EqualsAndHashCode
    @Data
    public static class MenuItemDTO {

        private Long id;

        /**
         * 元素名称
         */
        private String name;

        /**
         * 父级菜单id
         */
        private Long pid;

        /**
         * 菜单层级
         */
        private Integer level;

        /**
         * 路径
         */
        private String path;

        /**
         * 图标
         */
        private String icon;

        private Set<MenuItemDTO> children;
    }
}
