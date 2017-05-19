package com.xuen.admin.controller;

import com.xuen.admin.service.XService;
import com.xuen.admin.bean.APIResult;
import com.xuen.xconfig.module.Config;
import java.util.List;
import javax.annotation.Resource;
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
    private XService xService;

    @RequestMapping("/update.action")
    @ResponseBody
    public APIResult updateConf(Config config) {
        return xService.updateConf(config);
    }

    @RequestMapping("/create.action")
    @ResponseBody
    public APIResult create(Config config) {
        return xService.createConf(config);
    }

    @RequestMapping("/get.action")
    @ResponseBody
    public List<Config> getConf(String token) {
        return xService.getConf(token);
    }

    @RequestMapping("/test.action")
    @ResponseBody
    public String test() {
        return xService.test();
    }

}
