package com.xuen.xconfig.listener;

import com.google.common.base.Preconditions;
import com.xuen.xconfig.anno.Zklis;
import com.xuen.xconfig.core.ZkListenersHolder;
import com.xuen.xconfig.util.FieldUtil;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
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
        Preconditions.checkArgument(!StringUtils.isEmpty(path), "path 不能为空");

        Map<Object, Map<String, Field>> beanXvalues = zkListenersHolder.getBeanXvalues();
        for (Map.Entry<Object, Map<String, Field>> entry : beanXvalues.entrySet()) {
            // String anyProperties = zkListenersHolder.findAnyProperties(entry.getKey());
            Map<String, Field> value = entry.getValue();
            if (value.size() < 0) {
                continue;
            }
            for (Map.Entry<String, Field> item : value.entrySet()) {
                if (path.equals(item.getKey())) {
                    FieldUtil.unsafeSetValue(entry.getKey(), item.getValue(), new String(data, "utf-8"));
                }
            }

        }
    }


}
