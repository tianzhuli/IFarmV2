package com.ifarm.parse;

import org.springframework.stereotype.Component;

import com.ifarm.bean.DeviceValueBase;
import com.ifarm.bean.device.AirTemHumDeviceValue;

/**
 * 空气温湿度解析器
 * @author LI
 *
 */
@Component
public class AirTemHumDeviceParse extends BaseCollectorDeviceParse{

	@Override
	public DeviceValueBase doParse(Long deviceId, byte[] arr) {
		// TODO Auto-generated method stub
		return new AirTemHumDeviceValue(deviceId, arr);
	}
	
}
