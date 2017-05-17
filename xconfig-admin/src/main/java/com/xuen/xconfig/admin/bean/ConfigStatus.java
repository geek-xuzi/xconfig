package com.xuen.xconfig.admin.bean;

/**
 * @author zheng.xu
 * @since 2017-05-17
 */
public enum ConfigStatus {

    REVIEWING("待审核"),
    REVIEWED("已审核");
    private final String desc;

    ConfigStatus(String desc) {
        this.desc = desc;
    }
}
