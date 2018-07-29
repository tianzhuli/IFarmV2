package com.ifarm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.FarmWFMControlSystem;
import com.ifarm.constant.SystemResultCodeEnum;
import com.ifarm.dao.FarmControlSystemWFMDao;
import com.ifarm.util.JsonObjectUtil;
import com.ifarm.util.SystemResultEncapsulation;

@Service
public class FarmControlSystemWFMService {
	@Autowired
	private FarmControlSystemWFMDao farmControlSystemWFMDao;
	private static final Logger farmControlSystemService_log = LoggerFactory.getLogger(FarmControlSystemWFMService.class);
	
	public String saveControlSystem(FarmWFMControlSystem farmControlSystem) {
		Integer farmId = farmControlSystem.getFarmId();
		if (farmId == null) {
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.NO_ID);
		}
		try {
			JSONObject jsonObject = JsonObjectUtil.fromBean(farmControlSystem);
			farmControlSystem.setSystemType("水肥药系统");
			farmControlSystem.setSystemCode("waterFertilizerMedicine");
			farmControlSystem.setSystemTypeCode("waterFertilizerMedicineControl");
			Integer systemId = farmControlSystemWFMDao.saveFarmControlSystem(farmControlSystem);
			jsonObject.put("systemId", systemId);
			//CacheDataBase.controlSystemValueMap.put(systemId, jsonObject);
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			farmControlSystemService_log.error(e.getMessage());
			farmControlSystemService_log.error("添加控制系统", e);
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.ERROR);
		}
	}
	
	public String farmControlSystemWFMTerimal(FarmWFMControlSystem fControlSystem) {
		JSONArray jsonArray = new JSONArray();
		FarmWFMControlSystem fWfmControlSystem = farmControlSystemWFMDao.getTById(fControlSystem.getSystemId(), FarmWFMControlSystem.class);
		Integer medicineNum = fWfmControlSystem.getMedicineNum();
		Integer districtNum = fWfmControlSystem.getDistrictNum();
		Integer fertierNum = fWfmControlSystem.getFertierNum();
		String controlType = "waterFertilizerMedicineControl";
		JSONObject pumpJsonObject = new JSONObject();
		pumpJsonObject.put("controlType", controlType);
		pumpJsonObject.put("terminalIdentifying", "pump");
		pumpJsonObject.put("functionName", "正转");
		pumpJsonObject.put("functionCode", "forward");
		jsonArray.add(pumpJsonObject);
		for (int i = 1; i <= medicineNum; i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("controlType", controlType);
			jsonObject.put("terminalIdentifying", "medicineCan" + i);
			jsonObject.put("functionName", "正转");
			jsonObject.put("functionCode", "forward");
			jsonArray.add(jsonObject);
			jsonObject.put("functionName", "反转");
			jsonObject.put("functionCode", "reverse");
			jsonArray.add(jsonObject);
		}
		for (int i = 1; i <= districtNum; i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("controlType", controlType);
			jsonObject.put("terminalIdentifying", "district" + i);
			jsonObject.put("functionName", "正转");
			jsonObject.put("functionCode", "forward");
			jsonArray.add(jsonObject);
			jsonObject.put("functionName", "反转");
			jsonObject.put("functionCode", "reverse");
			jsonArray.add(jsonObject);
		}
		for (int i = 1; i <= fertierNum; i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("controlType", controlType);
			jsonObject.put("terminalIdentifying", "fertilizerCan" + i);
			jsonObject.put("functionName", "正转");
			jsonObject.put("functionCode", "forward");
			jsonArray.add(jsonObject);
			jsonObject.put("functionName", "反转");
			jsonObject.put("functionCode", "reverse");
			jsonArray.add(jsonObject);
		}
		return jsonArray.toString();
	}
	
	public FarmWFMControlSystem getFarmControlSystemById(Integer systemId) {
		return farmControlSystemWFMDao.getTById(systemId, FarmWFMControlSystem.class);
	}
	
	public String deleteFarmControlSystem(FarmWFMControlSystem farmControlSystem) {
		try {
			farmControlSystemWFMDao.deleteBase(farmControlSystem);
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			farmControlSystemService_log.error(JSON.toJSONString(farmControlSystem) + "-delete error", e);
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.ERROR);
		}
	}
}
