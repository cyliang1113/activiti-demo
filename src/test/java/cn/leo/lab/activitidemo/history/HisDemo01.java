package cn.leo.lab.activitidemo.history;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.util.List;

public class HisDemo01 {

    private static Log log = LogFactory.getLog(HisDemo01.class);

    private static ProcessEngine processEngine = null;

    static{
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        processEngine = cfg.buildProcessEngine();
    }


    @Test
    public void test01() {
        HistoricActivityInstanceQuery query = processEngine.getHistoryService().createHistoricActivityInstanceQuery();
        long count = query.taskAssignee("zhangsan").count();
        log.info(count);
        List<HistoricActivityInstance> zhangsan = query.taskAssignee("zhangsan").list();
        for (HistoricActivityInstance historicActivityInstance : zhangsan) {
            log.info(historicActivityInstance.getActivityName() + ", " + historicActivityInstance.getProcessInstanceId() +
                    ", " + historicActivityInstance.getActivityId());
        }
    }

    @Test
    public void test02() {
        HistoricProcessInstanceQuery query = processEngine.getHistoryService().createHistoricProcessInstanceQuery();
        List<HistoricProcessInstance> kermit = query.startedBy("Kermit").list();
        for (HistoricProcessInstance historicProcessInstance : kermit) {
            log.info(historicProcessInstance.getProcessDefinitionId());
        }

    }
}
