package com.kt.iam.module.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPermissionRouteNavVO implements Serializable {

    private String name;
    private Long parentId;
    private String id;
    private Meta meta;
    private String component;
    private String redirect;
    private String path;

    @Data
    public static class Meta {
        private String icon;
        private String title;
        private Boolean show;
        private Boolean hideChildren;

    }
}
