package cn.leo.lab.activitidemo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class QingJiaProcessTest {
    private static Log log = LogFactory.getLog(QingJiaProcessTest.class);

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
        deployment.addClasspathResource("process/qingjia/qingjialiucheng.bpmn");
        deployment.addClasspathResource("process/qingjia/qingjialiucheng.png");
        deployment.name("请假流程部署");
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
     * 开始请假流程
     */
    @Test
    public void qingjiaStart(){
//        String liuchengId = "qingjialiucheng:1:4";
//        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(liuchengId);
        String key = "qingjialiucheng";
        HashMap<String, String> form = new HashMap<String, String>();
        form.put("startTime", "2019-02-15");
        form.put("endTime", "2019-02-17");
        form.put("reason", "请年假");
//        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(key, form);
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult();
        ProcessInstance processInstance = processEngine.getFormService().submitStartFormData(processDefinition.getId(), form);
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
            log.info(t.getName());
            log.info(t.getId());
            log.info(t.getTaskLocalVariables());
        }
    }

    /**
     * 请假
     */
    @Test
    public void qingjia(){
        String taskId = "40002";
        processEngine.getTaskService().complete(taskId);

    }

    /**
     * 请假审批
     */
    @Test
    public void qingjiashenpi(){
        String taskId = "7502";
        processEngine.getTaskService().complete(taskId);

    }
}
