package com.xuen.xconfig.redis;

import com.lambdaworks.redis.RedisFuture;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;
import java.util.Map;
import java.util.Set;

/**
 * @author zheng.xu
 * @since 2017-05-16
 */
public class RedisClient {

    private RedisAsyncCommands<String, String> redisAsyncCommands;

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

    public RedisFuture hSet(String key, String field, String value) {
        return redisAsyncCommands.hset(key, field, value);
    }

    public RedisFuture hMGet(String key, String field) {
        return redisAsyncCommands.hmget(key, field);
    }

    public RedisFuture<Map<String, String>> hgetall(String key) {
        RedisFuture<Map<String, String>> result = redisAsyncCommands.hgetall(key);
        return result;
    }

    public RedisFuture sAdd(String key, String data) {
        return redisAsyncCommands.sadd(key, data);
    }


    public RedisFuture<Set<String>> sMembers(String token) {
        RedisFuture<Set<String>> result = redisAsyncCommands.smembers(token);
        return result;
    }


}
