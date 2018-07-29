package com.ifarm.bean;

import java.io.Serializable;

import com.ifarm.util.ByteConvert;

public class ControlCommand implements Comparable<ControlCommand>, Serializable {
	private static final long serialVersionUID = 7909691415613846968L;
	private String commandId;
	private ControlTask controlTask; // 一次完整使用控制操作的编号，包括开始到结束
	private Integer taskId;
	private String commandCategory; // 指令类别（立即执行、定时执行、手动停止、自动停止)
	private Integer level; // 命令等级（手动停止为4最高等级，其次为自动停止、往下以此为立即执行和定时执行）
	private Integer controlDeviceId;
	private int[] controlTerminalbits;

	public ControlCommand(ControlTask controlTask, String commandCategory, Integer level, int[] controlTerminalbits, Integer controlDeviceId) {
		this.controlTask = controlTask;
		this.taskId = controlTask.getControllerLogId();
		this.commandCategory = commandCategory;
		this.level = level;
		this.controlTerminalbits = controlTerminalbits;
		this.controlDeviceId = controlDeviceId;
	}

	public ControlCommand(ControlTask controlTask, String commandCategory, Integer level, Integer controlDeviceId) {
		this.controlTask = controlTask;
		this.taskId = controlTask.getControllerLogId();
		this.commandCategory = commandCategory;
		this.level = level;
		this.controlDeviceId = controlDeviceId;
	}

	public ControlCommand() {

	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public ControlTask getControlTask() {
		return controlTask;
	}

	public void setControlTask(ControlTask controlTask) {
		this.controlTask = controlTask;
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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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
		byte[] arr = null;
		switch (this.commandCategory) {
		case "execution":
			arr = new byte[12];
			arr[0] = 0x68;
			arr[1] = 0x00; // 长度后面填充
			arr[2] = 0x03; // 设备类型
			arr[3] = 0x68;
			byte[] collectorArray = ByteConvert.convertTobyte(String.valueOf(this.controlDeviceId), false);
			arr[4] = collectorArray[0];
			arr[5] = collectorArray[1];
			arr[6] = collectorArray[2];
			arr[7] = collectorArray[3];
			arr[8] = 0x01;
			arr[9] = 0x02;
			byte[] bits = ByteConvert.terminalBitsConvert(controlTerminalbits, 2);
			arr[10] = bits[0];
			arr[11] = bits[1];
			break;
		case "stop":
			arr = new byte[12];
			arr[0] = 0x68;
			arr[1] = 0x00; // 长度后面填充
			arr[2] = 0x03; // 设备类型
			arr[3] = 0x68;
			byte[] collectorArrayStop = ByteConvert.convertTobyte(String.valueOf(this.controlDeviceId), false);
			arr[4] = collectorArrayStop[0];
			arr[5] = collectorArrayStop[1];
			arr[6] = collectorArrayStop[2];
			arr[7] = collectorArrayStop[3];
			arr[8] = 0x01;
			arr[9] = 0x03;
			byte[] bitStops = ByteConvert.terminalBitsConvert(controlTerminalbits, 2);
			arr[10] = bitStops[0];
			arr[11] = bitStops[1];
			break;
		default:
			arr = new byte[0];
			break;
		}
		return arr;
	}

	@Override
	public int compareTo(ControlCommand o) {
		// TODO Auto-generated method stub
		if (this.level < o.getLevel()) {
			return 1;
		} else if (this.level > o.getLevel()) {
			return -1;
		} else {
			return 0;
		}
	}
}
