package com.zhph.workflow.api.po;

import java.io.Serializable;

public class FacadeResponse<T> implements Serializable {

    private static final long serialVersionUID = 2163523275359918188L;

    private int code;
    private String msg;
    private T data;

    private FacadeResponse(int code, T data, String msg){
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static <Z> FacadeResponse<Z> success(Z data, String msg){
        return new FacadeResponse<Z>(WorkflowConstant.SYS_SUCCESS_CODE, data, msg);
    }

    public static <Z> FacadeResponse<Z> failure(int code, String msg){
        return new FacadeResponse<Z>(code, null, msg);
    }

    public boolean isSuccess(){
        return WorkflowConstant.SYS_SUCCESS_CODE == this.code;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
