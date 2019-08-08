package com.zhph.workflow.api.po;


import java.io.Serializable;

/**
 * 流程
 */
public class WorkflowProcess implements Serializable {

    private static final long serialVersionUID = 3219067643041629496L;

    private String id;
    private String name;
    private String orderNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
