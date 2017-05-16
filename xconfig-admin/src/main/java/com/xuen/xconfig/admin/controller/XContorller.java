package com.xuen.xconfig.admin.controller;

import com.xuen.xconfig.admin.server.XServer;
import com.xuen.xconfig.core.ZookeeperFactoryBean;
import javax.annotation.Resource;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zheng.xu
 * @since 2017-05-15
 */
@RequestMapping("/xconfig")
@Controller
public class XContorller {

    @Resource
    private ZookeeperFactoryBean zookeeperFactoryBean;

    @Resource
    private XServer xServer;

    @RequestMapping("/update.action")
    @ResponseBody
    public String updateConf(String path) throws Exception {
        CuratorFramework zkClient = zookeeperFactoryBean.getZkClient();
        System.out.println(xServer.test());
        return xServer.test();
    }

}
