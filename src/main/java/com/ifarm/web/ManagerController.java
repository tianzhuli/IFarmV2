package com.ifarm.web;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ifarm.annotation.AuthPassport;
import com.ifarm.bean.FarmControlSystem;
import com.ifarm.bean.Manager;
import com.ifarm.constant.SystemResultCodeEnum;
import com.ifarm.nosql.service.UserLogMongoService;
import com.ifarm.service.FarmControlSystemService;
import com.ifarm.service.ManagerService;
import com.ifarm.service.UserService;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.ConDb;

@RestController
@RequestMapping(value = "manager")
public class ManagerController {
	@Autowired
	private ManagerService managerService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserLogMongoService logService;
	
	@Autowired
	private FarmControlSystemService farmControlService;
	
	@RequestMapping(value = "allManager")
	@AuthPassport(validate = false)
	public String getAllManager() {
		return managerService.getAllManager();
	}

	@RequestMapping(value = "allUser")
	public String getAllUser(String managerId) {
		try {
			return userService.getAllUserList();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return SystemResultCodeEnum.ERROR;
		}
		
	}

	@RequestMapping(value = "updateManager")
	public String updateManager(Manager manager) {
		return managerService.updateManager(manager);
	}

	@RequestMapping(value = "updateTopic")
	public String updateTopic() throws Exception {
		ConDb conDb = new ConDb();
		Connection con = conDb.openCon();
		CacheDataBase.loadTopics(con);
		con.close();
		if (!CacheDataBase.mqttService.mqttClient.isConnected()) {
			CacheDataBase.mqttService.connect();
		}
		CacheDataBase.loadMqttService();
		return "success";
	}

	@RequestMapping(value = "userLog")
	public String getUserLog(@RequestParam("userId") String userId) throws Exception {
		return logService.getUserLog(userId);
	}

	@RequestMapping(value = "login")
	@AuthPassport(validate = false)
	public String managerLogin(Manager manager) {
		return managerService.managerLogin(manager);
	}
	
	@RequestMapping("controlSystemList")
	public String farmControlSystemsDynamicList(FarmControlSystem farmControlSystem, String userId) {
		return farmControlService.farmControlSystemsDynamicList(farmControlSystem, userId);
	}

}
