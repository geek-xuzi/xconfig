package com.xuen.xconfig.listener;

import com.google.common.util.concurrent.MoreExecutors;
import com.xuen.xconfig.anno.ZKListener;
import com.xuen.xconfig.anno.Zklis;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;

/**
 * @author zheng.xu
 * @since 2017-05-15
 */
@Zklis(path = "/xuen")
public class ConfigListener implements ZKListener {

    @Override
    public void executor(CuratorFramework client, String path) {
        final NodeCache cache = new NodeCache(client, path);
        cache.getListenable().addListener(() -> {
            byte[] data = cache.getCurrentData().getData();
            if (data != null) {
                // do some thing
            }
            System.out.println("xuzi" + new String(data, "utf-8"));

        }, MoreExecutors.directExecutor());
    }
}
