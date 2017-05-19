package com.xuen.admin.service;

import com.xuen.admin.bean.APIResult;

/**
 * @author zheng.xu
 * @since 2017-05-19
 */
public interface DbService {

    APIResult createApp(String appName);

    APIResult deleteApp(Integer aid);
}
