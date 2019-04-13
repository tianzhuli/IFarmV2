package com.ifarm.parse;

import org.springframework.stereotype.Component;

import com.ifarm.bean.DeviceValueBase;
import com.ifarm.bean.device.WeatherMonitorDeviceValue;

@Component
public class WeatherMonitorDeviceParse extends BaseCollectorDeviceParse {

	@Override
	public DeviceValueBase doParse(Long deviceId, byte[] arr) {
		// TODO Auto-generated method stub
		DeviceValueBase deviceValueBase = new WeatherMonitorDeviceValue();
		deviceValueBase.setDeviceId(deviceId);
		deviceValueBase.setCollectData(arr, 8);
		return deviceValueBase;
	}

}
