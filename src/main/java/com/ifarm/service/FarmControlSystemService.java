package com.ifarm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.ControlSystem;
import com.ifarm.bean.ControlTask;
import com.ifarm.bean.FarmControlDevice;
import com.ifarm.bean.FarmControlSystem;
import com.ifarm.bean.FarmControlUnit;
import com.ifarm.bean.MultiControlCommand;
import com.ifarm.bean.MultiControlTask;
import com.ifarm.constant.IfarmConfigConstant;
import com.ifarm.constant.SystemConfigCache;
import com.ifarm.constant.SystemResultCodeEnum;
import com.ifarm.dao.ControlSystemDao;
import com.ifarm.dao.FarmControlDeviceDao;
import com.ifarm.dao.FarmControlSystemDao;
import com.ifarm.dao.FarmControlTerminalDao;
import com.ifarm.dao.FarmControlUnitDao;
import com.ifarm.enums.ControlSystemEnum;
import com.ifarm.enums.ServiceHeadEnum;
import com.ifarm.enums.SystemControlStatusEnum;
import com.ifarm.util.BaseIfarmUtil;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.ControlStrategyUtil;
import com.ifarm.util.JsonObjectUtil;
import com.ifarm.util.StringUtil;
import com.ifarm.util.SystemResultEncapsulation;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FarmControlSystemService {
	@Autowired
	private FarmControlSystemDao farmControlSystemDao;

	@Autowired
	private FarmControlDeviceDao farmControlDeviceDao;

	@Autowired
	private ControlSystemDao controlSystemDao;

	@Autowired
	private FarmControlUnitDao farmControlUnitDao;

	@Autowired
	private FarmControlTerminalDao farmControlTerminalDao;

	private static final Logger farmControlSystemService_log = LoggerFactory
			.getLogger(FarmControlSystemService.class);

	String[] controlSystemKeys = { "systemId", "farmId", "farmName",
			"systemCode", "systemType", "systemTypeCode", "systemDistrict",
			"systemNo", "systemDescription", "systemLocation",
			"systemCreateTime" };

	String[] wfmControlSystemKeys = { "systemId", "farmId", "farmName",
			"systemCode", "systemType", "systemTypeCode", "systemDistrict",
			"systemNo", "systemDescription", "systemLocation",
			"systemCreateTime", "medicineNum", "districtNum", "fertierNum" };

	public JSONArray farmControlRegion(Integer farmId) {
		JSONArray array = JSONArray.parseArray(JSON
				.toJSONString(farmControlSystemDao.farmControlRegion(farmId)));
		return array;
	}

	public boolean farmControlSystemVerification(String userId, Integer farmId,
			Integer systemId) {
		return farmControlSystemDao.farmControlSystemVerification(userId,
				farmId, systemId);
	}

	public String updateControlSystemState(FarmControlSystem farmControlSystem) {
		if (farmControlSystemDao.updateDynamic(farmControlSystem)) {
			return SystemResultEncapsulation
					.resultCodeDecorate(SystemResultCodeEnum.SUCCESS);
		}
		return SystemResultEncapsulation
				.resultCodeDecorate(SystemResultCodeEnum.ERROR);
	}

	public String saveControlSystem(FarmControlSystem farmControlSystem) {
		Integer farmId = farmControlSystem.getFarmId();
		if (farmId == null) {
			return SystemResultEncapsulation
					.resultCodeDecorate(SystemResultCodeEnum.NO_ID);
		}
		try {
			JSONObject jsonObject = JsonObjectUtil.fromBean(farmControlSystem);
			Integer systemId = farmControlSystemDao
					.saveFarmControlSystem(farmControlSystem);
			jsonObject.put("systemId", systemId);
			// CacheDataBase.controlSystemValueMap.put(systemId, jsonObject);
			return SystemResultEncapsulation
					.resultCodeDecorate(SystemResultCodeEnum.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			farmControlSystemService_log.error(e.getMessage());
			farmControlSystemService_log.error("添加控制系统", e);
			return SystemResultEncapsulation
					.resultCodeDecorate(SystemResultCodeEnum.ERROR);
		}
	}

	public String farmControlSystemsDynamicList(
			FarmControlSystem fControlSystem, String userId) {
		List list = farmControlSystemDao.farmControlSystemState(userId,
				fControlSystem.getSystemCode());
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			Object[] objects = (Object[]) list.get(i);
			JSONObject jsonObject = new JSONObject();
			for (int j = 0; j < objects.length; j++) {
				if (objects[j] != null) {
					jsonObject.put(controlSystemKeys[j], objects[j].toString());
				}
			}
			array.add(jsonObject);
		}
		return array.toString();
	}

	/**
	 * 水肥药一体系统
	 * 
	 * @param userId
	 * @return
	 */
	public String farmWFMControlSystemsDynamicList(String userId) {
		List list = farmControlSystemDao.farmWFMControlSystemState(userId);
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			Object[] objects = (Object[]) list.get(i);
			JSONObject jsonObject = new JSONObject();
			for (int j = 0; j < objects.length; j++) {
				if (objects[j] != null) {
					jsonObject.put(wfmControlSystemKeys[j],
							objects[j].toString());
				}
			}
			array.add(jsonObject);
		}
		return array.toString();
	}

	/**
	 * 根据控制任务信息，去找到对应的设备信息，根据具体的业务逻辑生成相应的bits数组
	 * 
	 * @param controlTask
	 * @throws Exception
	 */

	public void produceControlTaskCommand(ControlTask controlTask)
			throws Exception {

		List<Object[]> list = farmControlSystemDao
				.farmControlUnitToTerminal(controlTask.getUnitId());
		// 目前假定都是一个设备，后期如果有多个设备，必然涉及更复杂的业务
		if (list != null && list.size() > 0) {
			Integer controlDeviceId = (Integer) list.get(0)[1];
			controlTask.setControlDeviceId(controlDeviceId); // 目前只是简单操作，后期可能涉及一个控制系统有多个设备关联
			controlTask.setCollectorId(farmControlDeviceDao.getTById(
					controlDeviceId, FarmControlDevice.class).getCollectorId()); // 当然不同设备有可能来源于不同集中器，所以很坑
		} else {
			return;
		}
		ControlStrategyUtil.controlTaskOperation(controlTask, list);
	}

	public void controlTerminalToControlDevice(
			Map<Integer, ArrayList<Object[]>> map, List<Object[]> list,
			ArrayList<Integer> deviceList) {
		for (int i = 0; i < list.size(); i++) {
			Object[] objects = list.get(i);
			Integer deviceId = (Integer) objects[0];
			if (map.containsKey(deviceId)) {
				map.get(deviceId).add(objects);
			} else {
				ArrayList<Object[]> arrayList = new ArrayList<Object[]>();
				arrayList.add(objects);
				map.put(deviceId, arrayList);
				deviceList.add(deviceId);
			}
		}
	}

	/**
	 * 配置所有的设备命令
	 * 
	 * @param controlTask
	 */
	public void produceMultiControlTaskCommand(MultiControlTask controlTask) {
		Integer unitId = controlTask.getUnitId();
		String controlType = controlTask.getControlOperation(); // irrigate,fertilizer,medicine
		Map<Integer, ArrayList<Object[]>> map = new HashMap<Integer, ArrayList<Object[]>>();
		ArrayList<Integer> controlDevicesArrayList = new ArrayList<>();
		if ((boolean) CacheDataBase.systemConfigCacheMap
				.get(SystemConfigCache.MULTIPLE_CONTROL_SYSTEM)) {
			String unitNodeInfo = controlTask.getUnitNodeInfo();
			JSONObject unitNodeJson = JSONObject.parseObject(unitNodeInfo);
			ControlSystemEnum controlSystemEnum = ControlSystemEnum
					.getValueByType(controlType);
			if (BaseIfarmUtil.isPreBoot(controlType)) {
				JSONArray preBootArray = ((JSONObject) CacheDataBase.farmConfigCacheMap
						.get(IfarmConfigConstant.CONTROL_PRE_BOOT))
						.getJSONArray(controlSystemEnum.getCode());
				if (preBootArray != null) {
					for (int i = 0; i < preBootArray.size(); i++) {
						String preBootCode = preBootArray.getString(i);
						List<Object[]> terminalList = null;
						if (StringUtil.equals(preBootCode, "pumpEnable")) {
							terminalList = farmControlSystemDao
									.farmMultiControlTerminalByUnit(unitId,
											preBootCode, 1);
						} else if (StringUtil.equals(preBootCode, "canEnable")) {
							terminalList = farmControlSystemDao
									.farmMultiControlTerminalByUnit(unitId,
											preBootCode, Integer
													.valueOf(controlTask
															.getCanNo()));
						} else {
							continue;
						}
						controlTerminalToControlDevice(map, terminalList,
								controlDevicesArrayList);
					}
				}
			}
			for (String functionCode : unitNodeJson.keySet()) {
				String[] terminalNoArr = unitNodeJson.getString(functionCode)
						.split(",");
				for (String terminalNo : terminalNoArr) {
					List<Object[]> terminalList = farmControlSystemDao
							.farmMultiControlTerminalByUnit(unitId,
									functionCode, Integer.valueOf(terminalNo));
					controlTerminalToControlDevice(map, terminalList,
							controlDevicesArrayList);
				}
			}
			sortAndBuildCommand(controlDevicesArrayList, map, controlTask);
			return;
		}
		String controlArea = controlTask.getControlArea();
		String canNo = controlTask.getCanNo();
		// 控制区域阀门先打开
		if (controlArea != null) {
			String[] controlAreas = controlArea.split(",");
			for (int i = 0; i < controlAreas.length; i++) {
				List<Object[]> districtList = farmControlSystemDao
						.farmMultiControlUnitToTerminal(unitId, "district"
								+ controlAreas[i]);
				controlTerminalToControlDevice(map, districtList,
						controlDevicesArrayList);
			}
		}
		// 罐的操作
		if ("irrigate".equals(controlType)) {

		} else if ("fertilizer".equals(controlType)) {
			if (canNo != null) {
				String[] cans = canNo.split(",");
				for (int i = 0; i < cans.length; i++) {
					List<Object[]> list = farmControlSystemDao
							.farmMultiControlUnitToTerminal(unitId,
									"fertilizerCan" + cans[i]);
					controlTerminalToControlDevice(map, list,
							controlDevicesArrayList);
				}
			}
		} else if ("medicine".equals(controlType)) {
			if (canNo != null) {
				String[] cans = canNo.split(",");
				for (int i = 0; i < cans.length; i++) {
					List<Object[]> list = farmControlSystemDao
							.farmMultiControlUnitToTerminal(unitId,
									"medicineCan" + cans[i]);
					controlTerminalToControlDevice(map, list,
							controlDevicesArrayList);
				}
			}
		}
		// 控制水泵的设备
		List<Object[]> pumpList = farmControlSystemDao
				.farmMultiControlUnitToTerminal(unitId, "pump");
		controlTerminalToControlDevice(map, pumpList, controlDevicesArrayList);
		sortAndBuildCommand(controlDevicesArrayList, map, controlTask);
	}

	public void sortAndBuildCommand(ArrayList<Integer> controlDevicesArrayList,
			Map<Integer, ArrayList<Object[]>> map, MultiControlTask controlTask) {
		for (int i = 0; i < controlDevicesArrayList.size(); i++) {
			Integer deviceId = controlDevicesArrayList.get(i);
			FarmControlDevice farmControlDevice = farmControlDeviceDao
					.getTById(deviceId, FarmControlDevice.class);
			if (farmControlDevice == null) {
				farmControlSystemService_log.error(deviceId
						+ "---database no exist");
				continue;
			}
			Long collectorId = farmControlDevice.getCollectorId();
			ArrayList<Object[]> arrayList = map.get(deviceId);
			int[] forwardbits = new int[32];
			HashSet<String> hashSet = new HashSet<>();
			for (int j = 0; j < arrayList.size(); j++) {
				Object[] objects = arrayList.get(j);
				if ("enable".equals((String) objects[2])) {
					forwardbits[(int) objects[1]] = 1;
				}
				hashSet.add((String) objects[3]);
			}
			MultiControlCommand command = new MultiControlCommand(controlTask,
					"execution", deviceId, collectorId);
			command.setControlTerminalbits(forwardbits);
			command.setIndentifying(hashSet.toString());
			controlTask.getWfmControlCommands().add(command);
		}
	}

	public String authorityLimit(String userId, Integer checkNum) {
		if (userId.length() < 11) {
			return "error";
		}
		int sum = Integer.parseInt(userId.substring(0, 6))
				^ Integer.parseInt(userId.substring(6));
		if (checkNum == sum) {
			return "success";
		}
		return "error";
	}

	public FarmControlSystem getFarmControlSystemById(Integer systemId) {
		return farmControlSystemDao.getTById(systemId, FarmControlSystem.class);
	}

	public String deleteFarmControlSystem(FarmControlSystem farmControlSystem) {
		try {
			farmControlSystemDao.deleteBase(farmControlSystem);
			return SystemResultEncapsulation
					.resultCodeDecorate(SystemResultCodeEnum.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			farmControlSystemService_log.error(
					JSON.toJSONString(farmControlSystem) + "-delete error", e);
			return SystemResultEncapsulation
					.resultCodeDecorate(SystemResultCodeEnum.ERROR);
		}
	}

	public String queryFarmControlSystem(FarmControlSystem farmControlSystem) {
		return JsonObjectUtil.toJsonArrayString(farmControlSystemDao
				.getDynamicListAddLike(farmControlSystem));
	}

	public String controlSystemRegion(String farmId) {
		JSONObject jsonObject = new JSONObject();
		List list = farmControlSystemDao.controlSystemRegion(farmId);
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

	public String regionControlSystem(String farmId, String systemDistrict,
			String systemLocation) {
		List list = farmControlSystemDao.regionControlSystem(farmId,
				systemDistrict, systemLocation);
		return JsonObjectUtil.buildCommandJsonObject(list,
				ServiceHeadEnum.REGION_CONTROL_SYSTEM).toJSONString();
	}

	public String openControlSystem(Integer farmId, String systemCode) {
		ControlSystemEnum controlSystemEnum = ControlSystemEnum
				.getValueByCode(systemCode);
		if (controlSystemEnum != null) {
			ControlSystem controlSystem = new ControlSystem();
			controlSystem.setFarmId(farmId);
			controlSystem.setSystemCode(controlSystemEnum.getCode());
			List<ControlSystem> list = controlSystemDao
					.getDynamicList(controlSystem);
			if (list.size() > 0) {
				return SystemResultEncapsulation
						.resultCodeDecorate(SystemResultCodeEnum.ID_EXIST);
			}
			controlSystem.setSystemType(controlSystemEnum.getShortName());
			controlSystem.setSystemTypeCode(controlSystemEnum.getType());
			controlSystem.setSystemStatus(SystemControlStatusEnum.ENABLE
					.getCode());
			controlSystemDao.saveBase(controlSystem);
			return SystemResultEncapsulation
					.resultCodeDecorate(SystemResultCodeEnum.SUCCESS);
		}
		return SystemResultEncapsulation
				.resultCodeDecorate(SystemResultCodeEnum.ERROR);
	}

	public String queryControlSystem(ControlSystem controlSystem) {
		return JsonObjectUtil.toJsonArrayString(controlSystemDao
				.getDynamicList(controlSystem));
	}

	public String farmControlSystemTerimal(FarmControlUnit farmControlUnit) {
		FarmControlUnit controlUnit = farmControlUnitDao.getTById(
				farmControlUnit.getUnitId(), FarmControlUnit.class);
		String controlType = controlUnit.getSystemType();
		JSONArray terminalIdentifyingArray = CacheDataBase.initBaseConfig
				.get("terminalType.json").getJSONObject("terminalIdentifying")
				.getJSONArray(controlType);
		return terminalIdentifyingArray.toString();
	}

}