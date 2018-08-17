package com.ifarm.bean;

import com.ifarm.util.CacheDataBase;

public class WFMControlCommand extends ControlCommand {

	private static final long serialVersionUID = 3603987618260925806L;
	private boolean received;
	private boolean stoped;
	private String receivedResult;
	private String stopResult;
	private long receiveTime;
	private long stopTime;
	private String indentifying; // 标识，指该命令或者该设备控制了几个逻辑单元
	Long collectorId;

	public boolean isReceived() {
		return received;
	}

	public void setReceived(boolean isReceived) {
		this.received = isReceived;
	}

	public boolean isStoped() {
		return stoped;
	}

	public void setStoped(boolean stoped) {
		this.stoped = stoped;
	}

	public String getReceivedResult() {
		return receivedResult;
	}

	public void setReceivedResult(String receivedResult) {
		this.receivedResult = receivedResult;
	}

	public String getStopResult() {
		return stopResult;
	}

	public void setStopResult(String stopResult) {
		this.stopResult = stopResult;
	}

	public long getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(long receiveTime) {
		this.receiveTime = receiveTime;
	}

	public long getStopTime() {
		return stopTime;
	}

	public void setStopTime(long stopTime) {
		this.stopTime = stopTime;
	}

	public WFMControlCommand(WFMControlTask wfmControlTask, String commandCategory, Integer controlDeviceId, Long collectorId) {
		setTaskId(wfmControlTask.getControllerLogId());
		setControlDeviceId(controlDeviceId);
		this.collectorId = collectorId;
		setUserId(wfmControlTask.getUserId());
		setCommandCategory(commandCategory);
		setCommandId(CacheDataBase.machineCode + String.valueOf(hashCode()));
	}

	public WFMControlCommand() {

	}

	public Long getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(Long collectorId) {
		this.collectorId = collectorId;
	}

	public String getIndentifying() {
		return indentifying;
	}

	public void setIndentifying(String indentifying) {
		this.indentifying = indentifying;
	}
}
