package com.xuen.xconfig.redis;

import com.lambdaworks.redis.api.async.RedisAsyncCommands;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.xuen.lettuceredis.RedisClientBuilder;
import com.xuen.lettuceredis.bean.Server;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zheng.xu
 * @since 2017-05-16
 */
public class RedisFartory {

    //单机版,不建议使用
    public static RedisAsyncCommands createSimpleAsync(String host, int port, String password) {
        return RedisClientBuilder
                .createSingle(host, port)
                .setPassword(password)
                .buildAsync();
    }

    // 不建议使用, 建议直接用异步
    public static RedisCommands<String, String> createSimpleSync(String host, int port,
            String password,
            int opTimeout) {
        return RedisClientBuilder
                .createSingle(host, port)
                .setPassword(password)
                .buildSync();
    }

    // 集群版
    public static RedisAsyncCommands createAsyncRedisClient(String masterId, String password,
            List<Server> sentinelServers) {
        return RedisClientBuilder
                .createSentinel(masterId, sentinelServers)
                .setTimeout(1000)
                .setTimeoutUnit(TimeUnit.MILLISECONDS)
                .setPassword(password)
                .buildAsync();
    }

    // 不建议使用, 建议直接用异步
    public static RedisCommands createSyncRedisClient(String masterId, String password,
            List<Server> sentinelServers) {
        return RedisClientBuilder
                .createSentinel(masterId, sentinelServers)
                .setTimeout(1000)
                .setTimeoutUnit(TimeUnit.MILLISECONDS)
                .setPassword(password)
                .buildSync();
    }

}
