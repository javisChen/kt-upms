package com.kt.iam.module.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kt.iam.module.user.persistence.IamUser;
import com.kt.iam.module.permission.vo.PermissionVO;
import com.kt.iam.module.user.dto.UserPageListSearchDTO;
import com.kt.iam.module.user.dto.UserUpdateDTO;
import com.kt.iam.module.user.vo.UserDetailVO;
import com.kt.iam.module.user.vo.UserPageListVO;
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
public interface IUserService extends IService<IamUser> {

    void saveUser(UserUpdateDTO entity);

    int countUserByCode(String code);

    void updateUserById(UserUpdateDTO userUpdateDTO);

    Page<UserPageListVO> pageList(UserPageListSearchDTO pageRequest);

    void updateStatus(UserUpdateDTO userUpdateDTO);

    IamUser getUserByPhone(String username);

    List<PermissionVO> getUserElements(String userCode);

    IamUser getUserById(Long userId);

    UserDetailVO getUserDetailVOById(Long userId);

    User getUserInfoByPhone(String phone);

    IamUser getUserByCode(String userCode);

    void removeUserById(Long id);
}
