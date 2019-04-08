package com.zhph.workflow.api.po;


import java.io.Serializable;
import java.util.Date;

/**
 * 任务实例
 *
 */
public class WorkflowComment implements Serializable {


    private static final long serialVersionUID = -2759233221987048701L;

    private String id;
    private String result; //PASS
    private String resultCnName; //通过
    private String comment; //征信有问题
    private Date createTime;
    private String taskName; //资料审核


    public String getResultCnName(){
        WorkflowConstant.TaskResult taskResult = WorkflowConstant.TaskResult.getTaskResult(this.result);
        return taskResult != null ? taskResult.getCnName() : "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

}
