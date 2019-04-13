package com.ifarm.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.ifarm.bean.Farm;
import com.ifarm.bean.FarmPosition;
import com.ifarm.bean.FarmRegion;
import com.ifarm.bean.UserFarmAuthority;
import com.ifarm.constant.SystemResultCodeEnum;
import com.ifarm.dao.FarmDao;
import com.ifarm.dao.FarmPositionDao;
import com.ifarm.dao.FarmRegionDao;
import com.ifarm.dao.UserDao;
import com.ifarm.dao.UserFarmAuthorityDao;
import com.ifarm.util.JsonObjectUtil;
import com.ifarm.util.SystemResultEncapsulation;

@Service
public class FarmService {
	@Autowired
	private FarmDao farmDao;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private FarmRegionDao farmRegionDao;
	
	@Autowired
	private FarmPositionDao farmPositionDao;

	@Autowired
	private UserFarmAuthorityDao userFarmAuthorityDao;
	
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public String saveFarm(Farm farm) {
		farm.setFarmCreateTime(Timestamp.valueOf(simpleDateFormat.format(new Date())));
		try {
			farmDao.saveBase(farm);
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.ERROR);
		}
	}

	public String updateFarm(Farm farm) {
		if (farmDao.updateDynamic(farm)) {
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.SUCCESS);
		} else {
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.ERROR);
		}
	}

	public String getFarmsList(String userId) {
		if (userId==null) {
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.NULL);
		}
		if (userId.contains("_")) {
			JSONArray jsonArray = new JSONArray();
			userId = userId.split("_")[0];
			List<UserFarmAuthority> uAuthorities = userFarmAuthorityDao.queryFarmAuthorityBySubUserId(userId);
			for (int i = 0; i < uAuthorities.size(); i++) {
				UserFarmAuthority userFarmAuthority = uAuthorities.get(i);
				Integer farmId = userFarmAuthority.getFarmId();
				Farm farm = farmDao.getTById(farmId, Farm.class);
				jsonArray.add(JsonObjectUtil.fromBean(farm));
			}
			return jsonArray.toString();
		}// 子用户可以查询对应农场
		List<Farm> list = farmDao.getFarmsList(userId);
		return JsonObjectUtil.toJsonArrayString(list);
	}

	public String getUserAroundFarmList(String aroundPersonId) {
		return JsonObjectUtil.toJsonArrayString(farmDao.getFarmsList(aroundPersonId));
	}
	
	public String addFarmRegion(FarmRegion farmRegion) {
		farmRegionDao.saveBase(farmRegion);
		return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.SUCCESS); 
	}
	
	public String queryFarmRegionList(FarmRegion farmRegion) {
		List<FarmRegion> farmRegions = farmRegionDao.getDynamicList(farmRegion);
		return JsonObjectUtil.toJsonArray(farmRegions).toJSONString();
	}
	
	public String deleteRegion(FarmRegion farmRegion) {
		List<FarmRegion> farmRegions = farmRegionDao.getDynamicList(farmRegion);
		for (int i = 0; i < farmRegions.size(); i++) {
			farmRegionDao.deleteBase(farmRegions.get(i));
		}
		return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.SUCCESS); 
	}
	
	public String addFarmPosition(FarmPosition farmPosition) {
		farmPositionDao.saveBase(farmPosition);
		return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.SUCCESS); 
	}
	
	public String queryFarmPosition(FarmPosition farmPosition) {
		List<FarmPosition> list = farmPositionDao.getDynamicList(farmPosition);
		return JsonObjectUtil.toJsonArray(list).toJSONString();
	}
	
	public String deleteFarmPosition(FarmPosition farmPosition) {
		List<FarmPosition> list = farmPositionDao.getDynamicList(farmPosition);
		for (int i = 0; i < list.size(); i++) {
			farmPositionDao.deleteBase(list.get(i));
		}
		return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.SUCCESS); 
	}
}
