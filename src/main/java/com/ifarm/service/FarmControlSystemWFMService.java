package com.ifarm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.FarmControlUnit;
import com.ifarm.bean.FarmWFMControlSystem;
import com.ifarm.constant.SystemResultCodeEnum;
import com.ifarm.dao.FarmControlSystemWFMDao;
import com.ifarm.dao.FarmControlUnitDao;
import com.ifarm.util.JsonObjectUtil;
import com.ifarm.util.SystemResultEncapsulation;

@Service
public class FarmControlSystemWFMService {
	@Autowired
	private FarmControlSystemWFMDao farmControlSystemWFMDao;

	@Autowired
	private FarmControlUnitDao farmControlUnitDao;

	private static final Logger farmControlSystemService_log = LoggerFactory
			.getLogger(FarmControlSystemWFMService.class);

	public String saveControlSystem(FarmWFMControlSystem farmControlSystem) {
		Integer farmId = farmControlSystem.getFarmId();
		if (farmId == null) {
			return SystemResultEncapsulation
					.resultCodeDecorate(SystemResultCodeEnum.NO_ID);
		}
		try {
			JSONObject jsonObject = JsonObjectUtil.fromBean(farmControlSystem);
			farmControlSystem.setSystemType("水肥药系统");
			farmControlSystem.setSystemCode("waterFertilizerMedicine");
			farmControlSystem
					.setSystemTypeCode("waterFertilizerMedicineControl");
			Integer systemId = farmControlSystemWFMDao
					.saveFarmControlSystem(farmControlSystem);
			jsonObject.put("systemId", systemId);
			// CacheDataBase.controlSystemValueMap.put(systemId, jsonObject);
			return SystemResultEncapsulation
					.resultCodeDecorate(SystemResultCodeEnum.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			farmControlSystemService_log.error(e.getMessage());
			farmControlSystemService_log.error("添加控制系统", e);
			return SystemResultEncapsulation
					.resultCodeDecorate(SystemResultCodeEnum.ERROR);
		}
	}
	
	public String farmControlSystemWFMTerimal(FarmControlUnit farmControlUnit) {
		JSONArray jsonArray = new JSONArray();
		FarmControlUnit wfmControlUnit = farmControlUnitDao.getTById(
				farmControlUnit.getUnitId(), FarmControlUnit.class);
		JSONObject wfmJsonObject = JSONObject.parseObject(wfmControlUnit
				.getUnitExtInfo());
		Integer medicineNum = wfmJsonObject.getInteger("medicineNum");
		Integer fertierNum = wfmJsonObject.getInteger("fertierNum");
		String controlType = "waterFertilizerMedicineControl";
		JSONObject pumpJsonObject = new JSONObject();
		pumpJsonObject.put("controlType", controlType);
		pumpJsonObject.put("terminalIdentifying", "pump");
		pumpJsonObject.put("functionName", "开");
		pumpJsonObject.put("functionCode", "enable");
		pumpJsonObject.put("terminalName", "水泵");
		jsonArray.add(pumpJsonObject);
		for (int i = 1; i <= medicineNum; i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("controlType", controlType);
			jsonObject.put("terminalIdentifying", "medicineCan" + i);
			jsonObject.put("functionName", "开");
			jsonObject.put("functionCode", "enable");
			jsonObject.put("terminalName", "药罐" + i);
			jsonArray.add(jsonObject);
		}
		for (int i = 1; i <= 1; i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("controlType", controlType);
			jsonObject.put("terminalIdentifying", "district" + i);
			jsonObject.put("functionName", "开");
			jsonObject.put("functionCode", "enable");
			jsonObject.put("terminalName", "区域" + i);
			jsonArray.add(jsonObject);
		}
		for (int i = 1; i <= fertierNum; i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("controlType", controlType);
			jsonObject.put("terminalIdentifying", "fertilizerCan" + i);
			jsonObject.put("functionName", "开");
			jsonObject.put("functionCode", "enable");
			jsonObject.put("terminalName", "肥罐" + i);
			jsonArray.add(jsonObject);
		}
		return jsonArray.toString();
	}

	public FarmWFMControlSystem getFarmControlSystemById(Integer systemId) {
		return farmControlSystemWFMDao.getTById(systemId,
				FarmWFMControlSystem.class);
	}

	public String deleteFarmControlSystem(FarmWFMControlSystem farmControlSystem) {
		try {
			farmControlSystemWFMDao.deleteBase(farmControlSystem);
			return SystemResultEncapsulation
					.resultCodeDecorate(SystemResultCodeEnum.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			farmControlSystemService_log.error(
					JSON.toJSONString(farmControlSystem) + "-delete error", e);
			return SystemResultEncapsulation
					.resultCodeDecorate(SystemResultCodeEnum.ERROR);
		}
	}

	public String queryFarmControlSystem(FarmWFMControlSystem farmControlSystem) {
		return JsonObjectUtil.toJsonArrayString(farmControlSystemWFMDao
				.getDynamicListAddLike(farmControlSystem));
	}
}
