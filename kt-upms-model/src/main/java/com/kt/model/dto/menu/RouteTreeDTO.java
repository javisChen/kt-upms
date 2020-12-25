package com.kt.model.dto.menu;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteTreeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<TreeNode> routes;

    @EqualsAndHashCode
    @Data
    public static class TreeNode {

        private Long id;

        private String name;

        private Long pid;

        private Integer level;

        private String path;

        private String icon;

        private List<TreeNode> children;
    }
}
