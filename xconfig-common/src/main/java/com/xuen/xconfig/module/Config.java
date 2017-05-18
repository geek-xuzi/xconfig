package com.xuen.xconfig.module;

import java.io.Serializable;

/**
 * @author zheng.xu
 * @since 2017-05-17
 */
public class Config implements Serializable {

    private long cId;
    private int version;
    private ConfigStatus configStatus;
    private String configData;
    private String appName;
    private String configName;
    private ConfigType configType;

    public Config() {
    }

    public Config(long cId, int version, ConfigStatus configStatus, String configData,
            String appName, String configName, ConfigType configType) {
        this.cId = cId;
        this.version = version;
        this.configStatus = configStatus;
        this.configData = configData;
        this.appName = appName;
        this.configName = configName;
        this.configType = configType;
    }

    public long getcId() {
        return cId;
    }

    public void setcId(long cId) {
        this.cId = cId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public ConfigStatus getConfigStatus() {
        return configStatus;
    }

    public void setConfigStatus(ConfigStatus configStatus) {
        this.configStatus = configStatus;
    }

    public String getConfigData() {
        return configData;
    }

    public void setConfigData(String configData) {
        this.configData = configData;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public ConfigType getConfigType() {
        return configType;
    }

    public void setConfigType(ConfigType configType) {
        this.configType = configType;
    }
}
