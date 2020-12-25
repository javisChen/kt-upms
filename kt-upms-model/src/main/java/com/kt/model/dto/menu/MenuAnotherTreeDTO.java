package com.kt.model.dto.menu;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuAnotherTreeDTO implements Serializable {

    private List<TreeNode> routes;

    @Data
    public static class TreeNode {

        private Long id;
        private Long pid;
        private String key;
        private String title;
        private String icon;
        private List<TreeNode> children;
        private Boolean group;

    }
}
