package com.xuen.xconfig.admin.controller;

import com.xuen.xconfig.admin.bean.APIResult;
import com.xuen.xconfig.admin.bean.Config;
import com.xuen.xconfig.admin.bean.ConfigStatus;
import com.xuen.xconfig.admin.server.XServer;
import com.xuen.xconfig.core.ZookeeperFactoryBean;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zheng.xu
 * @since 2017-05-15
 */
@RequestMapping("/xconfig")
@Controller
public class XContorller {

    @Resource
    private XServer xServer;

    @RequestMapping("/update.action")
    @ResponseBody
    public APIResult updateConf(@RequestParam String path,
            @RequestParam String value) {
        try {
            System.out.println(path + ":" + value);
            return xServer.updateConf(path, value);
        } catch (Exception e) {
            return new APIResult(0, "配置修改失败");
        }
    }

    @RequestMapping("/test.action")
    @ResponseBody
    public String test() {
        return xServer.test();
    }

    @RequestMapping("/create.action")
    @ResponseBody
    public APIResult create(@RequestParam Config config) {
        return xServer.create(config);
    }

}
