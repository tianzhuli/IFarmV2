package com.ifarm.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.FarmCollectorUnit;
import com.ifarm.dao.FarmCollectorUnitDao;
import com.ifarm.enums.ServiceHeadEnum;
import com.ifarm.enums.SystemReturnCodeEnum;
import com.ifarm.util.JsonObjectUtil;
import com.ifarm.util.SystemResultEncapsulation;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class FarmCollectorUnitService extends
		AbstractFarmService<FarmCollectorUnitDao, FarmCollectorUnit> {

	@Autowired
	private FarmCollectorUnitDao farmCollectorUnitDao;

	public String saveCollectorUnit(FarmCollectorUnit farmCollectorUnit) {
		validator(farmCollectorUnit);
		// 唯一性校验
		FarmCollectorUnit existUnit = new FarmCollectorUnit();
		existUnit.setFarmId(farmCollectorUnit.getFarmId());
		existUnit.setUnitDistrict(farmCollectorUnit.getUnitDistrict());
		existUnit.setUnitPosition(farmCollectorUnit.getUnitPosition());
		List<FarmCollectorUnit> farmControlUnits = farmCollectorUnitDao
				.getDynamicList(existUnit);
		if (farmControlUnits.size() > 0) {
			return SystemResultEncapsulation
					.fillResultCode(SystemReturnCodeEnum.UNIQUE_ERROR);
		}
		return super.baseSave(farmCollectorUnit);
	}

	/**
	 * 采集单元的区域和位置
	 * 
	 * @param farmId
	 * @return
	 */
	public String controlUnitRegion(String farmId) {
		JSONObject jsonObject = new JSONObject();
		List list = farmCollectorUnitDao.collectorUnitRegion(farmId);
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
	 * 区域位置的采集单元
	 * 
	 * @param farmId
	 * @param unitDistrict
	 * @param unitLocation
	 * @return
	 */
	public String regionControlUnit(String farmId, String unitDistrict,
			String unitLocation) {
		List list = farmCollectorUnitDao.regionCollectorUnit(farmId, unitDistrict,
				unitLocation);
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
				itemArray.add(extJsonObject);
			}
			jsonArray.add(itemArray);
		}
		return JsonObjectUtil.buildCommandJsonObject(jsonArray,
				ServiceHeadEnum.REGION_COLLECTOR_UNIT).toJSONString();
	}
}
