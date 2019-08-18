package com.ifarm.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.DeviceValueBase;
import com.ifarm.bean.FarmCollectorDevice;
import com.ifarm.dao.CollectorDeviceValueDao;
import com.ifarm.dao.DeviceHistoryValuesDao;
import com.ifarm.dao.FarmCollectorDeviceDao;
import com.ifarm.enums.DeviceValueType;
import com.ifarm.log.RedisLog;
import com.ifarm.redis.util.CollectorDeviceValueRedisUtil;
import com.ifarm.util.CreateExcel;
import com.ifarm.util.JsonObjectUtil;

@Service
public class CollectorDeviceValueService {
	@Autowired
	private DeviceHistoryValuesDao deviceHistoryValuesDao;

	@Autowired
	private FarmCollectorDeviceDao fCollectorDeviceDao;

	@Autowired
	private CollectorDeviceValueDao collectorDeviceValueDao;

	@Autowired
	private CollectorDeviceValueRedisUtil cDeviceRedisUtil;

	@Autowired
	private FarmCollectorDeviceService farmCollectorDeviceService;

	@Autowired
	private CollectorDeviceValueRedisUtil collectorDeviceValueRedisUtil;

	private SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final Logger LOGGER = LoggerFactory.getLogger(CollectorValueService.class);

	public void saveCollectorDeviceValues(DeviceValueBase collectorDeviceValue) {
		collectorDeviceValue.setUpdateTime(Timestamp.valueOf(dFormat.format(new Date())));
		try {
			collectorDeviceValueDao.saveBase(collectorDeviceValue);
			FarmCollectorDevice farmCollectorDevice = fCollectorDeviceDao.getTById(collectorDeviceValue.getDeviceId(), FarmCollectorDevice.class);
			if (farmCollectorDevice != null) {
				cDeviceRedisUtil.saveCollectorDeviceValueByFarm(farmCollectorDevice.getFarmId(), collectorDeviceValue.getDeviceId(), collectorDeviceValue);
				cDeviceRedisUtil.saveCollectorDeviceValueByUnit(farmCollectorDevice.getUnitNo(), collectorDeviceValue.getDeviceId(), collectorDeviceValue);
			} else {
				LOGGER.error("{} is no exist", collectorDeviceValue);
			}
		} catch (Exception e) {
			// TODO: handle exception
			RedisLog.COLLECTOR_DEVICE_VALUE_LOG.error("saveCollectorDeviceValues error", e);
		}
	}

	public JSONArray fillCollectorDevice(JSONArray array,DeviceValueBase deviceValue,Long deviceId, FarmCollectorDevice farmCollectorDevice) {
		JSONObject deviceValueJsonObject = JsonObjectUtil.fromBean(deviceValue);
		if (deviceValueJsonObject == null) {
			deviceValueJsonObject = new JSONObject();
			deviceValueJsonObject.put("deviceId", deviceId);
		}
		packagingDeviceDetail(deviceValueJsonObject, farmCollectorDevice);
		array.add(deviceValueJsonObject);
		return array;
	}

