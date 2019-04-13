package com.ifarm.parse;

import org.springframework.stereotype.Component;

import com.ifarm.bean.DeviceValueBase;
import com.ifarm.bean.device.OxygenDeviceValue;

@Component
public class OxygenDeviceParse extends BaseCollectorDeviceParse{

	@Override
	public DeviceValueBase doParse(Long deviceId, byte[] arr) {
		DeviceValueBase deviceValueBase = new OxygenDeviceValue();
		deviceValueBase.setDeviceId(deviceId);
		deviceValueBase.setCollectData(arr, 8);
		return deviceValueBase;
	}
	
}
