package com.zhph.workflow.activiti.custom.listener;

import com.zhph.workflow.api.po.WorkflowConstant;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 */
public class AutoCompleteListener implements TaskListener {
    private static Log log = LogFactory.getLog(AutoCompleteListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("AutoCompleteListener");
        TaskService taskService = delegateTask.getExecution().getEngineServices().getTaskService();
        taskService.complete(delegateTask.getId());
    }

}
