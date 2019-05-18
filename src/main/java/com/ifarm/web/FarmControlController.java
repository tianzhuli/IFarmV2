package com.ifarm.web;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.ControlTask;
import com.ifarm.bean.FarmControlSystem;
import com.ifarm.bean.FarmControlTerminal;
import com.ifarm.bean.MultiControlTask;
import com.ifarm.constant.ControlTaskEnum;
import com.ifarm.enums.ControlSystemEnum;
import com.ifarm.enums.SystemReturnCodeEnum;
import com.ifarm.nosql.service.CombinationControlTaskService;
import com.ifarm.redis.util.ControlTaskRedisHelper;
import com.ifarm.redis.util.UserRedisUtil;
import com.ifarm.redis.util.WfmControlTaskRedisHelper;
import com.ifarm.service.FarmControlSystemService;
import com.ifarm.service.FarmControlTerminalService;
import com.ifarm.util.BaseIfarmUtil;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.ControlHandlerUtil;
import com.ifarm.util.ControlTaskUtil;
import com.ifarm.util.EventContext;
import com.ifarm.util.SystemResultEncapsulation;

@RequestMapping("farmControl")
@RestController
public class FarmControlController {
	@Autowired
	private FarmControlSystemService farmControlService;

	@Autowired
	private FarmControlTerminalService farmControlTerminalService;

	@Autowired
	private CombinationControlTaskService combinationControlTaskService;

	@Autowired
	private ControlTaskRedisHelper controlTaskRedisHelper;

	@Autowired
	private WfmControlTaskRedisHelper wfmControlTaskRedisHelper;

	@Autowired
	private UserRedisUtil userRedisUtil;

	@RequestMapping("controlSystemList")
	@CrossOrigin
	public String farmControlSystemsDynamicList(
			FarmControlSystem farmControlSystem, String userId) {
		return farmControlService.farmControlSystemsDynamicList(
				farmControlSystem, userId);
	}

	@RequestMapping("wfmControlSystemList")
	@CrossOrigin
	public String wfmControlSystemList(String userId) {
		return farmControlService.farmWFMControlSystemsDynamicList(userId);
	}

	@RequestMapping("controlRegion")
	@CrossOrigin
	public String controlRegion(Integer farmId) {
		return farmControlService.farmControlRegion(farmId).toString();
	}

	@RequestMapping("updateControlSystem")
	public String updateControlSystem(FarmControlSystem farmControlSystem) {
		return farmControlService.updateControlSystemState(farmControlSystem);
	}

	@RequestMapping("authorityLimit")
	public String authorityLimit(@RequestParam("userId") String userId,
			@RequestParam("checkNum") Integer checkNum) {
		return farmControlService.authorityLimit(userId, checkNum);
	}

	@RequestMapping("combinationControlHistory")
	@CrossOrigin
	public String combinationControlHistory(String userId) {
		return combinationControlTaskService
				.queryCombinationControlTask(userId);
	}

