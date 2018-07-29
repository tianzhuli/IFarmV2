package com.ifarm.constant;

public class ControlTaskEnum {
	public static final String EXECUTING = "EXECUTEING"; //任务运行
	public static final String WAITTING = "WAITTING"; //任务等待
	public static final String BLOCKING = "BLOCKING"; //任务下发
	public static final String STOPPING = "STOPPING"; //任务停止中
	public static final String STOPPED = "STOPPED"; //任务已停止
	public static final String CONFICTING = "CONFICTING"; //任务冲突
	public static final String NO_MESSAGE = "null";
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String EXEUTION_SUCCESS = "executionSuccess";
	public static final String STOP_SUCCESS = "stopSuccess";
	public static final String EXECUTION_FAIL = "executionFail";
	public static final String STOP_FAIL = "executionFail";
	public static final String RUNNING = "running";
	public static final String NO_TASK = "noTask";
	public static final String CONFLICT = "schemerConflict";
	public static final String CONFLICT_CURRENT = "currentRunning";
	public static final String STOP_SUCESS_RESPONSE = "stopSucess"; 
	public static final String STOP_FAIL_RESPONSE = "failStop"; 
	public static final String EXECUTION_SUCCESS_RESPONSE = "successExcution";  //任务执行成功
	public static final String EXECUTION_FAIL_RESPONSE = "failExecution";
	public static final String STOP_TIMEOUT_RESPONSE = "stopTimeout"; 
	public static final String EXECUTION_TIMEOUT_RESPONSE = "executionTimeout";
	public static final String EXECUTION_COMPLETE_RESPONSE = "execuitonComplete"; //任务完成
}
