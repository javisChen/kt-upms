package com.kt.iam.module.usergroup.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupBaseVO implements Serializable {

    private Long id;
    private Long pid;
    private String name;
    private Integer status;
    private Integer level;

}
