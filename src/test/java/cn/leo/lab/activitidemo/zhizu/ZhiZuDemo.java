package cn.leo.lab.activitidemo.zhizu;

import cn.leo.lab.activitidemo.history.HisDemo01;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
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

public class ZhiZuDemo {

    private static Log log = LogFactory.getLog(ZhiZuDemo.class);

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
        deployment.addClasspathResource("process/zhizu/zhizu.bpmn");
        deployment.addClasspathResource("process/zhizu/zhizu.png");
        deployment.name("直租部署");
        Deployment deploy = deployment.deploy();
        log.info(deploy.getId());
        log.info(deploy.getName());
    }

    /**
     * 开始流程
     */
    @Test
    public void start(){
        String key = "zhiZuProcess";    //流程的唯一标识
        String entryUser = "XiXi";       //进件人Id
        String businessKey = "orderNo888";      //订单号
        String title = "订单" + businessKey;      //标题
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("entryUser", entryUser);
        processEngine.getIdentityService().setAuthenticatedUserId(entryUser);
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(key, businessKey, variables);
        processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), title);
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
        String key = "zhiZuProcess";
        String bKey = "orderNo888";
        Date startTime = new Date(2019-1900, 2-1, 28, 12, 0, 0);
        TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
//        taskQuery.processDefinitionKey(key);    //根据流程key查询
//        taskQuery.processInstanceBusinessKey(bKey); //根据订单号查询
        taskQuery.taskAssignee(user);   //根据任务处理人
        taskQuery.taskCreatedAfter(startTime);  //根据时间查询
        List<Task> list = taskQuery.list();
        for(Task t : list){
            log.info("=============");
            log.info("taskId: " + t.getId());
            log.info("taskName: " + t.getName());
            log.info("taskAssignee: " + t.getAssignee());
//            log.info("processId: " + t.getProcessInstanceId());
        }
    }

    /**
     * 查询待认领任务
     */
    @Test
    public void queryNotClaimTask(){
        String user = "D2";
        String bKey = "orderNo111";
        Date startTime = new Date(2019-1900, 2-1, 28, 12, 0, 0);
        TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
//        taskQuery.processInstanceBusinessKey(bKey);
        taskQuery.taskCandidateUser(user);  //待认领人
        List<Task> list = taskQuery.list();
        for(Task t : list){
            log.info("=============");
            log.info("taskId: " + t.getId());
            log.info("taskName: " + t.getName());
            log.info("taskAssignee: " + t.getAssignee());
//            log.info("processId: " + t.getProcessInstanceId());
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
        String id = "175001";
        List<HistoricActivityInstance> activityInstanceList = processEngine.getHistoryService().createHistoricActivityInstanceQuery().processInstanceId(id).list();
        for (HistoricActivityInstance act : activityInstanceList) {
            if ("userTask".equals(act.getActivityType())) {
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
        String id = "152501";
        processEngine.getRuntimeService().deleteProcessInstance(id, "测试删除");
    }
}
