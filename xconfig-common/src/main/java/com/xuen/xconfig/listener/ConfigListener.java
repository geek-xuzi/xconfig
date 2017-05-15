package com.xuen.xconfig.listener;

import com.xuen.xconfig.anno.Zklis;
import java.io.UnsupportedEncodingException;

/**
 * @author zheng.xu
 * @since 2017-05-15
 */
@Zklis(path = "/xuen")
public  class ConfigListener extends AbstractZkListener {


    @Override
    protected void handle(byte[] data) throws UnsupportedEncodingException {
        System.out.println("Setting logback new level to :" + new String(data, "utf-8"));
    }
}
