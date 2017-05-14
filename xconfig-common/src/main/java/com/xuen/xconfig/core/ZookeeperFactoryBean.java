package com.xuen.xconfig.core;

import com.xuen.xconfig.ZKListener;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * zheng.xu
 * 2017/5/14.
 */
public class ZookeeperFactoryBean implements FactoryBean<CuratorFramework>, InitializingBean, DisposableBean {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private CuratorFramework zkClient;
    private String zkConnectionString;
    private List<ZKListener> listeners;

    public void setListeners(List<ZKListener> listeners) {
        this.listeners = listeners;
    }

    public void setZkClient(CuratorFramework zkClient) {
        this.zkClient = zkClient;
    }

    // 设置ZK链接字符串
    public void setZkConnectionString(String zkConnectionString) {
        this.zkConnectionString = zkConnectionString;
    }

    @Override
    public void destroy() throws Exception {
        zkClient.close();
    }

    @Override
    public CuratorFramework getObject() throws Exception {
        return zkClient;
    }

    @Override
    public Class<?> getObjectType() {
        return CuratorFramework.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 1000 是重试间隔时间基数，3 是重试次数
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        zkClient = createWithOptions(zkConnectionString, retryPolicy, 2000, 10000);
        registerListeners(zkClient);
        zkClient.start();
    }

    private void registerListeners(CuratorFramework zkClient) {
        zkClient.getConnectionStateListenable().addListener((curatorFramewor, connectionState) -> {
            logger.info("CuratorFramework state changed: {}", connectionState);
            if (connectionState == ConnectionState.CONNECTED || connectionState == ConnectionState.RECONNECTED) {
                for (ZKListener listener : listeners) {
                    listener.executor(zkClient);
                    logger.info("Listener {} executed!", listener.getClass().getName());
                }
            }
        });

        zkClient.getUnhandledErrorListenable().addListener((message, e) -> logger.info("CuratorFramework unhandledError: {}", message));
    }

    // 自行设置连接参数
    public CuratorFramework createWithOptions(String zkConnectionString, RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs) {
        return CuratorFrameworkFactory.builder()
                .connectString(zkConnectionString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
    }

}
