package com.ifarm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.FarmControlDevice;
import com.ifarm.bean.FarmControlTerminal;
import com.ifarm.bean.FarmControlUnit;
import com.ifarm.dao.FarmControlDeviceDao;
import com.ifarm.dao.FarmControlTerminalDao;
import com.ifarm.dao.FarmControlUnitDao;
import com.ifarm.enums.SystemReturnCodeEnum;
import com.ifarm.exception.ValidatorException;
import com.ifarm.util.BaseIfarmUtil;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.JsonObjectUtil;
import com.ifarm.util.StringUtil;
import com.ifarm.util.SystemResultEncapsulation;

@Service
@SuppressWarnings("rawtypes")
public class FarmControlTerminalService extends
		AbstractFarmService<FarmControlTerminalDao, FarmControlTerminal> {
	@Autowired
	private FarmControlTerminalDao farmControlTerminalDao;
	@Autowired
	private FarmControlDeviceDao farmControlDeviceDao;
	@Autowired
	private FarmControlUnitDao farmControlUnitDao;

	String[] controlTerminalKeys = { "functionName", "functionCode",
			"terminalNoArray" };

	String[] wfmFunctionName = { "灌溉", "施肥", "施药" };

	String[] wfmFunctionCode = { "irrigate", "fertilizer", "medicine" };

	public String getFarmControlOperationList(
			FarmControlTerminal farmControlTerminal) {
		List<FarmControlTerminal> farmControlTerminals = farmControlTerminalDao
				.getDynamicList(farmControlTerminal);
		FarmControlUnit farmControlUnit = farmControlUnitDao.getTById(
				farmControlTerminal.getUnitId(), FarmControlUnit.class);
		if (farmControlTerminals.size() <= 0 || farmControlUnit == null) {
			throw new ValidatorException("unit terminal no config");
		}
		JSONArray jsonArray = new JSONArray();
		JSONObject resultJsonObject = new JSONObject();
		JSONObject functionCodeJsonObject = new JSONObject();
		JSONObject terminalJsonObject = new JSONObject();
		for (FarmControlTerminal terminal : farmControlTerminals) {
			if (StringUtil.equals(farmControlUnit.getSystemCode(),
					terminal.getControlType())
					&& !terminal.getPreBoot()) {
				if (!functionCodeJsonObject.containsKey(terminal
						.getFunctionCode())) {
					functionCodeJsonObject.put(terminal.getFunctionCode(),
							terminal.getFunctionName());
					JSONArray array = new JSONArray();
					array.add(terminal.getTerminalNo());
					terminalJsonObject.put(terminal.getFunctionCode(), array);
				} else {
					terminalJsonObject.getJSONArray(terminal.getFunctionCode())
							.add(terminal.getTerminalNo());
				}
			}
		}
		for (String functionCode : functionCodeJsonObject.keySet()) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(controlTerminalKeys[0], functionCode);
			jsonObject.put(controlTerminalKeys[1],
					functionCodeJsonObject.get(functionCode));
			jsonObject.put(controlTerminalKeys[2],
					terminalJsonObject.getJSONArray(functionCode));
			jsonArray.add(jsonObject);
		}
		resultJsonObject.put("operations", jsonArray);
		resultJsonObject.put("isEnableMultiFunction", BaseIfarmUtil
				.isEnableMultiFunction(farmControlUnit.getSystemType()));
		return jsonArray.toString();
	}

	public String saveFarmControlTerminal(
			FarmControlTerminal farmControlTerminal) {
		FarmControlDevice farmControlDevice = farmControlDeviceDao.getTById(
				farmControlTerminal.getControlDeviceId(),
				FarmControlDevice.class);
		if (farmControlDevice == null) {
			return SystemResultEncapsulation.fillResultCode(
					SystemReturnCodeEnum.PARAM_ERROR, "device no exist");
		}
		FarmControlUnit farmControlUnit = farmControlUnitDao.getTById(
				farmControlTerminal.getUnitId(), FarmControlUnit.class);
		if (farmControlUnit == null) {
			return SystemResultEncapsulation.fillResultCode(
					SystemReturnCodeEnum.PARAM_ERROR, "unit no exist");
		}
		if (BaseIfarmUtil.isPreBootFunctionCode(farmControlTerminal
				.getFunctionCode())) {
			farmControlTerminal.setPreBoot(true);
		} else {
			farmControlTerminal.setPreBoot(false);
		}
		super.validator(farmControlTerminal);
		FarmControlTerminal contorlDeviceTerminal = new FarmControlTerminal(
				farmControlTerminal.getControlDeviceId(),
				farmControlTerminal.getControlDeviceBit());
		List controlTermList = farmControlTerminalDao
				.getDynamicList(contorlDeviceTerminal);
		if (controlTermList.size() > 0) {
			return SystemResultEncapsulation.fillResultCode(
					SystemReturnCodeEnum.UNIQUE_ERROR, null,
					"exist same deviceId and bit");
		}
		if (BaseIfarmUtil.isPreBoot(farmControlUnit.getSystemType())) {
			FarmControlTerminal identifyingTerminal = new FarmControlTerminal(
					farmControlTerminal.getUnitId(),
					farmControlTerminal.getTerminalCode(),
					farmControlTerminal.getTerminalNo());
			List identifyingTerminalList = farmControlTerminalDao
					.getDynamicList(identifyingTerminal);
			if (identifyingTerminalList.size() > 0) {
				return SystemResultEncapsulation.fillResultCode(
						SystemReturnCodeEnum.UNIQUE_ERROR, null,
						"exist same unit and terminal");
			}
		}
		return super.baseSave(farmControlTerminal);

	}

	public String getFarmControlTerminals(
			FarmControlTerminal farmControlTerminal) {
		JSONArray jsonArray = new JSONArray();
		List<FarmControlTerminal> controlTerminalrms = farmControlTerminalDao
				.getDynamicList(farmControlTerminal);
		for (int i = 0; i < controlTerminalrms.size(); i++) {
			FarmControlTerminal fControlTerminal = controlTerminalrms.get(i);
			JSONObject jsonObject = JsonObjectUtil
					.toJsonObject(fControlTerminal);
			jsonObject.put(
					"controlName",
					CacheDataBase.initBaseConfig.get("controlSystemType.json")
							.getJSONObject("systemName")
							.getString(fControlTerminal.getControlType()));
			jsonArray.add(jsonObject);
		}
		return jsonArray.toJSONString();
	}

	public String buildHandCommand(FarmControlTerminal farmControlTerminal) {
		List<FarmControlTerminal> farmControlTerminals = farmControlTerminalDao
				.getDynamicList(farmControlTerminal);
		for (int i = 0; i < farmControlTerminals.size(); i++) {

		}
		return null;
	}
}
