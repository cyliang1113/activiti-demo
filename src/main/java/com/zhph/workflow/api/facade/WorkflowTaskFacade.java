package com.zhph.workflow.api.facade;

import com.zhph.workflow.api.po.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

/**
 * 任务facade
 */
@RequestMapping("/workflowTaskFacade")
public interface WorkflowTaskFacade {

    /**
     * 待办的任务总条数
     */
    @RequestMapping(value = "/todoTotal", method = RequestMethod.GET)
    FacadeResponse<Page<?>> todoTotal(String userId);

    /**
     * 待办的任务
     */
    @RequestMapping(value = "/todoList", method = RequestMethod.GET)
    FacadeResponse<Page<WorkflowTask>> todoList(String userId, String orderNo, Date createTimeStart, Date createTimeEnd, Integer currentPage, Integer pageSize);

    /**
     * 完成任务
     */
    @RequestMapping(value = "/complete", method = RequestMethod.GET)
    FacadeResponse<?> complete(String taskId, String userId, WorkflowConstant.TaskResult result, String comment);

    /**
     * 待认领任务
     */
    @RequestMapping(value = "/waitClaimList", method = RequestMethod.GET)
    FacadeResponse<Page<WorkflowTask>> waitClaimList(String userId, String orderNo, Date createTimeStart, Date createTimeEnd, Integer currentPage, Integer pageSize);

    /**
     * 认领任务
     */
    @RequestMapping(value = "/claim", method = RequestMethod.GET)
    FacadeResponse<?> claim(String taskId, String userId);

    /**
     * 已完成的任务
     */
    @RequestMapping(value = "/finishedList", method = RequestMethod.GET)
    FacadeResponse<Page<WorkflowTask>> finishedList(String userId, String orderNo, Date createTimeStart, Date createTimeEnd, Integer currentPage, Integer pageSize);

    /**
     * 审批历史
     * @param processId
     * @return
     */
    @RequestMapping(value = "/approveHistory", method = RequestMethod.GET)
    FacadeResponse<List<WorkflowComment>> approveHistory(String processId);

    /**
     * 指定任务处理人
     */
    @RequestMapping(value = "/appointAssignee", method = RequestMethod.GET)
    FacadeResponse<?> appointAssignee(String taskId, String userId);

    /**
     * 任务候选组
     */
    @RequestMapping(value = "/candidateGroupList", method = RequestMethod.GET)
    FacadeResponse<List<String>> candidateGroupList(String taskId);

    /**
     * 角色的所有待办任务
     */
    @RequestMapping(value = "/allTodoList", method = RequestMethod.GET)
    FacadeResponse<Page<WorkflowTask>> allTodoList(String[] candidateGroups, String orderNo, Date createTimeStart, Date createTimeEnd, Integer currentPage, Integer pageSize);
}
