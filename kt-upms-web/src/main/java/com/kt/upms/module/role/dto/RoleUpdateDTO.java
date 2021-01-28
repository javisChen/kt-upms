package com.kt.upms.module.role.dto;


import com.kt.component.validator.ValidateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class RoleUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "id 不能为空", groups = {ValidateGroup.Update.class})
    private Long id;

    @NotBlank(message = "name 不能为空", groups = {ValidateGroup.Add.class})
    private String name;

    @NotNull(groups = ValidateGroup.Update.class, message = "status 不能为空")
    @Range(min = 0, max = 1)
    private Integer status;

}
