package cn.leo.lab.activitidemo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class QingJiaProcessTest3 {
    private static Log log = LogFactory.getLog(QingJiaProcessTest3.class);

    private static ProcessEngine processEngine = null;

    static{
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        processEngine = cfg.buildProcessEngine();
    }

    /**
     * 部署流程
     */
    @Test
    public void deploy() {
        DeploymentBuilder deployment = processEngine.getRepositoryService().createDeployment();
        deployment.addClasspathResource("process/qingjia/qingjialiucheng3.bpmn");
        deployment.addClasspathResource("process/qingjia/qingjialiucheng3.png");
        deployment.name("请假流程3部署");
        Deployment deploy = deployment.deploy();
        log.info(deploy.getId());
        log.info(deploy.getName());
    }

    /**
     * 开始流程
     */
    @Test
    public void qingjiaStart(){
//        String liuchengId = "qingjialiucheng:1:4";
//        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(liuchengId);
        String name = "xixi";
        processEngine.getIdentityService().setAuthenticatedUserId(name);
        String key = "qingjialiucheng3";
        HashMap<String, Object> var = new HashMap<>();
        var.put("who", name);
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(key, var);
        log.info(processInstance.getId());
        log.info(processInstance.getName());
    }

    /**
     * 查询流程
     */
    @Test
    public void queryProcess(){
        TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
        //taskQuery.taskAssignee("lisi");
        List<Task> list = taskQuery.list();
        for(Task t : list){
            log.info("====================");
            log.info("task: " + t.getName());
            log.info("taskId: " + t.getId());
            log.info("assignee: " + t.getAssignee());
            log.info("vars: " + processEngine.getRuntimeService().getVariables(t.getProcessInstanceId()));
            log.info("11>>>>");
            String processInstanceId = t.getProcessInstanceId();
            List<HistoricActivityInstance> list1 = processEngine.getHistoryService().createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
            for (HistoricActivityInstance historicActivityInstance : list1) {
                if("userTask".equals(historicActivityInstance.getActivityType()))
                log.info(historicActivityInstance.getActivityName() + ", " + historicActivityInstance.getAssignee() + ", ");
                List<Comment> taskComments = processEngine.getTaskService().getTaskComments(historicActivityInstance.getTaskId());
                for (Comment taskComment : taskComments) {
                    log.info("      comment: " + taskComment.getFullMessage());
                }
            }
            log.info("11<<<<");
        }
    }

    /**
     * 请假
     */
    @Test
    public void qingjia(){
        String taskId = "117506";
        HashMap<String, Object> var = new HashMap<>();
        var.put("days", 10);
        Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        processEngine.getTaskService().addComment(taskId, task.getProcessInstanceId(), "累了");
        processEngine.getTaskService().complete(taskId, var);

    }

    /**
     * 请假审批
     */
    @Test
    public void qingjiashenpi(){
        String taskId = "120005";
        HashMap<String, Object> var = new HashMap<>();
        var.put("result", "AGREE");
        Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        processEngine.getTaskService().addComment(task.getId(), task.getProcessInstanceId(), "准了");
        processEngine.getTaskService().complete(taskId, var);

    }

    @Test
    public void complete(){
        String taskId = "115003";
        processEngine.getTaskService().complete(taskId);
    }

    @Test
    public void queryProc() {
        String name = "xiaoming";
        List<HistoricProcessInstance> list = processEngine.getHistoryService().createHistoricProcessInstanceQuery().startedBy(name).list();
        for (HistoricProcessInstance proc : list) {
            log.info("===========================");
            log.info(proc.getProcessDefinitionId() + ", " + proc.getId());
            List<HistoricActivityInstance> activityInstanceList = processEngine.getHistoryService().createHistoricActivityInstanceQuery().processInstanceId(proc.getId()).list();
            for (HistoricActivityInstance act : activityInstanceList) {
                if ("userTask".equals(act.getActivityType())) {
                    String taskId = act.getTaskId();
                    log.info(act.getActivityName() + "(" + act.getActivityType() + ")" + ", " + taskId);
                    List<Comment> taskComments = processEngine.getTaskService().getTaskComments(taskId);
                    for (Comment taskComment : taskComments) {
                        log.info("      " + taskComment.getFullMessage());
                    }
                }
            }
        }
    }
}
