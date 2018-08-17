package com.ifarm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.FarmControlDevice;
import com.ifarm.bean.FarmControlSystem;
import com.ifarm.bean.FarmControlTerminal;
import com.ifarm.bean.FarmWFMControlSystem;
import com.ifarm.constant.SystemResultCodeEnum;
import com.ifarm.dao.FarmControlDeviceDao;
import com.ifarm.dao.FarmControlSystemDao;
import com.ifarm.dao.FarmControlSystemWFMDao;
import com.ifarm.dao.FarmControlTerminalDao;
import com.ifarm.util.JsonObjectUtil;
import com.ifarm.util.SystemResultEncapsulation;

@Service
@SuppressWarnings("rawtypes")
public class FarmControlTerminalService {
	@Autowired
	private FarmControlTerminalDao farmControlTerminalDao;
	@Autowired
	private FarmControlDeviceDao farmControlDeviceDao;
	@Autowired
	private FarmControlSystemDao farmControlSystemDao;
	@Autowired
	private FarmControlSystemWFMDao farmControlSystemWFMDao;
	
	String[] controlTerminalKeys = { "functionName", "functionCode" };

	private static final Logger FARM_CONTROL_TERMINAL_SERVICE_LOGGER = LoggerFactory.getLogger(FarmControlTerminalService.class);

	public String getFarmControlOperationList(FarmControlTerminal farmControlTerminal) {
		List list = farmControlTerminalDao.getFarmControlOperationList(farmControlTerminal);
		JSONArray array = new JSONArray();
		Map<String, String> functionMap = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			Object[] objects = (Object[]) list.get(i);
			JSONObject jsonObject = new JSONObject();
			if (!functionMap.containsKey(objects[1])) {
				jsonObject.put(controlTerminalKeys[0], objects[0]);
				jsonObject.put(controlTerminalKeys[1], objects[1]);
				array.add(jsonObject);
				functionMap.put(objects[1].toString(), "");
			}
		}
		return array.toString();
	}

	public String saveFarmControlSystem(FarmControlTerminal farmControlTerminal) {
		try {
			if (farmControlTerminal.getControlDeviceId() == null || farmControlTerminal.getSystemId() == null
					|| farmControlTerminal.getControlDeviceBit() == null) {
				return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.NO_ID);
			}
			FarmControlDevice farmControlDevice = farmControlDeviceDao.getTById(farmControlTerminal.getControlDeviceId(), FarmControlDevice.class);
			if (farmControlDevice == null) {
				return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.NO_DEVICE);
			}
			FarmControlSystem farmControlSystem = farmControlSystemDao.getTById(farmControlTerminal.getSystemId(), FarmControlSystem.class);
			FarmWFMControlSystem fWfmControlSystem = farmControlSystemWFMDao.getTById(farmControlTerminal.getSystemId(), FarmWFMControlSystem.class);
			if (farmControlSystem == null && fWfmControlSystem == null) {
				return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.NO_SMYSTM);
			}
			farmControlTerminalDao.saveFarmControlTerminal(farmControlTerminal);
			// CacheDataBase.controlTeminalDetailMap.put(terminalId,
			// JsonObjectUtil.fromBean(farmControlTerminal));
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			FARM_CONTROL_TERMINAL_SERVICE_LOGGER.error(e.getMessage());
			FARM_CONTROL_TERMINAL_SERVICE_LOGGER.error("添加控制系统终端", e);
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.ERROR);
		}
	}

	public String getFarmControlTerminals(FarmControlTerminal farmControlTerminal) {
		List<FarmControlTerminal> controlTerminalrms = farmControlTerminalDao.getDynamicList(farmControlTerminal);
		return JsonObjectUtil.toJsonArrayString(controlTerminalrms);
	}

	public String deleteFarmControlTerminal(FarmControlTerminal farmControlTerminal) {
		try {
			farmControlTerminalDao.deleteBase(farmControlTerminal);
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			FARM_CONTROL_TERMINAL_SERVICE_LOGGER.error(JSON.toJSONString(farmControlTerminal) + "-delete error", e);
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.ERROR);
		}
	}
}
