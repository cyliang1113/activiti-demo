package com.zhph.workflow.api.po;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务
 *
 */
@Data
public class WorkflowTask implements Serializable {

    private static final long serialVersionUID = 8340465192698593587L;

    private String id;
    private String name;
    private String assignee;
    private Date createTime;
    private String processId;
    private String processName;
    private String orderNo;
    private String startUser;


}
