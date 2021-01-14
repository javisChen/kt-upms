package com.kt.upms.module.auth.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.kt.upms.entity.UpmsUser;
import com.kt.upms.enums.BizEnums;
import com.kt.upms.module.auth.dto.LoginDTO;
import com.kt.upms.module.auth.dto.LoginUserDTO;
import com.kt.upms.module.user.service.IUpmsUserService;
import com.kt.upms.support.IUserPasswordHelper;
import com.kt.upms.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private IUpmsUserService iUpmsUserService;
    @Autowired
    private IUserPasswordHelper iUserPasswordHelper;
    @Override
    public LoginUserDTO login(LoginDTO dto) {
        UpmsUser upmsUser = iUpmsUserService.getUserByPhone(dto.getUsername());

        checkUser(upmsUser);

        checkPassword(upmsUser.getPassword(), dto.getPassword());

        return assembleLoginUserDTO(upmsUser);
    }

    private void checkUser(UpmsUser upmsUser) {
        Assert.isTrue(upmsUser == null, BizEnums.USER_LOGIN_INVALID);
    }

    private LoginUserDTO assembleLoginUserDTO(UpmsUser upmsUser) {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setId(String.valueOf(upmsUser.getId()));
        loginUserDTO.setName(upmsUser.getName());
        loginUserDTO.setAvatar("");
        loginUserDTO.setStatus(0);
        loginUserDTO.setRoleId("");
        loginUserDTO.setToken(StrUtil.uuid());
        return loginUserDTO;
    }

    private void checkPassword(String passwordHashed, String password) {
        boolean check = iUserPasswordHelper.checkPassword(DigestUtil.md5Hex(password), passwordHashed);
        Assert.isFalse(check, BizEnums.USER_LOGIN_INVALID);
    }
}
