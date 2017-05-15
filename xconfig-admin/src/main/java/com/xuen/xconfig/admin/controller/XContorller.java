package com.xuen.xconfig.admin.controller;

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

    @RequestMapping("/zk.action")
    @ResponseBody
    public String test() throws Exception {
        CuratorFramework zkClient = zookeeperFactoryBean.getZkClient();
        zkClient.setData().forPath("/xuen","asda".getBytes());
        final NodeCache cache = new NodeCache(zkClient, "/xuen");
        cache.getListenable().addListener(() -> {
            byte[] data = cache.getCurrentData().getData();
            //设置日志级别
            if (data != null) {
                System.out.println("Setting logback new level to :" + new String(data, "utf-8"));
            }
        });
        System.out.println("aaa");
        return "ok";
    }


}
