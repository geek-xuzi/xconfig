package com.xuen.xconfig.core;

import com.xuen.xconfig.redis.RedisClient;
import com.xuen.xconfig.util.FieldUtil;
import com.xuen.xconfig.util.Safes;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author zheng.xu
 * @since 2017-05-16
 */
@Component
public class RedisHandler {

    private static final long INITIALDELAY = 10;
    private static final long DELAY = 60;

    @Resource
    private RedisClient redisClient;

    @Resource
    private ZkListenersHolder zkListenersHolder;

//    @PostConstruct
    public void executor() {
        Map<Object, Map<String, Field>> beanXvalues = zkListenersHolder.getBeanXvalues();
        ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(10);
        scheduledService.scheduleWithFixedDelay(() ->
                        redisClient.get("xuen-str").whenComplete((o1, o2) -> {
                            Safes.of(beanXvalues).entrySet().stream()
                                    .filter(entry -> entry.getValue().size() > 0)
                                    .forEach(entry1 -> {
                                        entry1.getValue().entrySet().stream()
                                                .forEach(item ->
                                                        FieldUtil.unsafeSetValue(entry1.getKey()
                                                                , item.getValue(), o1.toString()));
                                    });
                        })

                , INITIALDELAY, DELAY, TimeUnit.SECONDS);
    }


}
