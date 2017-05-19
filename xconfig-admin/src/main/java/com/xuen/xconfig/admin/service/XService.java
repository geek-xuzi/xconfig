package com.xuen.xconfig.admin.service;

import com.xuen.xconfig.admin.bean.APIResult;
import com.xuen.xconfig.module.Config;
import java.util.List;

/**
 * @author zheng.xu
 * @since 2017-05-17
 */
public interface XService {

    APIResult createConf(Config config);

    APIResult updateConf(Config config);

    List<Config> getConf(String token);

    String test();
}
