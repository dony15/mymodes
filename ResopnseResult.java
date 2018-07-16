package com.sso.utils;

import java.io.Serializable;

/**
 * 响应工具类
 * @status 响应状态 true/false
 * @msg 响应消息 ""
 * @data 响应数据
 * 前段交互的一种规范(根据公司环境不同,标准不同)
 */
public class ResopnseResult implements Serializable {
    // 响应业务状态
    private Boolean status;
    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;

    public ResopnseResult() {

    }

    public ResopnseResult(Boolean status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    //getter/setter略
    public static ResopnseResult getErrorResult(String msg, Object data) {
        return new ResopnseResult(false, msg, data);
    }

    public static ResopnseResult getErrorResult(String msg) {
        return new ResopnseResult(false, msg, null);
    }

    public static ResopnseResult getOkResult(String msg, Object data) {
        return new ResopnseResult(true, msg, data);
    }

    public static ResopnseResult getOkResult(String msg) {
        return new ResopnseResult(true, msg, null);
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
