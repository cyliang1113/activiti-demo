package com.zhph.workflow.api.po;

import java.io.Serializable;

public class WorkflowOperateResult implements Serializable {
    private static final long serialVersionUID = 8790982583158579549L;

    private boolean result;
    private String msg;

    private WorkflowOperateResult(){

    }

    public static WorkflowOperateResult operateSuccess(){
        WorkflowOperateResult result = new WorkflowOperateResult();
        result.result = true;
        return result;
    }

    public static WorkflowOperateResult operateSuccess(String msg){
        WorkflowOperateResult result = new WorkflowOperateResult();
        result.result = true;
        result.msg = msg;
        return result;
    }

    public static WorkflowOperateResult operateFailure(String msg){
        WorkflowOperateResult result = new WorkflowOperateResult();
        result.result = false;
        result.msg = msg;
        return result;
    }

    public boolean isSuccess() {
        return result;
    }

    public boolean getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

}
