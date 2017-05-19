package com.xuen.xconfig.core;

import com.xuen.xconfig.redis.RedisClient;
import com.xuen.xconfig.util.FieldUtil;
import com.xuen.xconfig.util.Safes;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

/**
 * @author zheng.xu
 * @since 2017-05-16
 */
@Component
public class RedisHandler {

    private static final long INITIALDELAY = 10;
    private static final long DELAY = 30;

    @Resource
    private RedisClient redisClient;

    @Resource
    private CoreHolder coreHolder;

    @PostConstruct
    public void executor() {
        Map<Object, Map<String, Field>> beanXvalues = coreHolder.getBeanXvalues();
        ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(10);
        scheduledService.scheduleWithFixedDelay(() -> {
            redisClient.hgetall(CoreHolder.TOKEN).whenComplete((value, error) -> {
                System.out.println(value.toString());
                FieldUtil.setRemotValue(beanXvalues, value);
            });
        }, INITIALDELAY, DELAY, TimeUnit.SECONDS);
    }


}