	public JSONArray getCollectorDeviceValues(FarmCollectorDevice fDevice) {
		JSONArray collectorDeviceValueArray = new JSONArray();
		Long deviceId = fDevice.getDeviceId();
		Integer farmId = fDevice.getFarmId();
		Integer unitNo = fDevice.getUnitNo();
		String district = fDevice.getDeviceDistrict();
		String location = fDevice.getDeviceLocation();
		try {
			if (deviceId != null) {
				FarmCollectorDevice farmCollectorDevice = fCollectorDeviceDao.getTById(deviceId, FarmCollectorDevice.class);
				if (farmCollectorDevice == null) {
					return collectorDeviceValueArray;
				}
				DeviceValueBase deviceValue = collectorDeviceValueRedisUtil.getCollectorDeviceValueByFarm(farmCollectorDevice.getFarmId(), deviceId);
				if (deviceValue == null) {
					LOGGER.info("device={} is no deviceValue", farmCollectorDevice);
					return collectorDeviceValueArray;
				}
				JSONObject jsonObject = new JSONObject();
				JSONArray array = new JSONArray();
				fillCollectorDevice(array,deviceValue, deviceId, farmCollectorDevice);
				packagingDeviceValue(jsonObject, array, deviceValue.getDeviceValueType());
				collectorDeviceValueArray.add(jsonObject);
			} else if (deviceId == null && district != null) {
				collectorDeviceValueFiller(fDevice, collectorDeviceValueArray);
			} else if (district == null && location != null) {
				collectorDeviceValueFiller(fDevice, collectorDeviceValueArray);
			} else if (location != null && unitNo != null) {
				collectorDeviceValueFiller(fDevice, collectorDeviceValueArray);
			} else if (unitNo == null && farmId != null) {
				List<FarmCollectorDevice> farmCollectorDevices = farmCollectorDeviceService.queryFarmCollectorDeviceList(fDevice);
				Map<String, JSONArray> collectorDeviceTypeMap = new HashMap<String, JSONArray>();
				for (int i = 0; i < farmCollectorDevices.size(); i++) {
					FarmCollectorDevice farmCollectorDevice = farmCollectorDevices.get(i);
					Long newDeviceId = farmCollectorDevice.getDeviceId();
					DeviceValueBase deviceValueBase = collectorDeviceValueRedisUtil.getCollectorDeviceValueByFarm(farmId, newDeviceId);
					if (deviceValueBase != null) {
						String deviceValueType = deviceValueBase.getDeviceValueType().getCode();
						if (!collectorDeviceTypeMap.containsKey(deviceValueType)) {
							collectorDeviceTypeMap.put(deviceValueType, new JSONArray());
						}
						fillCollectorDevice(collectorDeviceTypeMap.get(deviceValueType), deviceValueBase, newDeviceId, farmCollectorDevice);
					}
				}
				for (Entry<String, JSONArray> entry : collectorDeviceTypeMap.entrySet()) {
					JSONObject jsonObject = new JSONObject();
					String type = entry.getKey();
					JSONArray dataArray = entry.getValue();
					packagingDeviceValue(jsonObject, dataArray, DeviceValueType.getValueTypeByCode(type));
					collectorDeviceValueArray.add(jsonObject);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("collector value error", e);
		}

		return collectorDeviceValueArray;
	}

	/**
	 * filler
	 * @param fDevice
	 * @param collectorDeviceValueArray
	 */
	private void collectorDeviceValueFiller(FarmCollectorDevice fDevice, JSONArray collectorDeviceValueArray) {
		List<FarmCollectorDevice> farmCollectorDevices = farmCollectorDeviceService.queryFarmCollectorDeviceList(fDevice);
		Map<String, JSONArray> collectorDeviceTypeMap = new HashMap<String, JSONArray>();
		for (int i = 0; i < farmCollectorDevices.size(); i++) {
			FarmCollectorDevice farmCollectorDevice = farmCollectorDevices.get(i);
			Long newDeviceId = farmCollectorDevice.getDeviceId();
			DeviceValueBase deviceValueBase = collectorDeviceValueRedisUtil.getCollectorDeviceValueByUnit(farmCollectorDevice.getUnitNo(), newDeviceId);
			if (deviceValueBase != null) {
				String deviceValueType = deviceValueBase.getDeviceValueType().getCode();
				if (!collectorDeviceTypeMap.containsKey(deviceValueType)) {
					collectorDeviceTypeMap.put(deviceValueType, new JSONArray());
				}
				fillCollectorDevice(collectorDeviceTypeMap.get(deviceValueType), deviceValueBase, newDeviceId, farmCollectorDevice);
			}
		}
		for (Entry<String, JSONArray> entry : collectorDeviceTypeMap.entrySet()) {
			JSONObject jsonObject = new JSONObject();
			String type = entry.getKey();
			JSONArray dataArray = entry.getValue();
			packagingDeviceValue(jsonObject, dataArray, DeviceValueType.getValueTypeByCode(type));
			collectorDeviceValueArray.add(jsonObject);
		}
	}

	public JSONObject getCollectorDeviceCacheValues(FarmCollectorDevice fDevice, String paramType) {
		JSONObject jsonObject = new JSONObject();
		Long deviceId = fDevice.getDeviceId();
		if (deviceId != null) {
			List<DeviceValueBase> deviceValueBases = collectorDeviceValueRedisUtil.getCollectorDeviceCacheValue(deviceId);
			if (deviceValueBases != null) {
				int size = deviceValueBases.size();
				ArrayList<Double> values = new ArrayList<>();
				ArrayList<String> times = new ArrayList<>();
				DeviceValueType deviceType = null;
				for (int i = 0; i < size; i++) {
					DeviceValueBase dValue = deviceValueBases.get(i);
					deviceType = dValue.getDeviceValueType();
					try {
						values.add(dValue.getDynamicParamValue(paramType));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					times.add(dValue.getUpdateTime().toString());
				}
				jsonObject.put("value", values);
				jsonObject.put("time", times);
				jsonObject.put("unit", getCollectDeviceUnit(deviceType, paramType));
			}
		}
		return jsonObject;
	}

	public String getCollectDeviceUnit(DeviceValueType deviceValueType, String paramType) {
		if (deviceValueType == null) {
			return "";
		}
		String[] paramCode = deviceValueType.getParamCode().split(",");
		String[] units = deviceValueType.getParamUnit().split(",");
		for (int i = 0; i < units.length; i++) {
			if (paramCode[i].equals(paramType)) {
				return units[i];
			}
		}
		return "";
	}

	public JSONArray screenCollectorDeviceValueByDistrictOrType(Integer farmId, String deviceDistrict, String deviceType) {
		JSONArray collectorDeviceValueArray = new JSONArray();
		Map<String, JSONArray> map = farmCollectorDeviceService.queryCollectorDeviceDistrict(farmId);
		if (deviceType != null) {
			JSONArray array = map.get(deviceType);
			analysisCollectorTypeAndAddtion(array, deviceDistrict, deviceType, collectorDeviceValueArray);
		} else {
			for (Entry<String, JSONArray> entry : map.entrySet()) {
				String type = entry.getKey();
				JSONArray array = entry.getValue();
				analysisCollectorTypeAndAddtion(array, deviceDistrict, type, collectorDeviceValueArray);
			}
		}
		return collectorDeviceValueArray;
	}

	public void analysisCollectorTypeAndAddtion(JSONArray array, String deviceDistrict, String deviceType, JSONArray collectorDeviceValueArray) {
		JSONObject jsonObject = new JSONObject();
		JSONArray list = new JSONArray();
		Long deviceNo = null;
		if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				JSONObject json = array.getJSONObject(i);
				deviceNo = json.getLong("deviceId");
				if (deviceNo != null) {
					FarmCollectorDevice farmCollectorDevice = fCollectorDeviceDao.getTById(deviceNo, FarmCollectorDevice.class);
					if (farmCollectorDevice == null || (deviceDistrict != null && !farmCollectorDevice.getDeviceDistrict().equals(deviceDistrict))) {
						continue;
					}
					JSONObject deviceValueJsonObject = JsonObjectUtil.fromBean(collectorDeviceValueRedisUtil.getCollectorDeviceValueByFarm(
							farmCollectorDevice.getFarmId(), deviceNo));
					if (deviceValueJsonObject == null) {
						deviceValueJsonObject = new JSONObject();
						deviceValueJsonObject.put("deviceId", deviceNo);
					}
					packagingDeviceDetail(deviceValueJsonObject, farmCollectorDevice);
					list.add(deviceValueJsonObject);
				}
			}
		}
		packagingDeviceValue(jsonObject, list, DeviceValueType.getValueTypeByCode(deviceType));
		collectorDeviceValueArray.add(jsonObject);

	}

	public JSONObject packagingDeviceDetail(JSONObject deviceValueJsonObject, FarmCollectorDevice farmCollectorDevice) {
		deviceValueJsonObject.put("deviceOrderNo", farmCollectorDevice.getDeviceOrderNo());
		deviceValueJsonObject.put("deviceDistrict", farmCollectorDevice.getDeviceDistrict());
		deviceValueJsonObject.put("deviceLocation", farmCollectorDevice.getDeviceLocation());
		deviceValueJsonObject.put("deviceType", farmCollectorDevice.getDeviceType());
		deviceValueJsonObject.put("deviceDistrict", farmCollectorDevice.getDeviceDistrict());
		return deviceValueJsonObject;
	}

	/**
	 * 封装实施数据
	 * 
	 * @param jsonObject
	 * @param array
	 * @param deviceValueType
	 * @return
	 */
	public JSONObject packagingDeviceValue(JSONObject jsonObject, JSONArray array, DeviceValueType deviceValueType) {
		if (deviceValueType == null) {
			return jsonObject;
		}
		jsonObject.put("data", array);
		jsonObject.put("type", deviceValueType.getCode());
		jsonObject.put("typeName", deviceValueType.getChineseName());
		jsonObject.put("header", deviceValueType.getParam());
		jsonObject.put("code", deviceValueType.getParamCode());
		jsonObject.put("unit", deviceValueType.getParamUnit());
		return jsonObject;
	}

	/**
	 * 获取采集设备的历史数据，可能涉及到不同设备类型字段不同
	 * 
	 * @param fCollectorDevice
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public JSONArray getHistoryCollectorDeviceValues(FarmCollectorDevice fCollectorDevice, Timestamp beginTime, Timestamp endTime) {
		List<Object> list = deviceHistoryValuesDao.getHistorySensorValues(fCollectorDevice, beginTime, endTime);
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			Object[] objects = (Object[]) list.get(i);
			JSONArray jsonArray = new JSONArray();
			for (int j = 0; j < objects.length; j++) {
				if (objects[j] != null) {
					jsonArray.add(objects[j].toString());
				} else {
					jsonArray.add("");
				}
			}
			array.add(jsonArray);
		}
		return array;
	}

	public JSONObject getHistorySensorValuesDynamic(FarmCollectorDevice fCollectorDevice, Timestamp beginTime, Timestamp endTime) {
		JSONArray jsonArray = getHistoryCollectorDeviceValues(fCollectorDevice, beginTime, endTime);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key", "农场编号,农场名称,设备号,设备编号,设备区域,设备类型,设备位置,光照强度,空气温度,空气湿度,土壤温度,土壤湿度,更新时间".split(","));
		jsonObject.put("data", jsonArray);
		return jsonObject;
	}

	public HSSFWorkbook getHistorySensorValuesExcel(FarmCollectorDevice fCollectorDevice, Timestamp beginTime, Timestamp endTime) {
		JSONArray jsonArray = getHistoryCollectorDeviceValues(fCollectorDevice, beginTime, endTime);
		CreateExcel excel = new CreateExcel(jsonArray, "农场编号,农场名称,设备号,设备编号,设备区域,设备类型,设备位置,光照强度,空气温度,空气湿度,土壤温度,土壤湿度,更新时间");
		return excel.getExcel();
	}

	public JSONArray getSensorDistrict(FarmCollectorDevice fCollectorDevice) {
		JSONArray array = new JSONArray();
		if (fCollectorDevice.getFarmId() != null) {
			if (fCollectorDevice.getDeviceDistrict() != null) {
				array = farmCollectorDeviceService.queryCollectorDeviceDistrict(fCollectorDevice.getFarmId()).get(
						fCollectorDevice.getDeviceDistrict());
			} else {
				Set<String> set = farmCollectorDeviceService.queryCollectorDeviceDistrict(fCollectorDevice.getFarmId()).keySet();
				array = JSONArray.parseArray(JSON.toJSONString(set));
			}
		}
		return array;
	}

	public void saveCollectorDeviceCache(Long deviceId, DeviceValueBase collectorDeviceValue) {
		try {
			collectorDeviceValueRedisUtil.saveCollectorDeviceCacheValue(deviceId, collectorDeviceValue);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("collector cache redis save error", e);
		}
	}
}
