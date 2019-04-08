package com.zhph.workflow.api.facade;

import com.zhph.workflow.api.po.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

/**
 * 流程facade
 */
@RequestMapping("/workflowProcessFacade")
public interface WorkflowProcessFacade {

    /**
     * 根据订单号查询流程
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/queryByOrderNo", method = RequestMethod.GET)
    FacadeResponse<WorkflowProcess> queryByOrderNo(String orderNo);


    /**
     * 开始直租流程
     */
    @RequestMapping(value = "/startDirectLeaseProcess", method = RequestMethod.GET)
    FacadeResponse<?> startDirectLeaseProcess(String userId, String orderNo);
}
