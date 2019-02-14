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

public class QuWanProcessTest {
    private static Log log = LogFactory.getLog(QuWanProcessTest.class);

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
        deployment.addClasspathResource("process/quwan/quwan.bpmn");
        deployment.addClasspathResource("process/quwan/quwan.png");
        deployment.name("去玩流程部署");
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
     * 开始流程
     */
    @Test
    public void qingjiaStart(){
//        String liuchengId = "qingjialiucheng:1:4";
//        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(liuchengId);
        String key = "quwanliucheng";
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(key);
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
            log.info("task: " + t.getName());
            log.info("taskId: " + t.getId());
            log.info("assignee: " + t.getAssignee());
            log.info(t.getTaskLocalVariables());
        }
    }

    /**
     * 请假
     */
    @Test
    public void qingjia(){
        String taskId = "22504";
        HashMap<String, Object> var = new HashMap<>();
        var.put("days", 10);
        processEngine.getTaskService().complete(taskId, var);

    }

    /**
     * 请假审批
     */
    @Test
    public void qingjiashenpi(){
        String taskId = "30003";
        processEngine.getTaskService().complete(taskId);

    }
}
