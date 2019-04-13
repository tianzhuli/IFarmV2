package com.ifarm.parse;

import org.springframework.stereotype.Component;

import com.ifarm.bean.DeviceValueBase;
import com.ifarm.bean.device.Co2DeviceValue;

@Component
public class Co2DeviceParse extends BaseCollectorDeviceParse{

	@Override
	public DeviceValueBase doParse(Long deviceId, byte[] arr) {
		DeviceValueBase deviceValueBase = new Co2DeviceValue();
		deviceValueBase.setDeviceId(deviceId);
		deviceValueBase.setCollectData(arr, 8);
		return deviceValueBase;
		// TODO Auto-generated method stub
	}
		
}
