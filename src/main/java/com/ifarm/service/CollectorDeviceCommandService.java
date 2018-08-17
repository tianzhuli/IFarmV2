package com.ifarm.service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifarm.bean.CollectorCommand;
import com.ifarm.bean.FarmCollectorDevice;
import com.ifarm.bean.ProductionDevice;
import com.ifarm.dao.FarmCollectorDeviceDao;
import com.ifarm.dao.ProductionDeviceDao;
import com.ifarm.redis.util.CollectorDeviceCommandRedisHelper;
import com.ifarm.util.CacheDataBase;

@Service
public class CollectorDeviceCommandService {
	@Autowired
	private FarmCollectorDeviceDao farmCollectorDeviceDao;
	@Autowired
	private ProductionDeviceDao productionDeviceDao;
	@Autowired
	private CollectorDeviceCommandRedisHelper collectorDeviceCommandRedisHelper;
	
	public class CollectorDeviceCommandCallable implements Callable<Boolean> {

		@Override
		public Boolean call() throws Exception {
			// TODO Auto-generated method stub
			while (true) {
				List<FarmCollectorDevice> list = farmCollectorDeviceDao.getDynamicList(new FarmCollectorDevice());
				for (int i = 0; i < list.size(); i++) {
					FarmCollectorDevice farmCollectorDevice = list.get(i);
					Integer deviceId = Integer.valueOf(String.valueOf(farmCollectorDevice.getDeviceId()));
					if (deviceId != null) {
						Long collectorId = farmCollectorDevice.getCollectorId();
						ProductionDevice productionDevice = productionDeviceDao.getTById(deviceId, ProductionDevice.class);
						if (productionDevice != null) {
							String deviceTypeCode = productionDevice.getDeviceTypeCode();
							if (deviceTypeCode != null) {
								Integer deviceNo = CacheDataBase.initBaseConfig.get("deviceCategory.json").getJSONObject("deviceNo").getInteger(deviceTypeCode);
								if (deviceNo != null) {
									collectorDeviceCommandRedisHelper.addRedisListValue(collectorId.toString(), CollectorCommand.productionCommand(deviceId, deviceNo));
								}
							}
						}
					}
				}
				TimeUnit.MINUTES.sleep(CacheDataBase.collectorTime);
			}
		}

	}
	
	@PostConstruct
	public void init() {
		CacheDataBase.controlService.submit(new CollectorDeviceCommandCallable());
	}
}
