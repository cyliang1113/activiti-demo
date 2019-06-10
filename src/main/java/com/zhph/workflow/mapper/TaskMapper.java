package com.zhph.workflow.mapper;

import com.zhph.workflow.api.po.WorkflowTask;

import java.util.List;

public interface TaskMapper {

    List<WorkflowTask> findTask();

}
