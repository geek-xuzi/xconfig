package com.xuen.xconfig.admin.server;

import com.xuen.xconfig.admin.bean.APIResult;
import com.xuen.xconfig.admin.bean.Config;

/**
 * @author zheng.xu
 * @since 2017-05-17
 */
public interface XService {

    APIResult createConf(Config config);

    APIResult updateConf(Config config);
}
