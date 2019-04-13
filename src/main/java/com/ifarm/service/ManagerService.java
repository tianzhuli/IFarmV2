package com.ifarm.service;

import java.lang.reflect.Field;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifarm.bean.Manager;
import com.ifarm.constant.SystemResultCodeEnum;
import com.ifarm.dao.ManagerDao;
import com.ifarm.nosql.bean.ManagerToken;
import com.ifarm.nosql.dao.ManagerTokenDao;
import com.ifarm.util.JsonObjectUtil;
import com.ifarm.util.SystemResultEncapsulation;

@Service
public class ManagerService {
	@Autowired
	private ManagerDao managerDao;

	@Autowired
	private ManagerTokenDao managerTokenDao;

	private static final Logger managerService_log = LoggerFactory.getLogger(ManagerService.class);
	private static final String SUPER_ADMIN = "admin";

	public String getAllManager() {
		return JsonObjectUtil.toJsonArrayString(managerDao.getAllManager());
	}

	public String updateManager(Manager manager) {
		Manager newManager = changeManager(manager);
		if (managerDao.updateManager(newManager)) {
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.SUCCESS);
		} else {
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.ERROR);
		}
	}

	public String managerGetSignature(String managerId) {
		if (managerId == null) {
			return SystemResultCodeEnum.ERROR;
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("ifarm");
		stringBuffer.append(UUID.randomUUID());
		stringBuffer.append(Base64.encodeBase64String(managerId.getBytes()));
		String token = stringBuffer.toString().replace("-", "");
		managerService_log.info(managerId + ":" + token);
		return token;
	}

	public Manager getManagerById(String managerId) {
		return managerDao.getManagerById(managerId);
	}

	public Manager changeManager(Manager newManager) {
		Manager oldManager = managerDao.getManagerById(newManager.getManagerId());
		Field[] fields = oldManager.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				fields[i].setAccessible(true);
				if (fields[i].get(newManager) != null) {
					fields[i].set(oldManager, fields[i].get(newManager));
				}
			} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return oldManager;
	}

	public String getManager(Manager manager) {
		return JsonObjectUtil.toJsonArrayString(managerDao.getDynamicList(manager));
	}

	public String managerLogin(Manager manager) {
		try {
			boolean flag = managerDao.login(manager.getManagerId(), manager.getManagerPwd());
			if (flag) {
				ManagerToken mToken = new ManagerToken();
				mToken.setManagerId(manager.getManagerId());
				String token = managerGetSignature(manager.getManagerId());
				mToken.setToken(token);
				managerTokenDao.saveManagerToken(mToken);
				Manager realManager = managerDao.getTById(manager.getManagerId(), Manager.class);
				if (SUPER_ADMIN.equals(realManager.getRoler()) || realManager.getRoler().equals(manager.getRoler())) {
					return SystemResultEncapsulation.resultTokenDecorate(SystemResultCodeEnum.SUCCESS, token);
				}
				return SystemResultEncapsulation.resultTokenDecorate(SystemResultCodeEnum.AUTH_ERROR, token);
			} else {
				return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.ERROR);
			}
		} catch (Exception e) {
			// TODO: handle exception
			managerService_log.error(e.getMessage());
			managerService_log.error("manager login", e);
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.ERROR);
		}
	}
}
