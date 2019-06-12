package cn.leo.lab.activitidemo;

import com.alibaba.fastjson.JSON;
import com.zhph.workflow.api.po.WorkflowTask;
import com.zhph.workflow.mapper.TaskMapper;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.util.List;

public class BaoXiaoProcessTest3 {
    private static Log log = LogFactory.getLog(BaoXiaoProcessTest3.class);

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
        deployment.addClasspathResource("process/baoxiao/baoxiaoliucheng3.bpmn");
        deployment.addClasspathResource("process/baoxiao/baoxiaoliucheng3.png");
        deployment.name("报销流程3部署");
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
        processEngine.getIdentityService().setAuthenticatedUserId("XxXX");
        String key = "baoxiaoliucheng3";
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(key);
        log.info(processInstance.getId());
        log.info(processInstance.getName());
    }

    @Test
    public void findTask(){
        ManagementService managementService = processEngine.getManagementService();
        List<WorkflowTask> workflowTasks = managementService.executeCustomSql(new AbstractCustomSqlExecution<TaskMapper, List<WorkflowTask>>(TaskMapper.class) {
            public List<WorkflowTask> execute(TaskMapper customMapper) {
                List<WorkflowTask> tasks = customMapper.findTask();
                return tasks;
            }
        });
        log.info(JSON.toJSONString(workflowTasks));
    }

    /**
     * 查询流程
     */
    @Test
    public void queryProcess(){
        ProcessInstanceQuery processInstanceQuery = processEngine.getRuntimeService().createProcessInstanceQuery();
        List<ProcessInstance> list = processInstanceQuery.processInstanceId("2501").list();
        for (ProcessInstance processInstance : list) {
            log.info(processInstance.getId());
        }

    }

    /**
     * 报销
     */
    @Test
    public void baoxiao(){
        String taskId = "2505";
        processEngine.getTaskService().complete(taskId);

    }

    /**
     * 审批
     */
    @Test
    public void qingjiashenpi(){
        String taskId = "20002";
        processEngine.getTaskService().complete(taskId);

    }

    @Test
    public void delProc() {
        processEngine.getRuntimeService().deleteProcessInstance("20001", "测试删除");
    }
}
