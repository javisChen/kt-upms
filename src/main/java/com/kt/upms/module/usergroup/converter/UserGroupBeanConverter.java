package com.kt.upms.module.usergroup.converter;
import com.kt.upms.entity.UpmsUserGroup;
import com.kt.upms.module.usergroup.vo.UserGroupListTreeVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Component
public class UserGroupBeanConverter {

    public UserGroupListTreeVO convertToUserGroupListTreeVO(UpmsUserGroup item) {
        UserGroupListTreeVO vo = new UserGroupListTreeVO();
        vo.setId(item.getId());
        vo.setPid(item.getPid());
        vo.setName(item.getName());
        vo.setStatus(item.getStatus());
        vo.setCreateTime(item.getGmtCreate());
        vo.setUpdateTime(item.getGmtModified());
        vo.setChildren(new ArrayList<>());
        return vo;
    }
}
