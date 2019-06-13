package cn.leo.lab.activitidemo;

import com.zhph.workflow.api.po.WorkflowTask;
import com.zhph.workflow.mapper.TaskMapper;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.util.List;

public class BaoXiaoProcessTest {
    private static Log log = LogFactory.getLog(BaoXiaoProcessTest.class);

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
        deployment.addClasspathResource("process/baoxiao/baoxiaoliucheng.bpmn");
        deployment.addClasspathResource("process/baoxiao/baoxiaoliucheng.png");
        deployment.name("报销流程部署");
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
        String key = "baoxiaoliucheng";
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(key);
        log.info(processInstance.getId());
        log.info(processInstance.getName());
    }

    @Test
    public void findTask(){
        ManagementService managementService = processEngine.getManagementService();
        List<WorkflowTask> workflowTasks = managementService.executeCustomSql(new AbstractCustomSqlExecution<TaskMapper, List<WorkflowTask>>(TaskMapper.class) {
            public List<WorkflowTask> execute(TaskMapper customMapper) {
                List<WorkflowTask> tasks = customMapper.queryTask(new WorkflowTask());
                return tasks;
            }
        });
        for (WorkflowTask workflowTask : workflowTasks) {
            log.info(workflowTask);
        }
        HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstanceQuery historicProcessQuery = historyService.createHistoricProcessInstanceQuery();
        historicProcessQuery
                .startedBy("")
                .processDefinitionKey("")
                .variableValueEquals("", "")
                .variableValueEquals("","");


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

    /**
     * 报销
     */
    @Test
    public void baoxiao(){
        String taskId = "15004";
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
}
