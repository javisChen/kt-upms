package com.kt.upms.security.cache;

import com.alibaba.fastjson.JSONObject;
import com.kt.component.dto.auth.LoginUser;
import com.kt.component.redis.RedisService;
import com.kt.upms.security.login.DefaultUser;
import com.kt.upms.security.configuration.SecurityCoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;

import static com.kt.upms.security.common.RedisKeyConst.USER_ACCESS_TOKEN_KEY_PREFIX;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
public class RedisUserCache implements UserCache {

    @Autowired
    private RedisService redisService;
    @Autowired
    private SecurityCoreProperties securityCoreProperties;

    @Override
    public UserDetails getUserFromCache(String token) {
        return (UserDetails) redisService.get(token);
    }

    @Override
    public void putUserInCache(UserDetails userDetails) {
        DefaultUser user = (DefaultUser) userDetails;
        LoginUser loginUser = user.getLoginUser();
        redisService.set(createAccessTokenKey(loginUser.getAccessToken()), JSONObject.toJSONString(userDetails),
                securityCoreProperties.getLogin().getExpire());
    }

    private String createAccessTokenKey(String accessToken) {
        return USER_ACCESS_TOKEN_KEY_PREFIX + accessToken;
    }

    @Override
    public void removeUserFromCache(String key) {
        redisService.remove(key);
    }
}
