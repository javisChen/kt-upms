package com.kt.upms.auth.core.cache;

import com.alibaba.fastjson.JSONObject;
import com.kt.component.redis.RedisService;
import com.kt.upms.auth.core.model.LoginUserContext;
import lombok.extern.slf4j.Slf4j;

/**
 * RedisToken管理器
 */
@Slf4j
public class RedisUserTokenCache extends AbstractUserTokenCache {

    private RedisService redisService;

    public RedisUserTokenCache(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    void saveCache(String key, Object value, long expires) {
        redisService.set(key, value, expires);
    }

    @Override
    LoginUserContext getCache(String key) {
        Object redisValue = redisService.get(key);
        if (redisValue == null) {
            return null;
        }
        return JSONObject.parseObject((String) redisValue, LoginUserContext.class);
    }

    @Override
    void removeCache(String key) {
        redisService.remove(key);
    }

}
