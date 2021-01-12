package com.kt.model.vo.usergroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupVO implements Serializable {

    private Long id;
    private Long pid;
    private Integer level;
    private String name;
    private Integer status;

}
