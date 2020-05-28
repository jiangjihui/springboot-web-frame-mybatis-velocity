package com.jjh.common.web.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 接口调用返回基础数据类
 */
@ApiModel("响应对象")
public class SimpleResponseForm<T> implements Serializable {

    private static final int SUCCESS_CODE = 0;
    private static final String SUCCESS_MESSAGE = "成功";

    @ApiModelProperty(value = "成功标识；true：成功；false:失败", required = true)
    private boolean success;

    @ApiModelProperty(value = "返回状态码；0:成功；其他状态码:失败", required = true)
    private int code = 0;

    @ApiModelProperty(value = "结果集")
    private T result;

    @ApiModelProperty(value = "描述信息")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SimpleResponseForm(boolean success,int code, T result, String message) {
        this.success = success;
        this.code = code;
        this.result = result;
        this.message = message;
    }

    public SimpleResponseForm() {
        this(true, SUCCESS_CODE, null, SUCCESS_MESSAGE);
    }

    public SimpleResponseForm(T result) {
        this.success = true;
        this.result = result;
    }

    public static <T> SimpleResponseForm<T> success(T result) {
        return new SimpleResponseForm(true,0, result,null);
    }

    public static <T> SimpleResponseForm<T> error(int code,String message) {
        return new SimpleResponseForm(false,code, null,message);
    }

    public static <T> SimpleResponseForm<T> error(String message) {
        return new SimpleResponseForm(false,1, null,message);
    }

    public static <T> SimpleResponseForm<T> result(boolean bool,int code,String message,T result) {
        return new SimpleResponseForm(bool,code, result,message);
    }
}
