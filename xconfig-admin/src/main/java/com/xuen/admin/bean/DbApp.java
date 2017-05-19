package com.xuen.admin.bean;


import java.io.Serializable;
import java.util.Date;

public class DbApp implements Serializable {

    private String app_name;
    private String token;
    private Date create_time;


    public DbApp(String app_name, String token, Date create_time) {
        this.app_name = app_name;
        this.token = token;
        this.create_time = create_time;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
