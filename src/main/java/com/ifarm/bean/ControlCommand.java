package com.ifarm.bean;

import java.io.Serializable;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.TimeHelper;

public class ControlCommand implements Serializable {
	private static final long serialVersionUID = 7909691415613846968L;
	private String commandId;
	private Integer taskId;
	private String userId;
	private String commandCategory; // 指令类别（立即执行、定时执行、手动停止、自动停止)
	private Integer controlDeviceId;
	private int[] controlTerminalbits;

	public ControlCommand(ControlTask controlTask, String commandCategory, int[] controlTerminalbits, Integer controlDeviceId) {
		this.taskId = controlTask.getControllerLogId();
		this.commandCategory = commandCategory;
		this.controlTerminalbits = controlTerminalbits;
		this.controlDeviceId = controlDeviceId;
		this.userId = controlTask.getUserId();
		this.commandId = TimeHelper.now() + CacheDataBase.machineCode + String.valueOf(hashCode());
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ControlCommand() {

	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public int[] getControlTerminalbits() {
		return controlTerminalbits;
	}

	public void setControlTerminalbits(int[] controlTerminalbits) {
		this.controlTerminalbits = controlTerminalbits;
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

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
}
