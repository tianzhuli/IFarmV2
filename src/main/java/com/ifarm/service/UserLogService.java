package com.ifarm.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifarm.bean.UserLog;
import com.ifarm.dao.UserLogDao;
import com.ifarm.util.ClientIpUtil;
import com.ifarm.util.JsonObjectUtil;

@Service
public class UserLogService {
	@Autowired
	private UserLogDao userLogDao;

	public void saveUserLog(HttpServletRequest request, Object object, String userOperateType, String userOperateObject, String returnMessage) {
		UserLog userLog = new UserLog();
		userLog.setUserIp(ClientIpUtil.getIpAddress(request));
		userLog.setRequestMessage(JsonObjectUtil.toJsonObjectString(object).toString());
		userLog.setReturnMessage(returnMessage);
		userLog.setUserOperateObject(userOperateObject);
		userLog.setUserOperateType(userOperateType);
		userLogDao.saveBase(userLog);
	}

	public void saveUserLog(HttpServletRequest request, String message, String userOperateType, String userOperateObject, String returnMessage) {
		UserLog userLog = new UserLog();
		userLog.setUserIp(ClientIpUtil.getIpAddress(request));
		userLog.setRequestMessage(message);
		userLog.setReturnMessage(returnMessage);
		userLog.setUserOperateObject(userOperateObject);
		userLog.setUserOperateType(userOperateType);
		userLogDao.saveBase(userLog);
	}

	public void saveUserLog(HttpServletRequest request, Object object, String userId, String userOperateType, String userOperateObject, String returnMessage) {
		UserLog userLog = new UserLog();
		userLog.setUserId(userId);
		userLog.setUserIp(ClientIpUtil.getIpAddress(request));
		userLog.setRequestMessage(JsonObjectUtil.toJsonObjectString(object).toJSONString());
		userLog.setReturnMessage(returnMessage);
		userLog.setUserOperateObject(userOperateObject);
		userLog.setUserOperateType(userOperateType);
		userLogDao.saveBase(userLog);
	}
}
