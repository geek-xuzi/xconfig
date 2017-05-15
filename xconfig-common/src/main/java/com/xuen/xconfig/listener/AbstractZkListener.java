package com.xuen.xconfig.listener;

import com.xuen.xconfig.anno.ZKListener;
import java.io.UnsupportedEncodingException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;

/**
 * @author zheng.xu
 * @since 2017-05-15
 */
public abstract class AbstractZkListener implements ZKListener {

    @Override
    public void executor(CuratorFramework client, String path) {
        final NodeCache cache = new NodeCache(client, path);
        cache.getListenable().addListener(() -> {
            byte[] data = cache.getCurrentData().getData();
            //设置日志级别
            if (data != null) {
                handle(data);
            }
        });
        try {
            cache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void handle(byte[] data) throws UnsupportedEncodingException;

}
