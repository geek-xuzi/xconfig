package com.xuen.xconfig.admin.server;

import com.xuen.xconfig.anno.XValue;
import com.xuen.xconfig.core.ZkListenersHolder;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author zheng.xu
 * @since 2017-05-16
 */
@Component
public class XServer {

    @XValue("/xuen")
    private String s = "xuzheng";

    @Resource
    private ZkListenersHolder zkListenersHolder;

    public String test() {
        return s;
    }

    public String updateConf(String path, String value) {
//        Pro

        return null;
    }
}
