package com.ifarm.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.FarmControlUnit;
import com.ifarm.dao.FarmControlUnitDao;
import com.ifarm.enums.ControlSystemEnum;
import com.ifarm.enums.ServiceHeadEnum;
import com.ifarm.enums.SystemReturnCodeEnum;
import com.ifarm.util.JsonObjectUtil;
import com.ifarm.util.SystemResultEncapsulation;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class FarmControlUnitService extends
		AbstractFarmService<FarmControlUnitDao, FarmControlUnit> {

	@Autowired
	private FarmControlUnitDao farmControlUnitDao;

	public String saveControlUnit(FarmControlUnit farmControlUnit) {
		ControlSystemEnum systemEnum = ControlSystemEnum
				.getValueByCode(farmControlUnit.getSystemCode());
		if (systemEnum == null) {
			return SystemResultEncapsulation
					.fillResultCode(SystemReturnCodeEnum.PARAM_ERROR);
		}
		farmControlUnit.setSystemType(systemEnum.getType());
		validator(farmControlUnit);
		// 唯一性校验
		FarmControlUnit existUnit = new FarmControlUnit();
		existUnit.setFarmId(farmControlUnit.getFarmId());
		existUnit.setSystemCode(farmControlUnit.getSystemCode());
		existUnit.setUnitDistrict(farmControlUnit.getUnitDistrict());
		existUnit.setUnitPosition(farmControlUnit.getUnitPosition());
		List<FarmControlUnit> farmControlUnits = farmControlUnitDao
				.getDynamicList(existUnit);
		if (farmControlUnits.size() > 0) {
			return SystemResultEncapsulation
					.fillResultCode(SystemReturnCodeEnum.UNIQUE_ERROR);
		}

		/*if (systemEnum.equals(ControlSystemEnum.WATER_FERTILIZER_MEDICINDE)) {
			if (!validatorWfmExt(farmControlUnit.getUnitExtInfo())) {
				return SystemResultEncapsulation.fillResultCode(
						SystemReturnCodeEnum.PARAM_ERROR,
						"medicineNum or fertierNum is null");
			}
		} else {
			if ((boolean) CacheDataBase.systemConfigCacheMap
					.get(SystemConfigCache.CONTROL_SYSTEM_TYPE_DRM)) {
				JSONArray array = CacheDataBase.initBaseConfig
						.get("controlSystemType.json")
						.getJSONObject("controlUnitExt")
						.getJSONArray(systemEnum.getType());
				for (int i = 0; i < array.size(); i++) {
					if (!validatorExtInfo(farmControlUnit.getUnitExtInfo(),
							array.getString(i))) {
						return SystemResultEncapsulation.fillResultCode(
								SystemReturnCodeEnum.PARAM_ERROR,
								array.getString(i) + " is null");
					}
				}
			}
		}*/
		return super.baseSave(farmControlUnit);
	}

	public boolean validatorWfmExt(String extInfo) {
		if (extInfo != null) {
			try {
				JSONObject extJsonObject = JSONObject.parseObject(extInfo);
				if (extJsonObject.getInteger("medicineNum") != null
						&& extJsonObject.getInteger("fertierNum") != null) {
					return true;
				}
			} catch (Exception e) {
				// TODO: handle exception
				FARM_SERVICE_LOG.error("json parse error", e);
			}
		}
		return false;
	}

	public boolean validatorExtInfo(String extInfo, String key) {
		if (extInfo != null) {
			try {
				JSONObject extJsonObject = JSONObject.parseObject(extInfo);
				if (extJsonObject.getInteger(key) != null) {
					return true;
				}
			} catch (Exception e) {
				// TODO: handle exception
				FARM_SERVICE_LOG.error("json parse error", e);
			}
		}
		return false;
	}

	/**
	 * 控制单元的区域和位置
	 * 
	 * @param farmId
	 * @return
	 */
	public String controlUnitRegion(String farmId) {
		JSONObject jsonObject = new JSONObject();
		List list = farmControlUnitDao.controlUnitRegion(farmId);
		if (list == null || list.size() == 0) {
			return jsonObject.toJSONString();
		}
		int i = 0;
		do {
			Object[] objects = (Object[]) list.get(i);
			String systemDistrict = String.valueOf(objects[0]);
			HashSet<String> districtSet = (HashSet<String>) jsonObject
					.get(systemDistrict);
			if (districtSet == null) {
				districtSet = new HashSet<>();
			}
			districtSet.add(String.valueOf(objects[1]));
			jsonObject.put(systemDistrict, districtSet);
			i++;
		} while (i < list.size());
		return jsonObject.toJSONString();
	}

	/**
	 * 区域位置的控制单元
	 * 
	 * @param farmId
	 * @param unitDistrict
	 * @param unitLocation
	 * @return
	 */
	public String regionControlUnit(String farmId, String unitDistrict,
			String unitLocation, String systemCode) {
		List list = farmControlUnitDao.regionControlUnit(farmId, unitDistrict,
				unitLocation, systemCode);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			Object[] objects = (Object[]) list.get(i);
			JSONArray itemArray = new JSONArray();
			for (int j = 0; j < objects.length - 1; j++) {
				itemArray.add(objects[j]);
			}
			JSONObject extJsonObject = new JSONObject();
			if (objects[objects.length - 1] != null) {
				extJsonObject = JSONObject.parseObject(String
						.valueOf(objects[objects.length - 1]));
			}
			itemArray.add(extJsonObject);
			ControlSystemEnum systemEnum = ControlSystemEnum
					.getValueByCode(String.valueOf(objects[3]));
			if (systemEnum != null) {
				itemArray.add(systemEnum.getShortName());
				itemArray.add(systemEnum.getType());
			} else {
				itemArray.add("其他");
				itemArray.add("Control");
			}
			jsonArray.add(itemArray);
		}
		return JsonObjectUtil.buildCommandJsonObject(jsonArray,
				ServiceHeadEnum.REGION_CONTROL_UNIT).toJSONString();
	}
}
