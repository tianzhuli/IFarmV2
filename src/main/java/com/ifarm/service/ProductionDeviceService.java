package com.ifarm.service;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.ProductionDevice;
import com.ifarm.constant.SystemResultCodeEnum;
import com.ifarm.dao.FarmCollectorDao;
import com.ifarm.dao.FarmCollectorDeviceDao;
import com.ifarm.dao.FarmControlDeviceDao;
import com.ifarm.dao.ProductionDeviceDao;
import com.ifarm.redis.util.ProductionDeviceUtil;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.JsonObjectUtil;
import com.ifarm.util.RandomUtil;

@Service
public class ProductionDeviceService {
	@Autowired
	private ProductionDeviceDao productionDeviceDao;

	@Autowired
	private ProductionDeviceUtil productionDeviceUtil;

	@Autowired
	private FarmCollectorDao farmCollectorDao;

	@Autowired
	private FarmCollectorDeviceDao farmCollectorDeviceDao;

	@Autowired
	private FarmControlDeviceDao farmControlDeviceDao;

	private static final Logger productionDevice_log = LoggerFactory.getLogger(ProductionDeviceService.class);

	/*private static final String concentrator = "concentrator"; // 集中器

	private static final String collectorDevice = "collectorDevice"; // 采集设备

	private static final String controlDevice = "controlDevice"; // 控制设备
*/
	/**
	 * 
	 * @param deviceType
	 *            采集设备分为不同的类型，控制也是，
	 * @param deviceCategory
	 *            包括控制和采集、集中器三种
	 * @param deviceCreatePerson
	 * @return
	 */
	
	public JSONArray createProductionDevice(ProductionDevice productionDevice, int batch) {
		JSONArray jsonArray = new JSONArray();
		String deviceCode = productionDevice.getDeviceCode();
		for (int i = 0; i < batch; i++) {
			JSONObject jsonObject = new JSONObject();
			String deviceVerification = RandomUtil.randomString();
			ProductionDevice proDevice = new ProductionDevice(productionDevice);
			proDevice.setDeviceVerification(deviceVerification);
			int base = CacheDataBase.initBaseConfig.get("deviceCategory.json").getJSONObject("deviceTypeNo").getIntValue(deviceCode);
			Integer deviceId = RandomUtil.randomInteger(base, 6);
			while (productionDeviceDao.existProductionDevice(deviceId)) {
				deviceId = RandomUtil.randomInteger(base, 6);
			}
			proDevice.setDeviceId(deviceId);
			productionDeviceDao.saveProductionDevice(proDevice);
			jsonObject.put("deviceId", deviceId);
			jsonObject.put("deviceVerification", deviceVerification);
			jsonObject.put("createTime", proDevice.getCreateTime().toString());
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	public JSONObject batchProductionDevice(ProductionDevice productionDevice, int batch) {
		JSONObject jsonObject = new JSONObject();
		try {
			JSONArray array = createProductionDevice(productionDevice, batch);
			jsonObject.put("response", SystemResultCodeEnum.SUCCESS);
			jsonObject.put("devices", array);
		} catch (Exception e) {
			// TODO: handle exception
			productionDevice_log.error("设备生产异常：", e);
			jsonObject.put("response", SystemResultCodeEnum.ERROR);
		}
		return jsonObject;
	}

	/**
	 * 添加设备到数据库，二维码扫描
	 * 
	 * @param productionDevice
	 * @return
	 */
	public JSONObject deviceCheck(ProductionDevice productionDevice) {
		JSONObject jsonObject = new JSONObject();
		Integer deviceId = productionDevice.getDeviceId();
		ProductionDevice originalProductionDevice = productionDeviceDao.queryProductionDevice(deviceId);
		if (deviceId == null || originalProductionDevice == null) {
			jsonObject.put("response", SystemResultCodeEnum.NO_ID);
			return jsonObject;
		}
		if (!originalProductionDevice.getDeviceVerification().equals(productionDevice.getDeviceVerification())) {
			jsonObject.put("response", SystemResultCodeEnum.CHECK_ERROR);
			return jsonObject;
		}
		jsonObject.put("response", SystemResultCodeEnum.SUCCESS);
		jsonObject.put("device", JsonObjectUtil.fromBean(originalProductionDevice));
		return jsonObject;
	}
	
	public static void main(String[] args) {
		System.out.println(Calendar.getInstance().getWeekYear() % 100);
	}

}
