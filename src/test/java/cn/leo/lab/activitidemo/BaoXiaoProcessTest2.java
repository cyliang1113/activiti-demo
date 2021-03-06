package cn.leo.lab.activitidemo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaoXiaoProcessTest2 {
    private static Log log = LogFactory.getLog(BaoXiaoProcessTest2.class);

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
        deployment.addClasspathResource("process/baoxiao/baoxiaoliucheng2.bpmn");
        deployment.addClasspathResource("process/baoxiao/baoxiaoliucheng2.png");
        deployment.name("报销流程部署2");
        Deployment deploy = deployment.deploy();
        log.info(deploy.getId());
        log.info(deploy.getName());
    }

    /**
     * 删除deploy
     */
    @Test
    public void delDeploy(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        String deployId = "1";
        repositoryService.deleteDeployment(deployId);
    }

    /**
     * 开始报销流程
     */
    @Test
    public void baoxiaoStart(){
//        String liuchengId = "qingjialiucheng:1:4";
//        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(liuchengId);
        String key = "baoxiaoliucheng2";
        String proposer = "wangwu";
        String accountants = "A1,A2";
        String manager = "zhaoliu";
        Integer money = 10000;
        HashMap<String, Object> var = new HashMap<>();
        var.put("proposer", proposer);
        var.put("accountants", accountants);
        var.put("manager", manager);
        var.put("money", money);

        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(key,var);
        log.info(processInstance.getId());
        log.info(processInstance.getName());
    }

    /**
     * 查询流程
     */
    @Test
    public void queryProcess(){
        TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
//        taskQuery.taskCandidateUser("AA");
//        taskQuery.taskAssignee("AA");
        List<Task> list = taskQuery.list();
        for(Task t : list){
            log.info(t.getName());
            log.info("taskId: " + t.getId());
            log.info("assignee: " + t.getAssignee());
            log.info("流程Id:" + t.getProcessInstanceId());
            String processInstanceId = t.getProcessInstanceId();
            Map<String, Object> variables = processEngine.getRuntimeService().getVariables(processInstanceId);
            log.info(variables.get("money"));
            log.info(variables.get("money").getClass());
        }

    }

    @Test
    public void claim(){
        String taskId = "47502";
        String userId = "AAA";
        processEngine.getTaskService().claim(taskId, userId);
    }

    /**
     * 报销
     */
    @Test
    public void baoxiao(){
        String taskId = "47502";
        processEngine.getTaskService().complete(taskId);

    }

    /**
     * 审批
     */
    @Test
    public void qingjiashenpi(){
        String taskId = "35002";
        processEngine.getTaskService().complete(taskId);

    }
}
