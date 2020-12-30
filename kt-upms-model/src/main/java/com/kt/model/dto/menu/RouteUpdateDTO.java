package com.kt.model.dto.menu;


import com.kt.model.validgroup.UpmsValidateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class RouteUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "id 不能为空")
    private Long id;

    private String name;

    private String code;

    private String path;

    private String icon;

    @NotNull(groups = UpmsValidateGroup.UpdateStatus.class, message = "status 不能为空")
    @Range(min = 1, max = 2)
    private Integer status;

    private Integer sequence;

    private Long pid;

}
