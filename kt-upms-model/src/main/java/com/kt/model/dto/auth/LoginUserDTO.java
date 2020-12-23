package com.kt.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Data
public class LoginUserDTO {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("status")
    private int status;
    @JsonProperty("roleId")
    private String roleId;
    @JsonProperty("token")
    private String token;

    @JsonProperty("role")
    private RoleBO role;

    @Data
    public static class RoleBO {
        @JsonProperty("id")
        private String id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("describe")
        private String describe;
        @JsonProperty("status")
        private int status;
        @JsonProperty("creatorId")
        private String creatorId;
        @JsonProperty("createTime")
        private long createTime;
        @JsonProperty("deleted")
        private int deleted;

        @JsonProperty("permissions")
        private List<PermissionsBO> permissions;

        @Data
        public static class PermissionsBO {
            @JsonProperty("roleId")
            private String roleId;
            @JsonProperty("permissionId")
            private String permissionId;
            @JsonProperty("permissionName")
            private String permissionName;
            @JsonProperty("actions")
            private String actions;
            @JsonProperty("actionList")
            private Object actionList;
            @JsonProperty("dataAccess")
            private Object dataAccess;
            /**
             * action : add
             * describe : 新增
             * defaultCheck : false
             */

            @JsonProperty("actionEntitySet")
            private List<ActionEntitySetBO> actionEntitySet;

            @Data
            public static class ActionEntitySetBO {
                @JsonProperty("action")
                private String action;
                @JsonProperty("describe")
                private String describe;
                @JsonProperty("defaultCheck")
                private Boolean defaultCheck;

            }
        }
    }
}
