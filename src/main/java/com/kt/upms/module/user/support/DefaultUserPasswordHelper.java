package com.kt.upms.module.user.support;

import cn.hutool.crypto.digest.DigestUtil;
import com.kt.upms.constants.UpmsConsts;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserPasswordHelper implements IUserPasswordHelper {

    @Override
    public String enhancePassword(String password) {
        return DigestUtil.bcrypt(password + UpmsConsts.USER_SALT);
    }

    @Override
    public boolean checkPassword(String password, String passwordHashed) {
        return DigestUtil.bcryptCheck(password + UpmsConsts.USER_SALT, passwordHashed);
    }

    public static void main(String[] args) {
        String password = "888888";
        final String bcrypt = DigestUtil.bcrypt(password + UpmsConsts.USER_SALT);
        System.out.println(bcrypt);
        final boolean b = DigestUtil.bcryptCheck(password + UpmsConsts.USER_SALT, bcrypt);
        System.out.println(b);
    }
}
