package com.xuen.xconfig.admin.controller;

import com.xuen.xconfig.admin.bean.APIResult;
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
    public APIResult updateConf(@RequestParam(required = true) String path,
            @RequestParam String value) {
        try {
            return xServer.updateConf(path, value);
        } catch (Exception e) {
            return new APIResult(0, "配置修改失败");
        }
    }

    @RequestMapping("/update.action")
    @ResponseBody
    public String getAllConf(@RequestParam(required = true) String path, String value) {

        return xServer.test();
    }


}
