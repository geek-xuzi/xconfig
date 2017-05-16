package com.xuen.xconfig.admin.server;

import com.xuen.xconfig.anno.XValue;
import org.springframework.stereotype.Component;

/**
 * @author zheng.xu
 * @since 2017-05-16
 */
@Component
public class XServer {

    @XValue("/xuen")
    private String s = "xuzheng";

    public String test() {
        return s;
    }

}
