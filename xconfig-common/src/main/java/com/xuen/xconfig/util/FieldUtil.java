package com.xuen.xconfig.util;

import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zheng.xu
 * @since 2017-05-16
 */
public class FieldUtil {

    static final private Splitter splitter0 = Splitter.on(",");
    static final private Splitter splitter1 = Splitter.on("=");

    public static void unsafeSetValue(Object target, Field field, String value) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(target, QPropertiesEditor.INSTANCE.apply(value, field.getType()));

        } catch (Throwable ignore) { // ignore
            throw Throwables.propagate(ignore);
        } finally { // fix accessible
            field.setAccessible(accessible);
        }
    }

    public static Map<String, String> parseConfig(String str) {
        Map<String, String> result = Maps.newHashMap();
        Iterable<String> values = splitter0.split(str);
        values.forEach(item -> {
            Iterator<String> iterator = splitter1.split(item).iterator();
            result.put(iterator.next(), iterator.next());
        });
        return result;
    }
}
