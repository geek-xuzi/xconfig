package com.xuen.xconfig.redis;

import com.lambdaworks.redis.RedisFuture;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;
import java.util.Map;

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

    public RedisFuture hMSet(String key, Map map) {
        return redisAsyncCommands.hmset(key, map);
    }

    public RedisFuture hMGet(String key, String field) {
        return redisAsyncCommands.hmget(key, field);
    }

    public RedisFuture sAdd(String key, String data) {
        return redisAsyncCommands.sadd(key, data);
    }


    public  RedisFuture sMembers(String token) {
        return redisAsyncCommands.smembers(token);
    }


}
