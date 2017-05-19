package com.xuen.admin.controller;

import com.xuen.admin.bean.APIResult;
import com.xuen.admin.service.DbService;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zheng.xu
 * @since 2017-05-19
 */
@RequestMapping("/xconfig")
@Controller
public class DbController {

    @Resource
    private DbService dbService;

    @RequestMapping("/app/add.action")
    @ResponseBody
    public APIResult create(@RequestParam String appName) {
        return dbService.createApp(appName);
    }

    @RequestMapping("/app/delete.action")
    @ResponseBody
    public APIResult create(@RequestParam Integer aid) {
        return dbService.deleteApp(aid);
    }
}
