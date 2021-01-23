package com.kt.upms.module.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.upms.entity.UpmsUser;
import com.kt.upms.module.permission.vo.PermissionVO;
import com.kt.upms.module.user.dto.UserAddDTO;
import com.kt.upms.module.user.dto.UserPageListSearchDTO;
import com.kt.upms.module.user.dto.UserUpdateDTO;
import com.kt.upms.module.user.vo.UserDetailVO;
import com.kt.upms.module.user.vo.UserPageListVO;
import org.springframework.security.core.userdetails.User;

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

    int countUserByCode(String code);

    void updateUserById(UserUpdateDTO userUpdateDTO);

    Page<UserPageListVO> pageList(UserPageListSearchDTO pageRequest);

    void updateStatus(UserUpdateDTO userUpdateDTO);

    UpmsUser getUserByPhone(String username);

    List<PermissionVO> getUserElements(String userCode);

    UpmsUser getUserById(Long userId);

    UserDetailVO getUserDetailVOById(Long userId);

    User getUserInfoByPhone(String phone);

    UpmsUser getUserByCode(String userCode);
}
