package com.kt.iam.module.usergroup.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupListTreeVO extends UserGroupBaseVO implements Serializable {

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<UserGroupListTreeVO> children;
    private List<String> roles;

}
