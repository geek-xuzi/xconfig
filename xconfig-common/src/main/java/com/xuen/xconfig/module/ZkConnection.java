package com.xuen.xconfig.module;

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
}
