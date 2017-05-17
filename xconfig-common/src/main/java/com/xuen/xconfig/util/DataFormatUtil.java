package com.xuen.xconfig.util;

import com.google.common.base.Splitter;

/**
 * @author zheng.xu
 * @since 2017-05-17
 */
public class DataFormatUtil {

    private static final Splitter pipeSp = Splitter.on("|").trimResults();
    private static final Splitter keySp = Splitter.on("=").trimResults();

    public static String formatData(String s, String key) {
        Iterable<String> key$values = pipeSp.split(s);

        for (String keyValue : key$values) {
            String[] split = keyValue.split("=");
            if (key.equals(split[0])) {
                return split[1];
            }
        }
        return null;
    }


}
