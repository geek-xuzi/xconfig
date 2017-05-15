package com.xuen.xconfig.anno;

import com.google.common.collect.Maps;
import com.xuen.xconfig.ZKListener;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author zheng.xu
 * @since 2017-05-15
 */
public class ZkListenersHolder implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ZkListenersHolder.class);

    private static final AtomicBoolean INIT = new AtomicBoolean(false);

    private Map<String, ZKListener> zkListeners = Maps.newHashMap();

    public Map<String, ZKListener> getZkListeners() {
        return zkListeners;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (!INIT.compareAndSet(false, true)) {
            return;
        }

        Map<String, Object> ZkListenters = contextRefreshedEvent.getApplicationContext()
                .getBeansWithAnnotation(ZkLis.class);

        if (MapUtils.isEmpty(ZkListenters)) {
            logger.warn("No Hos Checkers Found!");
            return;
        }

        ZkListenters.entrySet().stream()
                .filter(entry -> StringUtils
                        .isNotBlank(entry.getValue().getClass().getAnnotation(ZkLis.class).path()))
                .forEach(entry -> {
                    ZkLis zkLis = entry.getValue().getClass().getAnnotation(ZkLis.class);
                    zkListeners.put(zkLis.path(), (ZKListener) entry.getValue());
                });
    }
}
