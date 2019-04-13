package com.ifarm.parse;

import org.springframework.stereotype.Component;

import com.ifarm.bean.DeviceValueBase;
import com.ifarm.bean.device.FiveWithOneDeviceValue;

@Component
public class FiveWithOneCollectorDeviceParse extends BaseCollectorDeviceParse{

	@Override
	public DeviceValueBase doParse(Long deviceId, byte[] arr) {
		DeviceValueBase deviceValueBase = new FiveWithOneDeviceValue();
		deviceValueBase.setDeviceId(deviceId);
		deviceValueBase.setCollectData(arr, 8);
		return deviceValueBase;
		
	}
	
}
