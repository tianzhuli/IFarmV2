package com.ifarm.nosql.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifarm.nosql.bean.UserLogMongo;
import com.ifarm.nosql.dao.UserLogMongoDao;
import com.ifarm.util.ClientIpUtil;
import com.ifarm.util.JsonObjectUtil;

@Service
public class UserLogMongoService {
	@Autowired
	private UserLogMongoDao userLogMongoDao;

	public void saveUserLog(HttpServletRequest request, Object object, String userId, String userOperateType, String userOperateObject, String returnMessage) {
		UserLogMongo userLogMongo = new UserLogMongo();
		userLogMongo.setUserIp(ClientIpUtil.getIpAddress(request));
		userLogMongo.setRequestMessage(JsonObjectUtil.toJsonObjectString(object).toJSONString());
		userLogMongo.setUserId(userId);
		userLogMongo.setReturnMessage(returnMessage);
		userLogMongo.setUserOperateObject(userOperateObject);
		userLogMongo.setUserOperateType(userOperateType);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		userLogMongo.setLogCreateTime(Timestamp.valueOf(simpleDateFormat.format(new Date())));
		userLogMongoDao.saveUserLog(userLogMongo);
	}

	public String getUserLog(String userId) {
		return userLogMongoDao.getUserLog(userId);
	}
}
