package com.kt.model.dto.role;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class RoleUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "id 不能为空")
    private Long id;

    private String name;

}
