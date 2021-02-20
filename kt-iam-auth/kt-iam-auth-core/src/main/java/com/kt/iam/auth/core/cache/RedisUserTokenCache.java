package com.kt.iam.auth.core.cache;

import com.kt.component.redis.RedisService;
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
    Object getCache(String key) {
        return redisService.get(key);
    }

    @Override
    void removeCache(String key) {
        redisService.remove(key);
    }

}
