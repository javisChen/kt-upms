package com.kt.upms.module.route.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteAnotherTreeVO implements Serializable {

    private List<TreeNode> routes;

    @Data
    public static class TreeNode {

        private Long id;
        private Long pid;
        private Integer sequence;
        private String code;
        private String name;
        private String icon;
        private String component;
        private String levelPath;
        private Integer status;
        private String path;
        private Integer type;
        private Boolean hideChildren;
        private List<TreeNode> children;
        private Boolean group;

    }
}
