package com.xuen.xconfig.admin.bean;

/**
 * @author zheng.xu
 * @since 2017-05-17
 */
public enum ConfigType {
    JSON("json"),
    PROPERTIES("properties");
    private String desc;

    ConfigType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
