package com.ifarm.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.CollectorValue;
import com.ifarm.bean.FarmCollector;
import com.ifarm.dao.CollectorValueDao;
import com.ifarm.dao.FarmCollectorDao;
import com.ifarm.redis.util.FarmCollectorValueRedisUtil;
import com.ifarm.util.ConvertData;
import com.ifarm.util.JsonObjectUtil;

@Service
public class CollectorValueService {
	@Autowired
	private CollectorValueDao collectorValueDao;

	@Autowired
	private FarmCollectorDao farmCollectorDao;
	
	@Autowired
	private FarmCollectorValueRedisUtil fCollectorValueRedisUtil;
	
	private ConvertData convertData = new ConvertData();

	public void saveCollectorValues(byte[] arr, int size) {
		CollectorValue collectorValues = new CollectorValue();
		Long collectorId = convertData.byteToConvertLong(arr, 12, 4);
		collectorValues.setCollectorId(collectorId);
		collectorValues.setCollectorSignal((double) convertData.getdataType3(arr, 6));
		collectorValues.setCollectorSumItem(convertData.getdataType3(arr, 8));
		collectorValues.setCollectorValidItem(convertData.getdataType3(arr, 10));
		collectorValues.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		collectorValueDao.saveCollectorValues(collectorValues);
		fCollectorValueRedisUtil.setRedisStringValue(collectorId.toString(), collectorValues);
		//CacheDataBase.collectorStateValueMap.put(collectorId, JsonObjectUtil.fromBean(collectorValues));
	}

	public String getCollectorValues(FarmCollector farmCollector) {
		JSONArray jsonArray = new JSONArray();
		if (farmCollector.getCollectorId() != null) {
			//JSONObject jsonObject = CacheDataBase.collectorStateValueMap.get(farmCollector.getCollectorId());
			JSONObject jsonObject = JsonObjectUtil.fromBean(fCollectorValueRedisUtil.getRedisStringValue(farmCollector.getCollectorId().toString()));
			jsonArray.add(jsonObject);
			return jsonArray.toString();
		} else if (farmCollector.getFarmId() != null) {
			List<FarmCollector> list = farmCollectorDao.getDynamicList(farmCollector);
			for (int i = 0; i < list.size(); i++) {
				FarmCollector currentFarmCollector = list.get(i);
				//JSONObject jsonObject = CacheDataBase.collectorStateValueMap.get(currentFarmCollector.getCollectorId());
				JSONObject jsonObject = JsonObjectUtil.fromBean(fCollectorValueRedisUtil.getRedisStringValue(currentFarmCollector.getCollectorId().toString()));
				jsonArray.add(jsonObject);
			}
			return jsonArray.toString();
		} else {
			return null;
		}
	}
}
