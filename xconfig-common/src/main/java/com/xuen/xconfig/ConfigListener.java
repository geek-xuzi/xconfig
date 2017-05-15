package com.xuen.xconfig;

import com.google.common.util.concurrent.MoreExecutors;
import com.xuen.xconfig.anno.ZkLis;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;

/**
 * @author zheng.xu
 * @since 2017-05-15
 */
@ZkLis(path = "/xuen")
public class ConfigListener implements ZKListener {

    @Override
    public void executor(CuratorFramework client, String path) {
        final NodeCache cache = new NodeCache(client, path);
        cache.getListenable().addListener(() -> {
            byte[] data = cache.getCurrentData().getData();
            if (data != null) {
                // do some thing
            }

        }, MoreExecutors.directExecutor());
    }
}
