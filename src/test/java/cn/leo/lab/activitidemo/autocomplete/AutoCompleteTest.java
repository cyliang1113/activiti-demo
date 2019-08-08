package cn.leo.lab.activitidemo.autocomplete;

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
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.util.List;

public class AutoCompleteTest {
    private static Log log = LogFactory.getLog(AutoCompleteTest.class);

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
        deployment.addClasspathResource("process/taskAutoComplete/taskAuto.bpmn");
        deployment.addClasspathResource("process/taskAutoComplete/taskAuto.png");
        deployment.name("自动完成流程部署");
        Deployment deploy = deployment.deploy();
        log.info(deploy.getId());
        log.info(deploy.getName());
    }


    /**
     * 开始
     */
    @Test
    public void baoxiaoStart(){
        String key = "taskAuto";
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(key);
        log.info(processInstance.getId());
        log.info(processInstance.getName());
        List<Task> list = processEngine.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Task task : list) {
            log.info(task.toString());
        }

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
