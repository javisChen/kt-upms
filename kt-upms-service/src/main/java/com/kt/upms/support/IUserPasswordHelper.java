package com.kt.upms.support;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
public interface IUserPasswordHelper {

    String enhancePassword(String password);

    boolean checkPassword(String password, String passwordHashed);
}
