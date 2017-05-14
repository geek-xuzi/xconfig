package com.xuen.xconfig.module;

import com.xuen.xconfig.core.ZookeeperFactoryBean;
import org.apache.curator.framework.CuratorFramework;

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
        ZookeeperFactoryBean zookeeperFactoryBean = new ZookeeperFactoryBean();
        zookeeperFactoryBean.setZkConnectionString("xuzi520.cn:2801");
        try {
            CuratorFramework zkCilent = zookeeperFactoryBean.getObject();
            System.out.print(zkCilent.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
