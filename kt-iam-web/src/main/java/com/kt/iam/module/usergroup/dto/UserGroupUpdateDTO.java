package com.kt.iam.module.usergroup.dto;


import com.kt.component.validator.ValidateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class UserGroupUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "id 不能为空", groups = ValidateGroup.Update.class)
    private Long id;

    @NotBlank(message = "name 不能为空", groups = ValidateGroup.Add.class)
    private String name;

    private Long pid;

    public List<Long> roleIds;

    private Integer inheritType;
    private Integer type;


}
