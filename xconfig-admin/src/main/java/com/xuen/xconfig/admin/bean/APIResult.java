package com.xuen.xconfig.admin.bean;

/**
 * @author zheng.xu
 * @since 2017-05-16
 */
public class APIResult {

    private int code;
    private String message;

    public APIResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public APIResult(int code, String message) {
        this(code, message, null);
    }

    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
