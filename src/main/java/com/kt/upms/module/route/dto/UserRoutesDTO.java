package com.kt.upms.module.route.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoutesDTO implements Serializable {

    List<UserRouteItem> routes;

    @Data
    public static class UserRouteItem {
        private String name;
        private String parentId;
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
}
