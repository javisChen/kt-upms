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
    public void save(String key, Object value, long expires) {
        redisService.set(createKey(key), value, expires);
    }

    @Override
    public void remove(String key) {
        redisService.remove(createKey(key));
    }

    @Override
    public LoginUserContext get(String key) {
        Object redisValue = redisService.get(createKey(key));
        if (redisValue == null) {
            return null;
        }
        return JSONObject.parseObject((String) redisValue, LoginUserContext.class);
    }
}
