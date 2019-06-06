package com.common.web.form;

/**
 * 接口调用返回基础数据类
 */
public class SimpleResponseForm {

    private boolean success;
    private int code = 0;
    private Object result;
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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SimpleResponseForm(boolean success,int code, Object result, String message) {
        this.success = success;
        this.code = code;
        this.result = result;
        this.message = message;
    }

    public SimpleResponseForm(Object result) {
        this.result = result;
    }

    public static SimpleResponseForm success(Object result) {
        return new SimpleResponseForm(true,0, result,null);
    }

    public static SimpleResponseForm error(int code,String message) {
        return new SimpleResponseForm(false,code, null,message);
    }

    public static SimpleResponseForm error(String message) {
        return new SimpleResponseForm(false,1, null,message);
    }

    public static SimpleResponseForm result(boolean bool,int code,String message,Object result) {
        return new SimpleResponseForm(bool,code, result,message);
    }
}
