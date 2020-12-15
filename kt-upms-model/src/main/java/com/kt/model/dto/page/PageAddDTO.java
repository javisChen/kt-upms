package com.kt.model.dto.page;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class PageAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "name 不能为空")
    private String name;

}
