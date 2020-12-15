package com.kt.model.dto.menu;


import com.kt.model.validgroup.UpmsValidateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MenuUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "id 不能为空")
    private Long id;

    private String name;

    private String path;

    private String icon;

    @NotNull(groups = UpmsValidateGroup.UpdateStatus.class, message = "status 不能为空")
    @Range(min = 0, max = 1)
    private Integer status;

}
