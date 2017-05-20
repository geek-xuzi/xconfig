package com.xuen.xconfig.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xuen.xconfig.anno.XValue;
import com.xuen.xconfig.anno.ZKListener;
import com.xuen.xconfig.module.Config;
import com.xuen.xconfig.redis.RedisClient;
import com.xuen.xconfig.util.FieldUtil;
import com.xuen.xconfig.util.PropertyUtil;
import com.xuen.xconfig.util.Safes;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author zheng.xu
 * @since 2017-05-15
 */

public class CoreHolder extends PropertyPlaceholderConfigurer implements ApplicationListener<ContextRefreshedEvent>,
        BeanPostProcessor {

    public static final int SYSTEM_PROPERTIES_MODE_FALLBACK = 1;
    private static final Logger logger = LoggerFactory.getLogger(CoreHolder.class);
    private static final AtomicBoolean INIT = new AtomicBoolean(false);
    private Map<String, ZKListener> zkListeners = Maps.newHashMap();
    private static Map<String, String> localPropertiesMap = Maps.newHashMap();
    private int springSystemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;
    private ZookeeperFactoryBean zookeeperFactoryBean;
    private Map<Object, Map<String, Field>> beanXvalues = Maps.newHashMap();
    private Config config = null;
    private RedisClient redisClient;
    private Map<String, String> values = Maps.newHashMap();
    public static String TOKEN = "";
    private Properties propertiesb = new Properties();

    public Properties getProperties() {
        return propertiesb;
    }

    public Map<String, ZKListener> getZkListeners() {
        return zkListeners;
    }

    public Map<Object, Map<String, Field>> getBeanXvalues() {
        return beanXvalues;
    }

    public Config getConfig() {
        return config;
    }

    public Map<String, String> getValues() {
        return values;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (!INIT.compareAndSet(false, true)) {
            return;
        }
        System.out.print(getProperties().toString() + "aaaaa");
        redisClient = (RedisClient) contextRefreshedEvent.getApplicationContext()
                .getBean("redisClient");
        TOKEN = PropertyUtil.getProperty("token");
        try {
            // 使用token从redis中获取该应用的配置信息
            values = redisClient.hgetall(TOKEN).get(1000, TimeUnit.MILLISECONDS);
            FieldUtil.setRemotValue(beanXvalues, values);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            logger.warn("从redis获取数据超时");
        }
        //        HttpUtil.asyncGet("www.baidu.com", (response) -> {
//            String responseBody = null;
//            try {
//                responseBody = response.getResponseBody();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            config = JsonUtil.fromJson(responseBody, Config.class);
//            config.setAppName(localPropertiesMap.get("appName"));
//        });

//        Map<String, Object> ZkListenters = contextRefreshedEvent.getApplicationContext()
//                .getBeansWithAnnotation(Zklis.class);
//
//        if (MapUtils.isEmpty(ZkListenters)) {
//            logger.warn("No ZkListenter Found!");
//            return;
//        }
//
//        ZkListenters.entrySet().stream()
//                .filter(entry -> StringUtils
//                        .isNotBlank(entry.getValue().getClass().getAnnotation(Zklis.class).path()))
//                .forEach(entry -> {
//                    Zklis zkLis = entry.getValue().getClass().getAnnotation(Zklis.class);
//                    zkListeners.put(zkLis.path(), (ZKListener) entry.getValue());
//                });
//        try {
//            zookeeperFactoryBean = contextRefreshedEvent.getApplicationContext()
//                    .getBean("zookeeperFactoryBean", ZookeeperFactoryBean.class);
//            zookeeperFactoryBean.init(zkListeners);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Safes.of(beanXvalues).entrySet().stream()
//                .filter(entry -> CollectionUtils.isNotEmpty(entry.getValue().entrySet()))
//                .forEach(entry -> {
//                    entry.getValue().entrySet().stream()
//                            .forEach(item -> {
//                                String properties = values.get(item.getKey());
//                                FieldUtil.unsafeSetValue(entry.getKey(), item.getValue(),
//                                        properties);
//                            });
//                });
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String s) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        Map<String, Field> xFields = Maps.newHashMap();
        List<Field> fields = Lists.newArrayList(bean.getClass().getDeclaredFields());
        Safes.of(fields).stream()
                .filter(input -> input != null && input.isAnnotationPresent(XValue.class))
                .forEach(input -> xFields.put(input.getAnnotation(XValue.class).value(), input));
        if (xFields.size() > 0) {
            beanXvalues.put(bean, xFields);
        }
        return bean;
    }

//    public String findAnyProperties(String key) {
//        // load
//        // TODO: 17-5-15  1 从远程加载
//        try {
//
//            String data = ZkUtils.getDataForRemoteByKey("/xuen", key,
//                    zookeeperFactoryBean.getZkClient());
//            if (data != null) {
//                return data;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // 2. find in system config
//        String s = super.resolveSystemProperty(key);
//        if (s != null) {
//            return s;
//        }
//        // 3. find in local properties
//        s = localPropertiesMap.get(key);
//        if (null != s) {
//            return s;
//        }
//        return 1 + "";
////        throw new IllegalArgumentException(
////                key + " is not found in all the Config files (local, system, xconfig)"); // f
//    }

//    @Override
//    public void setSystemPropertiesMode(int systemPropertiesMode) {
//        super.setSystemPropertiesMode(systemPropertiesMode);
//        springSystemPropertiesMode = systemPropertiesMode;
//    }

//    @Override
//    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
//            Properties props) throws BeansException {
//        super.processProperties(beanFactoryToProcess, props);
//        for (Object key : props.keySet()) {
//            String keyStr = key.toString();
//            String valueStr = resolvePlaceholder(keyStr, props, springSystemPropertiesMode);
//            localPropertiesMap.put(keyStr, valueStr);
//        }
//    }


    @Override
    protected Properties mergeProperties() throws IOException {
        Properties properties = super.mergeProperties();
        properties.put("jdbc.username", "xuen");
        properties.put("jdbc.password", "xuen");
        properties.put("jdbc.url", "jdbc:mysql://xuzi520.cn:3306/xuen?characterEncoding=utf-8&useUnicode=true&createDatabaseIfNoExist=true");
        properties.put("jdbc.driverClassName", "com.mysql.jdbc.Driver");
        this.propertiesb = properties;
        return properties;
    }
}
