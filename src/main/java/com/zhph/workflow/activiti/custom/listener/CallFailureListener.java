package com.zhph.workflow.activiti.custom.listener;

import com.zhph.workflow.api.po.WorkflowConstant;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 进件提交时, 判断是不是电话打不通驳回的
 */
public class CallFailureListener implements TaskListener {
    private static Log log = LogFactory.getLog(CallFailureListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info(delegateTask.getId() + ", " + delegateTask.getName() + ", " + delegateTask.getAssignee());
        String processInstanceId = delegateTask.getProcessInstanceId();
        String executionId = delegateTask.getExecutionId();
        log.info(processInstanceId);
        log.info(executionId);

        Object callCheckResult = delegateTask.getExecution().getEngineServices().getRuntimeService().getVariable(executionId, "callCheckResult");
        if(callCheckResult != null && WorkflowConstant.TaskResult.CALL_FAILURE.name().equals(callCheckResult)){
            delegateTask.getExecution().getEngineServices().getRuntimeService().setVariable(executionId, "callFailure", true);
        }else{
            delegateTask.getExecution().getEngineServices().getRuntimeService().setVariable(executionId, "callFailure", false);
        }
    }

}
