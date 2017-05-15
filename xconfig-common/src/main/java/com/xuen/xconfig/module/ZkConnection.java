package com.xuen.xconfig.module;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017/5/14.
 */
public class ZkConnection {


    private String zkConnectionStr;

    public ZkConnection(String zkConnectionStr) {
        this.zkConnectionStr = zkConnectionStr;
    }

    public String getZkConnectionStr() {
        return zkConnectionStr;
    }

    public void setZkConnectionStr(String zkConnectionStr) {
        this.zkConnectionStr = zkConnectionStr;
    }

    public static void main(String[] args) {

        new Thread(() -> {
            Logger logger = LoggerFactory.getLogger("root");
            String zkTestStr = "xuzi520.cn:2181,xuzi520.cn:2182,xuzi520.cn:2183";
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                    .connectString(zkTestStr)
                    .retryPolicy(retryPolicy)
                    .connectionTimeoutMs(2000)
                    .sessionTimeoutMs(10000)
                    .build();

            zkClient.getConnectionStateListenable()
                    .addListener((curatorFramewor, connectionState) -> {
                        logger.info("CuratorFramework state changed: {}", connectionState);
                        if (connectionState == ConnectionState.CONNECTED
                                || connectionState == ConnectionState.RECONNECTED) {
                            //使用Curator的NodeCache来做ZNode的监听，不用我们自己实现重复监听
                            final NodeCache cache = new NodeCache(zkClient, "/xuen");
                            cache.getListenable().addListener(new NodeCacheListener() {
                                @Override
                                public void nodeChanged() throws Exception {

                                    byte[] data = cache.getCurrentData().getData();
                                    String s = new String(data, "utf-8");
                                    System.out.println(s);
                                }
                            });
                            try {
                                cache.start(true);
                            } catch (Exception e) {
                            }

                        }
                    });
            zkClient.start();
        }).start();
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
