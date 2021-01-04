package com.kt.model.dto.user;


import com.kt.model.validgroup.UpmsValidateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @NotNull(message = "id 不能为空")
    private Long id;

    /**
     * 用户名称
     */
    private String name;

    @NotNull(groups = UpmsValidateGroup.UpdateStatus.class, message = "status 不能为空")
    @Range(min = 1, max = 2)
    private Integer status;
}
