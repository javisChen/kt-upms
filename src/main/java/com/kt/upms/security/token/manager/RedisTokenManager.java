package com.kt.upms.security.token.manager;

import com.alibaba.fastjson.JSONObject;
import com.kt.component.redis.RedisService;
import com.kt.upms.security.login.LoginUserDetails;
import lombok.extern.slf4j.Slf4j;

/**
 * RedisToken管理器
 */
@Slf4j
public class RedisTokenManager implements UserTokenManager {

    public RedisTokenManager() {
        log.info("UserTokenManager ---------> LocalCacheTokenManager");
    }

    private RedisService redisService;

    public RedisTokenManager(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void save(String key, Object value, long expires) {
        redisService.set(key, value, expires);
    }

    @Override
    public void remove(String key) {
        redisService.remove(key);
    }

    @Override
    public LoginUserDetails get(String key) {
        Object redisValue = redisService.get(key);
        if (redisValue == null) {
            return null;
        }
        return JSONObject.parseObject((String) redisValue, LoginUserDetails.class);
    }
}
