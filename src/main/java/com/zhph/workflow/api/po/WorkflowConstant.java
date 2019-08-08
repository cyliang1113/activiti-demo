package com.zhph.workflow.api.po;

public class WorkflowConstant {

    public static final String RESULT = "Result";

    public static final String DIRECT_LEASE_PROCESS_KEY = "directLeaseProcess";

    public static final int SYS_ERROR_CODE = 50000;
    public static final int SYS_SUCCESS_CODE = 10000;

    public enum TaskResult {

        REFUSE("拒绝"),
        PASS("通过"),
        DISPROVE("驳回"),
        DISPROVE_2_ENTRY("驳回到进件"),
        CALL_FAILURE("电话打不通");

        private String cnName;

        private TaskResult(String cnName){
            this.cnName = cnName;
        }

        public String getCnName(){
            return this.cnName;
        }

        public static TaskResult getTaskResult(String name){
            TaskResult[] values = TaskResult.values();
            for (TaskResult value : values) {
                if(value.name().equals(name)){
                    return value;
                }
            }
            return null;
        }

    }
}
