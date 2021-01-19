package com.kt.upms.security.token.manager;

import com.kt.upms.security.login.LoginUserDetails;

/**
 * @author JarvisChen
 * @desc
 * @date 2019-10-19
 * @time 17:05
 */
public interface UserTokenManager {

    /**
     * 保存token
     */
    void save(String key, Object value, long expires);

    /**
     * 移除token
     *
     * @param key token key
     */
    void remove(String key);

    /**
     * 获取token
     * @param key token key
     * @return TokenStorageDTO
     */
    LoginUserDetails get(String key);

    /**
     * 检查token有效期
     * @param tokenStorageDTO
     */
//    default boolean isVaild(TokenStorageDTO tokenStorageDTO) {
//        return tokenStorageDTO != null;
//        if (tokenStorageDTO == null) {
//            throw new ExpiredTokenException("令牌无效");
//        }
//        Long expire = tokenStorageDTO.getExpire();
//        if (LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) > expire) {
//            throw new ExpiredTokenException("令牌已过期");
//        }
//    }
}
