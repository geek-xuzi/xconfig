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
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
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

    public static final int SYSTEM_PROPERTIES_MODE_FALLBACK = 1;

    private static final Logger logger = LoggerFactory.getLogger(ZkListenersHolder.class);

    private static final AtomicBoolean INIT = new AtomicBoolean(false);

    private Map<String, ZKListener> zkListeners = Maps.newHashMap();

    private static Map<String, String> localPropertiesMap = Maps.newHashMap();

    private int springSystemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;
    private ZookeeperFactoryBean zookeeperFactoryBean;


    public Map<String, ZKListener> getZkListeners() {
        return zkListeners;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (!INIT.compareAndSet(false, true)) {
            return;
        }

        System.out.println("onApplicationEvent");
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
            zookeeperFactoryBean = contextRefreshedEvent.getApplicationContext()
                    .getBean("zookeeperFactoryBean", ZookeeperFactoryBean.class);
            zookeeperFactoryBean.init(zkListeners);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        System.out.println("postProcessBeforeInitialization");
        return bean;
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

    private String findAnyProperties(String key) {
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
        // 3. find in local properties
        s = localPropertiesMap.get(key);
        if (null != s) {
            return s;
        }
        throw new IllegalArgumentException(
                key + " is not found in all the Config files (local, system, qconfig)"); // f
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String s) throws BeansException {
        System.out.println("postProcessAfterInitialization");
        List<Field> fields = Lists.newArrayList(bean.getClass().getDeclaredFields());
        Safes.of(fields).stream()
                .filter(input -> input != null && input.isAnnotationPresent(XValue.class))
                .forEach(input -> {
                    String property = findAnyProperties(input.getAnnotation(XValue.class).value());
                    unsafeSetValue(bean, input, property);
                });
        return bean;
    }

    @Override
    public void setSystemPropertiesMode(int systemPropertiesMode) {
        super.setSystemPropertiesMode(systemPropertiesMode);
        springSystemPropertiesMode = systemPropertiesMode;
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
            Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String valueStr = resolvePlaceholder(keyStr, props, springSystemPropertiesMode);
            localPropertiesMap.put(keyStr, valueStr);
        }

    }
}
