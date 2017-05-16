package com.xuen.xconfig.listener;

import com.google.common.base.Throwables;
import com.xuen.xconfig.anno.Zklis;
import com.xuen.xconfig.core.QPropertiesEditor;
import com.xuen.xconfig.core.ZkListenersHolder;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zheng.xu
 * @since 2017-05-15
 */
@Zklis(path = "/xuen")
public class ConfigListener extends AbstractZkListener {

    private static final Logger logger = LoggerFactory.getLogger(ConfigListener.class);

    @Resource
    private ZkListenersHolder zkListenersHolder;

    @Override
    protected void handle(byte[] data, String path) throws UnsupportedEncodingException {
        System.out.println("Setting logback new level to :" + new String(data, "utf-8"));
        Map<Object, Map<String, Field>> beanXvalues = zkListenersHolder.getBeanXvalues();
        for (Map.Entry<Object, Map<String, Field>> entry : beanXvalues.entrySet()) {
            // String anyProperties = zkListenersHolder.findAnyProperties(entry.getKey());
            Map<String, Field> value = entry.getValue();
            if (value.size() < 0) {
                continue;
            }

            for (Map.Entry<String, Field> item : value.entrySet()) {
                if (path.equals(item.getKey())) {
                    unsafeSetValue(entry.getKey(), item.getValue(), new String(data, "utf-8"));
                }
            }

        }
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
}