	@RequestMapping("farmControlTaskStrategy")
	@CrossOrigin
	public String farmControlTaskStrategy(String startExecuteTime,
			ControlTask controlTask, String command,
			HttpServletRequest httpRequest) throws InterruptedException {
		String userId = controlTask.getUserId();
		JSONObject resultJson = new JSONObject();
		if ("queryTasks".equals(command)) {
			JSONArray array = new JSONArray();
			List<ControlTask> list = controlTaskRedisHelper
					.getRedisListValues(userId);
			ControlTaskUtil.queryTasks(list, array, controlTask);
			List<MultiControlTask> wfmControlTasks = wfmControlTaskRedisHelper
					.getRedisListValues(userId);
			ControlTaskUtil.queryWfmTasks(wfmControlTasks, array, controlTask);
			resultJson.put("response", array);
			return resultJson.toString();
		} else if ("queryExecutingTasks".equals(command)) {
			JSONArray array = new JSONArray();
			ControlTaskUtil.queryExecutingTasks(
					controlTaskRedisHelper.getRedisListValues(userId), array,
					controlTask);
			ControlTaskUtil.queryWfmExecutingTasks(
					wfmControlTaskRedisHelper.getRedisListValues(userId),
					array, controlTask);
			resultJson.put("response", array);
			return resultJson.toString();
		} else if ("queryMessageCache".equals(command)) {
			resultJson.put("response",
					userRedisUtil.getUserControlResultMessageCache(userId));
			return resultJson.toString();
		} else if ("delete".equals(command)) {
			String result = ControlHandlerUtil.revolationTask(userId,
					controlTask.getControllerLogId(),
					controlTask.getControlType());
			return result;
		} else if ("deleteAll".equals(command)) {

		} else if ("execute".equals(command)) {
			if (startExecuteTime != null) {
				controlTask.setStartExecutionTime(Timestamp
						.valueOf(startExecuteTime));
			}
			try {
				String controlType = controlTask.getControlType();
				EventContext eventContext = new EventContext();
				if (BaseIfarmUtil.isSwithMultiControl()) {
					JSONArray multiArray = CacheDataBase.initBaseConfig.get(
							"controlSystemType.json").getJSONArray(
							"swithMultiControlTask");
					if (multiArray.contains(controlType)) {
						MultiControlTask mControlTask = ControlTaskUtil
								.fromControlTask(controlTask, httpRequest);
						mControlTask.setUserId(userId);
						ControlHandlerUtil.mHandlerControlMessage(mControlTask,
								userId, farmControlService, eventContext);
					} else {
						controlTask.setUserId(userId);
						ControlHandlerUtil.handlerControlMessage(controlTask,
								userId, farmControlService, eventContext);
					}
				} else {
					if (ControlSystemEnum.WATER_FERTILIZER_MEDICINDE
							.equals(ControlSystemEnum
									.getValueByType(controlType))) {
						MultiControlTask wfmControlTask = ControlTaskUtil
								.fromControlTask(controlTask, httpRequest);
						wfmControlTask.setUserId(userId);
						ControlHandlerUtil.mHandlerControlMessage(
								wfmControlTask, userId, farmControlService,
								eventContext);
					} else {
						controlTask.setUserId(userId);
						ControlHandlerUtil.handlerControlMessage(controlTask,
								userId, farmControlService, eventContext);
					}
				}
				if (eventContext.getEvent() != null) {
					return (String) eventContext.getEvent();
				}
			} catch (Exception e) {
				// TODO: handle exception
				return SystemResultEncapsulation.fillErrorCode(e);
			}
			return SystemResultEncapsulation
					.fillResultCode(SystemReturnCodeEnum.SUCCESS);
		} else if ("userConfirmation".equals(command)) {
			resultJson.put("response", ControlHandlerUtil
					.userConfirmationUpdate(userId,
							controlTask.getControllerLogId(),
							controlTask.getControlType(),
							controlTask.getUserConfirmation()));
			return resultJson.toString();
		}
		resultJson.put("response", ControlTaskEnum.NO_MESSAGE);
		return resultJson.toString();
	}

	@RequestMapping("farmControlTaskStrategyBatch")
	@CrossOrigin
	public String farmControlTaskStrategyBatch(String controlTasks,
			String command) throws Exception {
		JSONArray taskArray = JSONArray.parseArray(controlTasks);
		for (int i = 0; i < taskArray.size(); i++) {
			JSONObject messageJson = taskArray.getJSONObject(i);
			String controlType = messageJson.getString("controlType");
			ControlSystemEnum systemEnum = ControlSystemEnum
					.getValueByType(controlType);
			if (systemEnum == null) {
				throw new Exception("controlType no exist");
			}
			if (ControlSystemEnum.WATER_FERTILIZER_MEDICINDE
					.equals(controlType)) {
				MultiControlTask wfmControlTask = ControlTaskUtil
						.fromJson(messageJson);
				ControlHandlerUtil.mHandlerControlMessage(wfmControlTask,
						wfmControlTask.getUserId(), farmControlService,
						new EventContext());
			} else {
				ControlTask controlTask = ControlTaskUtil
						.fromJsonTotask(messageJson);
				ControlHandlerUtil.handlerControlMessage(controlTask,
						controlTask.getUserId(), farmControlService,
						new EventContext());
			}
		}
		JSONObject resultJson = new JSONObject();
		resultJson.put("response", ControlTaskEnum.RUNNING);
		return resultJson.toJSONString();
	}

	@RequestMapping("farmControlOperationList")
	@CrossOrigin
	public String getFarmControlOperationList(
			FarmControlTerminal farmControlTerminal) {
		return farmControlTerminalService
				.getFarmControlOperationList(farmControlTerminal);
	}

}
