package com.xuen.xconfig.listener;

import com.google.common.util.concurrent.MoreExecutors;
import com.xuen.xconfig.anno.ZKListener;
import java.io.UnsupportedEncodingException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

/**
 * @author zheng.xu
 * @since 2017-05-15
 */
public abstract class AbstractZkListener implements ZKListener {

    @Override
    public void executor(CuratorFramework client, String path) {
        final PathChildrenCache cache = new PathChildrenCache(client, path, true);

        cache.getListenable().addListener((client1, event) -> {
            ChildData data = event.getData();
            switch (event.getType()) {
                case CHILD_ADDED:
                    System.out.println("CHILD_ADDED : "+ data.getPath() +"  数据:"+ data.getData());
                    break;
                case CHILD_REMOVED:
                    System.out.println("CHILD_REMOVED : "+ data.getPath() +"  数据:"+ data.getData());
                    handle(data.getData(),data.getPath());
                    break;
                case CHILD_UPDATED:
                    System.out.println("CHILD_UPDATED : "+ data.getPath() +"  数据:"+ data.getData());
                    break;
                default:
                    break;
//                //设置日志级别
//            if (data != null) {
//                handle(data, path);
//            }
        }}, MoreExecutors.directExecutor());
        try {
            cache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void handle(byte[] data, String path) throws UnsupportedEncodingException;

}
