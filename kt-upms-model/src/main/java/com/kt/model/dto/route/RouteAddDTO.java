package com.kt.model.dto.route;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class RouteAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "name 不能为空")
    private String name;

    @NotNull(message = "pid 不能为空")
    private Long pid;

    @NotBlank(message = "code 不能为空")
    private String code;

    @NotNull(message = "status 不能为空")
    @Range(min = 1, max = 2)
    private Integer status;

    private String component;

    private Boolean hideChildren;

    private String path;

    private Integer type;

    private String icon;

    private Integer sequence;

    private List<Element> elements;

    @Data
    public static class Element {
        private String name;
        private Integer type;
    }
}
