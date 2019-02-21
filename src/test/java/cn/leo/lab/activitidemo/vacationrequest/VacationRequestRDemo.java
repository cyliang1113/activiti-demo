package cn.leo.lab.activitidemo.vacationrequest;

import cn.leo.lab.activitidemo.history.HisDemo01;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
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

public class VacationRequestRDemo {

    private static Log log = LogFactory.getLog(VacationRequestRDemo.class);

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
        deployment.addClasspathResource("process/vacationRequest/vacationRequest.bpmn");
        deployment.addClasspathResource("process/vacationRequest/vacationRequest.png");
        deployment.name("假期申请部署");
        Deployment deploy = deployment.deploy();
        log.info(deploy.getId());
        log.info(deploy.getName());
    }

    /**
     * 开始流程
     */
    @Test
    public void start(){
        String key = "vacationRequest";
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employeeName", "Kermit");
        variables.put("numberOfDays", new Integer(4));
        variables.put("vacationMotivation", "I'm really tired!");
        processEngine.getIdentityService().setAuthenticatedUserId("Kermit");
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(key, variables);
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
    public void complete(){
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("vacationApproved", true);
        String taskId = "72508";
        processEngine.getTaskService().complete(taskId, variables);

    }
}
