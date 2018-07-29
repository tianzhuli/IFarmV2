package com.ifarm.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;
import com.ifarm.constant.ControlTaskEnum;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.JsonObjectUtil;
import com.ifarm.util.TimeHelper;

@Entity
@Table(name = "farm_controller_log")
public class ControlTask implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer controllerLogId; // 编号
	private String userId; // 下发控制的人，后期消息推送到该用户
	private Integer farmId;
	private String farmName;
	private String systemDistrict;
	private String systemNo;
	private Integer controlDeviceId; // 后期需要考虑一个控制系统的一个任务是不是会涉及到多个设备的同时操作，但是多个设备同时操作需要多条指令回复，存在不一致问题，需要考虑
	private Integer systemId; // 控制系统Id
	private String controlType; // 控制类型（施肥、灌溉、卷帘、遮阳等等）
	private String controlOperation; // 控制的具体操作，比如卷帘升降
	private String functionName; // 操作的名称
	@Transient
	private Integer level; // 命令等级（手动停止为4最高等级，其次为自动停止、往下以此为立即执行和定时执行）
	private Integer executionTime; // 执行时间
	private Timestamp startExecutionTime; // 开始执行的时间
	// 推送和查询分界线
	private long startStopTime; // 开始停止时间，可以是系统自动或者手动的
	private long addResultTime;// 收到添加回复与设备开始时间有略微的偏差(单位为秒)
	private long stopTime; // 收到设备返回信息的停止时间
	private boolean addReceived; // 是否已经收到添加回复
	private boolean stopReceived; // 是否已经收到停止的回复信息
	private String addResult; // 收到的添加应答信息
	private String stopResult; // 收到的停止应答信息
	private Timestamp taskTime; // 用户添加该指令的时间
	private String taskState;// 任务状态,等待还是执行
	private String userConfirmation; // 任务执行失败或者超时之后用户需要反馈信息
	@Transient
	private String commandCategory; // 指令类别（立即执行、定时执行、手动停止、自动停止)
	@Transient
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Transient
	int[] controlTerminalbits;
	@Transient
	Long collectorId;
	@Transient
	String responseMessage; // 推送给用户的信息code
	@Transient
	private static final long serialVersionUID = 5547672866474747159L;

	public Long getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(Long collectorId) {
		this.collectorId = collectorId;
	}

	public String getControlType() {
		return controlType;
	}

	public void setControlType(String controlType) {
		this.controlType = controlType;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getControllerLogId() {
		return controllerLogId;
	}

	public void setControllerLogId(Integer controllerLogId) {
		this.controllerLogId = controllerLogId;
	}

	public long getStartStopTime() {
		return startStopTime;
	}

	public void setStartStopTime(long startStopTime) {
		this.startStopTime = startStopTime;
	}

	public Integer getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Integer executionTime) {
		this.executionTime = executionTime;
	}

	public Timestamp getStartExecutionTime() {
		return startExecutionTime;
	}

	public void setStartExecutionTime(Timestamp startExecutionTime) {
		this.startExecutionTime = startExecutionTime;
	}

	public long getAddResultTime() {
		return addResultTime;
	}

	public void setAddResultTime(long addResultTime) {
		this.addResultTime = addResultTime;
	}

	public long getStopTime() {
		return stopTime;
	}

	public void setStopTime(long stopTime) {
		this.stopTime = stopTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getFarmId() {
		return farmId;
	}

	public void setFarmId(Integer farmId) {
		this.farmId = farmId;
	}

	public boolean isAddReceived() {
		return addReceived;
	}

	public void setAddReceived(boolean addReceived) {
		this.addReceived = addReceived;
	}

	public boolean isStopReceived() {
		return stopReceived;
	}

	public void setStopReceived(boolean stopReceived) {
		this.stopReceived = stopReceived;
	}

	public String getAddResult() {
		return addResult;
	}

	public void setAddResult(String addResult) {
		this.addResult = addResult;
	}

	public String getFarmName() {
		return farmName;
	}

	public void setFarmName(String farmName) {
		this.farmName = farmName;
	}

	public String getSystemDistrict() {
		return systemDistrict;
	}

	public void setSystemDistrict(String systemDistrict) {
		this.systemDistrict = systemDistrict;
	}

	public String getSystemNo() {
		return systemNo;
	}

	public void setSystemNo(String systemNo) {
		this.systemNo = systemNo;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getControlOperation() {
		return controlOperation;
	}

	public void setControlOperation(String controlOperation) {
		this.controlOperation = controlOperation;
	}

	public String getStopResult() {
		return stopResult;
	}

	public void setStopResult(String stopResult) {
		this.stopResult = stopResult;
	}

	public Timestamp getTaskTime() {
		return taskTime;
	}

	public void setTaskTime(java.sql.Timestamp timestamp) {
		this.taskTime = timestamp;
	}

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public String getCommandCategory() {
		return commandCategory;
	}

	public void setCommandCategory(String commandCategory) {
		this.commandCategory = commandCategory;
	}

	public Integer getControlDeviceId() {
		return controlDeviceId;
	}

	public void setControlDeviceId(Integer controlDeviceId) {
		this.controlDeviceId = controlDeviceId;
	}

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

	public int[] getControlTerminalbits() {
		return controlTerminalbits;
	}

	public void setControlTerminalbits(int[] controlTerminalbits) {
		this.controlTerminalbits = controlTerminalbits;
	}

	public String getUserConfirmation() {
		return userConfirmation;
	}

	public void setUserConfirmation(String userConfirmation) {
		this.userConfirmation = userConfirmation;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public ControlCommand buildCommand(String type) {
		ControlCommand command = null;
		if ("execution".equals(type)) {
			commandCategory = "execution";
			command = new ControlCommand(this, commandCategory, level, controlTerminalbits, controlDeviceId);
		} else if ("stop".equals(type)) {
			commandCategory = "stop";
			command = new ControlCommand(this, commandCategory, level, controlDeviceId);
		} else {
			command = new ControlCommand();
		}
		return command;
	}

	

	/**
	 * 每次用户查询的结果，组成相对应的json 底层硬件现在不能返回修改时间
	 * 
	 * @return
	 */
	public JSONObject buildQueryResult() {
		JSONObject jsonObject = JsonObjectUtil.fromBean(this, 13);
		jsonObject.put("taskState", this.taskState);
		jsonObject.put("taskTime", this.taskTime.toString());
		jsonObject.put("systemType", CacheDataBase.farmControlSystemService.getFarmControlSystemById(systemId).getSystemType());
		if (ControlTaskEnum.WAITTING.equals(taskState)) {
			long currentTime = System.currentTimeMillis() / 1000;
			jsonObject.put("remainWaitTime", startExecutionTime.getTime() / 1000 - currentTime);
			return jsonObject;
		} else if (ControlTaskEnum.EXECUTING.equals(taskState)) {
			jsonObject.put("addResultTime", TimeHelper.secondConvertTime(this.addResultTime));
			long currentTime = System.currentTimeMillis() / 1000;
			long offset = currentTime - this.addResultTime - this.executionTime;
			if (offset > 0) {
				jsonObject.put("remainExecutionTime", 0);
			} else {
				jsonObject.put("remainExecutionTime", -offset);
			}
		} else if (ControlTaskEnum.STOPPING.equals(taskState)) {
			jsonObject.put("startStopTime", TimeHelper.secondConvertTime(this.startStopTime));
		}
		return jsonObject;
	}

	/**
	 * 推送给用户的信息重组 查询的结果不需要推送，否则太多了，只需要服务器计算好时间，每次查询结果更新到内存即可
	 * 
	 * @return
	 */
	public String pushUserMessage() {
		JSONObject jsonObject = JsonObjectUtil.fromBean(this, 14);
		jsonObject.put("response", responseMessage);
		jsonObject.put("taskState", taskState);
		jsonObject.put("taskTime", this.taskTime.toString());
		jsonObject.put("systemType", CacheDataBase.farmControlSystemService.getFarmControlSystemById(systemId).getSystemType());
		if (stopReceived) {
			jsonObject.put("stopTime", TimeHelper.secondConvertTime(this.stopTime));
		}
		if (addReceived) {
			jsonObject.put("addResultTime", TimeHelper.secondConvertTime(this.addResultTime));
		}
		return jsonObject.toString();
	}

	public JSONObject queryCurrentTask() {
		return buildQueryResult();
	}

}
