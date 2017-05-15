package com.xuen.xconfig.core;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xuen.xconfig.anno.XValue;
import com.xuen.xconfig.anno.ZKListener;
import com.xuen.xconfig.anno.Zklis;
import com.xuen.xconfig.util.Safes;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Resource;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author zheng.xu
 * @since 2017-05-15
 */

@Component
public class ZkListenersHolder extends PropertyPlaceholderConfigurer implements
        ApplicationListener<ContextRefreshedEvent>,
        BeanPostProcessor {

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

        Map<String, Object> ZkListenters = contextRefreshedEvent.getApplicationContext()
                .getBeansWithAnnotation(Zklis.class);

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

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        List<Field> fields = Lists.newArrayList(bean.getClass().getDeclaredFields());
        Safes.of(fields).stream()
                .filter(input -> input != null && input.isAnnotationPresent(XValue.class))
                .forEach(input -> {
                    String property = loadValue(input.getAnnotation(XValue.class).value());
                    unsafeSetValue(bean, input, property);
                });
        return null;
    }

    private void unsafeSetValue(Object target, Field field, String value) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(target, QPropertiesEditor.INSTANCE.apply(value, field.getType()));
            logger.debug("MConfig-Driven set bean : {}, field : {}, value : {}",
                    target.getClass().getSimpleName(), field.getName(), value);
        } catch (Throwable ignore) { // ignore
            logger.error(
                    "MConfig-Driven dynamic reload qConfig error. Please check the bean:{}, field: {}",
                    target.getClass().getSimpleName(), field.getName(), ignore);
            throw Throwables.propagate(ignore);
        } finally { // fix accessible
            field.setAccessible(accessible);
        }
    }

    private String loadValue(String key) {
        // load
        // TODO: 17-5-15  1 从zk加载
        CuratorFramework zkClient = zookeeperFactoryBean.getZkClient();
        try {
            byte[] data = zkClient.getData().watched().forPath(key);
            if (data != null) {
                return new String(data, "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 2. find in system config
        String s = super.resolveSystemProperty(key);
        if (s != null) {
            return s;
        }

        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {

        return null;
    }
}
