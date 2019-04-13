package com.ifarm.service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.CollectorCommand;
import com.ifarm.bean.FarmCollectorDevice;
import com.ifarm.redis.util.CollectorDeviceCommandRedisHelper;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.JsonObjectUtil;

@Service
public class CollectorDeviceCommandService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CollectorDeviceCommandService.class);
	@Autowired
	private FarmCollectorDeviceService farmCollectorDeviceService;
	@Autowired
	private ProductionDeviceService productionDeviceService;
	@Autowired
	private CollectorDeviceCommandRedisHelper collectorDeviceCommandRedisHelper;

	public class CollectorDeviceCommandCallable implements Callable<Boolean> {

		@Override
		public Boolean call() throws Exception {
			// TODO Auto-generated method stub
			TimeUnit.SECONDS.sleep(10); // 等待CacheData初始化完成
			while (true) {
				if (!CacheDataBase.collectorCommandOpen) {
					TimeUnit.MINUTES.sleep(CacheDataBase.collectorTime);
					continue;
				}
				try {
					List<FarmCollectorDevice> list = farmCollectorDeviceService
							.queryFarmCollectorDeviceList(new FarmCollectorDevice());
					for (int i = 0; i < list.size(); i++) {
						FarmCollectorDevice farmCollectorDevice = list.get(i);
						Integer deviceId = Integer.valueOf(String
								.valueOf(farmCollectorDevice.getDeviceId()));
						if (deviceId != null) {
							Long collectorId = farmCollectorDevice
									.getCollectorId();
							String deviceType = farmCollectorDevice
									.getDeviceType();
							JSONObject deviceTypeCommandJsonObject = CacheDataBase.initBaseConfig
									.get("deviceCategory.json").getJSONObject(
											"deviceTypeMappingCommand");
							Integer deviceCommand = deviceTypeCommandJsonObject
									.getInteger(deviceType);
							if (deviceCommand != null) {
								LOGGER.info(
										"deviceId {} and collecorId {} is start command",
										deviceId, collectorId);
								collectorDeviceCommandRedisHelper
										.addRedisListValue(collectorId
												.toString(), CollectorCommand
												.productionCommand(deviceId,
														deviceCommand));
							} else {
								LOGGER.info("deviceCommand is no exist:"
										+ JsonObjectUtil
												.toJsonObject(deviceTypeCommandJsonObject));
							}
						}
					}
					TimeUnit.MINUTES.sleep(CacheDataBase.collectorTime);
				} catch (Exception e) {
					// TODO: handle exception
					LOGGER.error("collector command production error", e);
					TimeUnit.MINUTES.sleep(CacheDataBase.collectorTime);
				}
			}
		}

	}

	@PostConstruct
	public void init() {
		CacheDataBase.controlService
				.submit(new CollectorDeviceCommandCallable());
	}
}
