package com.xuen.xconfig;

import org.apache.curator.framework.CuratorFramework;

/**
 * Created by Administrator on 2017/5/14.
 */
public interface ZKListener {

    void executor(CuratorFramework client);

}
