package cn.leo.lab.activitidemo.event;

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

public class EventDemoProcessTest {
    private static Log log = LogFactory.getLog(EventDemoProcessTest.class);

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
        deployment.addClasspathResource("process/event/eventDemo.bpmn");
        deployment.addClasspathResource("process/event/eventDemo.png");
        deployment.name("eventDemo流程部署");
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
        String key = "eventDemoProcess";
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
        //taskQuery.taskAssignee("zhangsan");
        List<Task> list = taskQuery.list();
        for(Task t : list){
            log.info(t.getName());
            log.info(t.getId());
            log.info(t.getTaskLocalVariables());
        }
    }


    @Test
    public void completeTask(){
        String taskId = "7502";
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("task_result", "-1");
        processEngine.getTaskService().complete(taskId, variables);

    }

}
