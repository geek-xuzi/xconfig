package com.xuen.admin.controller;

import com.xuen.admin.service.XService;
import com.xuen.admin.bean.APIResult;
import com.xuen.xconfig.module.Config;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zheng.xu
 * @since 2017-05-15
 */
@RequestMapping("/xconfig/prop")
@Controller
public class ConfigContorller {

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

    @RequestMapping("/upload.action")
    @ResponseBody
    public APIResult upload(MultipartFile file, String token) {
        try {
            return xService.upload(file, token);
        } catch (IOException e) {
            e.printStackTrace();
            return new APIResult(0, "更新失败");
        }
    }

}
