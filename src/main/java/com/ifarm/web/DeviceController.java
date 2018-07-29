package com.ifarm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ifarm.bean.FarmCollector;
import com.ifarm.bean.FarmCollectorDevice;
import com.ifarm.bean.FarmControlDevice;
import com.ifarm.bean.ProductionDevice;
import com.ifarm.redis.util.ProductionDeviceUtil;
import com.ifarm.service.FarmCollectorDeviceService;
import com.ifarm.service.FarmCollectorService;
import com.ifarm.service.FarmControlDeviceService;
import com.ifarm.service.ProductionDeviceService;
import com.ifarm.util.CacheDataBase;

@RestController
@RequestMapping("device")
public class DeviceController {
	@Autowired
	private ProductionDeviceService productionDeviceService;

	@Autowired
	private ProductionDeviceUtil productionDeviceUtil;

	@Autowired
	private FarmCollectorService farmCollectorService;

	@Autowired
	private FarmCollectorDeviceService farmCollectorDeviceService;

	@Autowired
	private FarmControlDeviceService farmControlDeviceService;

	@RequestMapping("production")
	String productionDevcie(ProductionDevice productionDevice, @RequestParam("batch") int batch) {
		return productionDeviceService.batchProductionDevice(productionDevice, batch).toString();
	}

	@RequestMapping("category")
	String productionDeviceCategory() {
		return CacheDataBase.initBaseConfig.get("deviceCategory.json").getJSONArray("deviceCategory").toJSONString();
	}

	@RequestMapping("check")
	String deviceAppendToFarm(ProductionDevice productionDevice) {
		return productionDeviceService.deviceCheck(productionDevice).toString();
	}

	@RequestMapping("concentrator/addition")
	String concentratorAppend(FarmCollector farmCollector) {
		return farmCollectorService.saveFarmCollector(farmCollector);
	}

	@RequestMapping("collectorDevice/addition")
	String collectorDeviceAddition(FarmCollectorDevice farmCollectorDevice) {
		return farmCollectorDeviceService.saveCollectorDevice(farmCollectorDevice);
	}
	
	@RequestMapping("collectorDevice/delete")
	String collectorDeviceDelet(FarmCollectorDevice farmCollectorDevice) {
		return farmCollectorDeviceService.deleteFarmCollectorDevice(farmCollectorDevice);
	}

	@RequestMapping("controlDevice/delete")
	String controlDeviceDelete(FarmControlDevice farmControlDevice) {
		return farmControlDeviceService.deleteFarmControlDevice(farmControlDevice);
	}
}
