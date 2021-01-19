package com.kt.upms.module.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.upms.entity.UpmsPermission;
import com.kt.upms.entity.UpmsUser;
import com.kt.upms.module.user.dto.UserAddDTO;
import com.kt.upms.module.user.dto.UserPageListSearchDTO;
import com.kt.upms.module.user.dto.UserUpdateDTO;
import com.kt.upms.module.user.vo.UserDetailVO;
import com.kt.upms.module.user.vo.UserPageListVO;
import com.kt.upms.security.login.DefaultUser;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
public interface IUserService extends IService<UpmsUser> {

    void saveUser(UserAddDTO entity);

    void updateUserById(UserUpdateDTO userUpdateDTO);

    Page<UserPageListVO> pageList(UserPageListSearchDTO pageRequest);

    void updateStatus(UserUpdateDTO userUpdateDTO);

    /**
     * 获取用户所拥有的全部权限
     */
    List<UpmsPermission> getUserPermissions(Long userId);

    UpmsUser getUserByPhone(String username);

    UserDetailVO getUserDetailVOById(Long userId);

    DefaultUser getUserInfoByPhone(String phone);
}
