package com.xuen.xconfig.util;

import com.google.common.base.Throwables;
import com.xuen.xconfig.core.QPropertiesEditor;
import java.lang.reflect.Field;

/**
 * @author zheng.xu
 * @since 2017-05-16
 */
public class FieldUtil {

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
}
