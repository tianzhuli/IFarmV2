package com.ifarm.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.ifarm.bean.Farm;
import com.ifarm.bean.UserFarmAuthority;
import com.ifarm.constant.SystemResultCodeEnum;
import com.ifarm.dao.FarmDao;
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
	private UserFarmAuthorityDao userFarmAuthorityDao;
	
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public String saveFarm(Farm farm) {
		farm.setFarmCreateTime(Timestamp.valueOf(simpleDateFormat.format(new Date())));
		if (farmDao.saveBase(farm)) {
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.SUCCESS);
		} else {
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
}
