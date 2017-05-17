package com.xuen.xconfig.admin.controller;

import com.xuen.xconfig.admin.bean.APIResult;
import com.xuen.xconfig.admin.bean.Config;
import com.xuen.xconfig.admin.server.XServiceImpl;
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
    private XServiceImpl xService;

    @RequestMapping("/update.action")
    @ResponseBody
    public APIResult updateConf(Config config) {
       return xService.updateConf(config);
    }

    @RequestMapping("/test.action")
    @ResponseBody
    public String test() {
        return xService.test();
    }

    @RequestMapping("/create.action")
    @ResponseBody
    public APIResult create(Config config) {
        return xService.createConf(config);
    }

}
