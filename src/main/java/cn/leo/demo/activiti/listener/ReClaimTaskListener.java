package cn.leo.demo.activiti.listener;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 认领过的任务重新自动认领
 */
public class ReClaimTaskListener implements TaskListener {
    private static Log log = LogFactory.getLog(ReClaimTaskListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info(delegateTask.getId() + ", " + delegateTask.getName() + ", " + delegateTask.getAssignee());

        String currentActivityId = delegateTask.getExecution().getCurrentActivityId();
        String instanceId = delegateTask.getExecution().getProcessInstanceId();

        TaskService taskService = delegateTask.getExecution().getEngineServices().getTaskService();
        Task task = getPreTask(taskService, instanceId, currentActivityId);

        if (task != null) {
            try {
                taskService.claim(delegateTask.getId(), task.getAssignee());
            } catch (Exception e) {
                log.error("自动重新认领任务异常", e);
            }
        }
    }

    private Task getPreTask(TaskService taskService, String instanceId, String activitiId) {
        Task task = taskService.createNativeTaskQuery().sql(
                "SELECT * FROM ACT_HI_TASKINST task" + "\n" +
                "JOIN ACT_HI_ACTINST act on act.TASK_ID_ = task.ID_" + "\n" +
                "WHERE task.PROC_INST_ID_ = '" + instanceId + "' AND act.ACT_ID_ = '" + activitiId + "'\n" +
                "ORDER BY act.START_TIME_ DESC LIMIT 1")
                .singleResult();
        return task;
    }
}
