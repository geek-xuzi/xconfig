package com.xuen.xconfig.core;

import com.xuen.xconfig.anno.ZKListener;
import java.util.Map;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZookeeperFactoryBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private CuratorFramework zkClient;
    private String zkConnectionString;

    public CuratorFramework getZkClient() {
        return zkClient;
    }

    // 设置ZK链接字符串
    public ZookeeperFactoryBean(String zkConnectionString) {
        this.zkConnectionString = zkConnectionString;
    }


    public void init(Map<String, ZKListener> listeners) throws Exception {
        // 1000 是重试间隔时间基数，3 是重试次数
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        zkClient = createWithOptions(zkConnectionString, retryPolicy, 2000, 10000);
        registerListeners(zkClient, listeners);
        zkClient.start();
    }

    private void registerListeners(CuratorFramework zkClient, Map<String, ZKListener> listeners) {
        zkClient.getConnectionStateListenable().addListener((curatorFramewor, connectionState) -> {
            logger.info("CuratorFramework state changed: {}", connectionState);
            if (connectionState == ConnectionState.CONNECTED
                    || connectionState == ConnectionState.RECONNECTED) {
                for (Map.Entry<String, ZKListener> listener : listeners.entrySet()) {
                    listener.getValue().executor(zkClient, listener.getKey());
                }
            }
        });

        zkClient.getUnhandledErrorListenable().addListener(
                (message, e) -> logger.info("CuratorFramework unhandledError: {}", message));
    }

    // 自行设置连接参数
    public CuratorFramework createWithOptions(String zkConnectionString, RetryPolicy retryPolicy,
            int connectionTimeoutMs, int sessionTimeoutMs) {
        return CuratorFrameworkFactory.builder()
                .connectString(zkConnectionString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
    }

}
