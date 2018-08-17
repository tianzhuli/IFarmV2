package com.ifarm.bean;

import java.io.Serializable;

import com.ifarm.util.ByteConvert;
import com.ifarm.util.CacheDataBase;

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
		this.commandId = CacheDataBase.machineCode + String.valueOf(hashCode());
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

	public byte[] commandToByte() {
		byte[] arr = new byte[11];
		byte[] collectorArray = ByteConvert.convertTobyte(String.valueOf(this.controlDeviceId), false);
		arr[0] = 0x03; // 设备类型标识，目前都是对应的通断器
		for (int i = 0; i < collectorArray.length; i++) {
			arr[i + 1] = collectorArray[i];
		}
		arr[5] = 0x01; // 主功能号
		arr[6] = 0x00; // 次功能号
		switch (this.commandCategory) {
		case "execution":
			arr[7] = 0x01;
			arr[8] = 0x00;
			break;
		case "stop":
			arr[7] = 0x02;
			arr[8] = 0x00;
			break;
		default:
			arr[7] = 0x00;
			arr[8] = 0x00; //默认可以做查询
			break;
		}
		byte[] bits = ByteConvert.terminalBitsConvert(controlTerminalbits, 2);
		arr[9] = bits[0];
		arr[10] = bits[1];
		return arr;
	}
}
