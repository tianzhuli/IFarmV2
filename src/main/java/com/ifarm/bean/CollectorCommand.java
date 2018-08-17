package com.ifarm.bean;

import com.ifarm.util.ByteConvert;

public class CollectorCommand {
	private Integer deviceId;
	private Integer deviceNo;

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(Integer deviceNo) {
		this.deviceNo = deviceNo;
	}

	public CollectorCommand(Integer deviceId, Integer deviceNo) {
		this.deviceId = deviceId;
		this.deviceNo = deviceNo;
	}

	public static CollectorCommand productionCommand(Integer deviceId, Integer deviceNo) {
		return new CollectorCommand(deviceId, deviceNo);
	}

	public byte[] commandToByte() {
		byte[] arr = new byte[9];
		arr[0] = (byte) deviceNo.intValue();
		byte[] deviceArray = ByteConvert.convertTobyte(String.valueOf(deviceId), true);
		for (int i = 0; i < deviceArray.length; i++) {
			arr[i + 1] = deviceArray[0];
		}
		arr[5] = 0x01;
		arr[6] = 0x00;
		arr[7] = 0x00;
		arr[8] = 0x00;
		return arr;
	}
}
