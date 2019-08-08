package cn.leo.lab.activitidemo.grouptask;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupTaskDemo {

    private static Log log = LogFactory.getLog(GroupTaskDemo.class);

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
        deployment.addClasspathResource("process/grouptask/grouptask.bpmn");
        deployment.addClasspathResource("process/grouptask/grouptask.png");
        deployment.name("组任务部署");
        Deployment deploy = deployment.deploy();
        log.info(deploy.getId());
        log.info(deploy.getName());
    }

    /**
     * 开始流程
     */
    @Test
    public void start(){
        String key = "grouptask";    //流程的唯一标识
        String entryUser = "Leo";       //进件人Id
        processEngine.getIdentityService().setAuthenticatedUserId(entryUser);
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(key);
        HistoricProcessInstance processInstance1 = processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
        log.info(processInstance1.getId());
        log.info(processInstance1.getName());
    }

    /**
     * 查询代办任务
     */
    @Test
    public void queryTask(){
        String user = "B1";
        TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
        taskQuery.taskAssignee(user);
        List<Task> list = taskQuery.list();
        for(Task t : list){
            log.info("=============");
            log.info("taskId: " + t.getId());
            log.info("taskName: " + t.getName());
            log.info("taskAssignee: " + t.getAssignee());
        }
    }

    /**
     * 查询待认领任务
     */
    @Test
    public void queryNotClaimTask(){
        String user = "user1";
        TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
        taskQuery.taskCandidateUser(user);  //待认领人
        List<Task> list = taskQuery.list();
        for(Task t : list){
            log.info("=============");
            log.info("taskId: " + t.getId());
            log.info("taskName: " + t.getName());
            log.info("taskAssignee: " + t.getAssignee());
        }
    }

    /**
     * 认领任务
     */
    @Test
    public void claimTask(){
        String taskId = "192504";
        String userId = "D2";
        processEngine.getTaskService().claim(taskId, userId);
    }

    /**
     * 完成任务1
     */
    @Test
    public void complete1(){
        String taskId = "180005";
        HistoricTaskInstance task = processEngine.getHistoryService().createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        String comment = "进件已经修改";
        processEngine.getTaskService().addComment(taskId, task.getProcessInstanceId(), comment);
        processEngine.getTaskService().complete(taskId);
    }

    /**
     * 完成任务2
     */
    @Test
    public void complete2(){
        String taskId = "192504";
        HistoricTaskInstance task = processEngine.getHistoryService().createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
//        String comment = "因为OOOO, 所以驳回...";
        String comment = "通过.";
        processEngine.getTaskService().addComment(taskId, task.getProcessInstanceId(), comment);
        String taskDefinitionKey = task.getTaskDefinitionKey();
        Map<String, Object> variables = new HashMap<String, Object>();
//        String result = "reject";
        String result = "pass";
        variables.put(taskDefinitionKey + "_result", result);
        processEngine.getTaskService().complete(taskId, variables);

    }

    @Test
    public void queryProc() {
        String id = "205001";
        List<HistoricActivityInstance> activityInstanceList = processEngine.getHistoryService().createHistoricActivityInstanceQuery().processInstanceId(id).list();
        for (HistoricActivityInstance act : activityInstanceList) {
            if ("userTask".equals(act.getActivityType()) || "startEvent".equals(act.getActivityType()) || "endEvent".equals(act.getActivityType())) {
                String taskId = act.getTaskId();
                log.info(act.getActivityName() + "(" + act.getActivityType() + ")" + ", " + taskId + ", " + act.getAssignee());
                List<Comment> taskComments = processEngine.getTaskService().getTaskComments(taskId);
                for (Comment taskComment : taskComments) {
                    log.info("      " + taskComment.getFullMessage());
                }
            }
        }
    }

    @Test
    public void delProc() {
        String id = "197501";
        processEngine.getRuntimeService().deleteProcessInstance(id, "测试删除");
    }
}
