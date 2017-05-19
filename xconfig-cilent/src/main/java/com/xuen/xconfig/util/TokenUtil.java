package com.xuen.xconfig.util;

import java.util.UUID;
import org.junit.Test;

/**
 * @author zheng.xu
 * @since 2017-05-19
 */
public class TokenUtil {

    @Test
    public static String generateToken() {
        String s = UUID.randomUUID().toString();
        s.replaceAll("-", "");
        s += System.currentTimeMillis();
        System.out.println(s);
        return s;
    }

}
