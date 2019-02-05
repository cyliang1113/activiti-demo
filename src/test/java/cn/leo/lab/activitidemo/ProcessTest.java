package cn.leo.lab.activitidemo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.util.List;

public class ProcessTest {
    private static Log log = LogFactory.getLog(ProcessTest.class);

    private static ProcessEngine processEngine = null;

    static{
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        processEngine = cfg.buildProcessEngine();
    }

    /**
     * 部署流程
     */
    //@Test
    public void deploy() {

        DeploymentBuilder deployment = processEngine.getRepositoryService().createDeployment();
        deployment.addClasspathResource("qingjialiucheng.bpmn");
        deployment.addClasspathResource("qingjialiucheng.png");
        deployment.name("请假流程部署");
        Deployment deploy = deployment.deploy();
        log.info(deploy.getId());
        log.info(deploy.getName());
    }

    /**
     * 开始请假流程
     */
    //@Test
    public void qingjiaStart(){
        String liuchengId = "qingjialiucheng:1:4";
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(liuchengId);
        log.info(processInstance.getId());
        log.info(processInstance.getName());
    }

    /**
     * 查询流程
     */
    //@Test
    public void queryProcess(){
        TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
        taskQuery.taskAssignee("lisi");
        List<Task> list = taskQuery.list();
        for(Task t : list){
            log.info(t.getName());
            log.info(t.getId());
        }
    }

    /**
     * 请假
     */
    //@Test
    public void qingjia(){
        String taskId = "2504";
        processEngine.getTaskService().complete(taskId);

    }

    /**
     * 请假审批
     */
    @Test
    public void qingjiashenpi(){
        String taskId = "5002";
        processEngine.getTaskService().complete(taskId);

    }
}
