package com.ifarm.parse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ifarm.bean.DeviceValueBase;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.ConvertData;

public abstract class BaseCollectorDeviceParse {

	protected ConvertData convertData = new ConvertData();
	protected static final Logger LOGGER = LoggerFactory.getLogger(BaseCollectorDeviceParse.class);

	public void parse(byte[] arr,Long collecotrId) {
		Long deviceId = convertData.byteToConvertLong(arr, 4, 4);
		DeviceValueBase deviceValueBase = doParse(deviceId, arr);
		CacheDataBase.collectorDeviceValueService.saveCollectorDeviceValues(deviceValueBase);
		CacheDataBase.collectorDeviceValueService.saveCollectorDeviceCache(deviceId, deviceValueBase);
		LOGGER.info("{}",deviceValueBase);
	}

	public abstract DeviceValueBase doParse(Long deviceId, byte[] arr);
}
