package com.xuen.xconfig.core;

import com.google.common.collect.Maps;
import com.xuen.xconfig.anno.ZKListener;
import com.xuen.xconfig.anno.Zklis;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Resource;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author zheng.xu
 * @since 2017-05-15
 */

@Component
public class ZkListenersHolder implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ZkListenersHolder.class);

    private static final AtomicBoolean INIT = new AtomicBoolean(false);

    private Map<String, ZKListener> zkListeners = Maps.newHashMap();

    @Resource
    private ZookeeperFactoryBean zookeeperFactoryBean;


    public Map<String, ZKListener> getZkListeners() {
        return zkListeners;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (!INIT.compareAndSet(false, true)) {
            return;
        }

        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, Object> ZkListenters = applicationContext.getBeansWithAnnotation(Zklis.class);

        if (MapUtils.isEmpty(ZkListenters)) {
            logger.warn("No ZkListenter Found!");
            return;
        }

        ZkListenters.entrySet().stream()
                .filter(entry -> StringUtils
                        .isNotBlank(entry.getValue().getClass().getAnnotation(Zklis.class).path()))
                .forEach(entry -> {
                    Zklis zkLis = entry.getValue().getClass().getAnnotation(Zklis.class);
                    zkListeners.put(zkLis.path(), (ZKListener) entry.getValue());
                });

        try {
            zookeeperFactoryBean.init(zkListeners);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
