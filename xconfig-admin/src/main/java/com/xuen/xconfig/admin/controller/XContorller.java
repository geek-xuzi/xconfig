package com.xuen.xconfig.admin.controller;

import com.xuen.xconfig.admin.bean.APIResult;
import com.xuen.xconfig.admin.service.XServiceImpl;
import com.xuen.xconfig.anno.XValue;
import com.xuen.xconfig.module.Config;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
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

    @XValue("asdsa")
    private String d;

    @Resource
    private XServiceImpl xService;

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

}
