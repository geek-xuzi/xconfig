package com.xuen.xconfig.redis;

import com.lambdaworks.redis.RedisFuture;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;

/**
 * @author zheng.xu
 * @since 2017-05-16
 */
public class RedisClient {

    private RedisAsyncCommands redisAsyncCommands;

    RedisClient(RedisAsyncCommands redisAsyncCommands) {
        this.redisAsyncCommands = redisAsyncCommands;
    }

    public RedisFuture set(String key, String value) {
        RedisFuture redisFuture = redisAsyncCommands.set(key, value);
        return redisFuture;
    }

    public RedisFuture get(String key) {
        RedisFuture<String> redisFuture = redisAsyncCommands.get(key);
        return redisFuture;
    }
}
