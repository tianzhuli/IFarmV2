package com.ifarm.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.ControlCommand;
import com.ifarm.bean.ControlTask;
import com.ifarm.bean.WFMControlCommand;
import com.ifarm.bean.WFMControlTask;
import com.ifarm.constant.ControlTaskEnum;
import com.ifarm.service.FarmControlSystemService;

public class ControlHandlerUtil {
	private static final Logger controlHandlerUtil_log = LoggerFactory.getLogger(ControlHandlerUtil.class);
	private static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<SimpleDateFormat>();
	private static int waitTime = 30; // 每次任务之间的冷却时间，保证时间误差可用，之前是2min,现改成1min

	public static SimpleDateFormat getSimpleDateFormat(String pattern) {
		SimpleDateFormat sdf = null;
		sdf = tl.get();
		if (sdf == null) {
			sdf = new SimpleDateFormat(pattern);
			tl.set(sdf);
		}
		return sdf;
	}

	/**
	 * 设备回复的消息
	 * 
	 * @param command
	 * @param flag
	 * @return
	 */
	public static void controlDeviceReturnMessage(ControlCommand command, boolean flag) {
		if (command instanceof WFMControlCommand) {
			wfmControlDeviceReturnMessage((WFMControlCommand) command, flag);
			return;
		}
		String commandCategory = command.getCommandCategory();
		ControlTask controlTask = command.getControlTask();
		if ("execution".equals(commandCategory)) {
			controlTask.setAddReceived(true);
			controlTask.setAddResultTime(System.currentTimeMillis() / 1000);
			if (flag) {
				controlTask.setAddResult(ControlTaskEnum.EXEUTION_SUCCESS);
				controlTask.setTaskState(ControlTaskEnum.EXECUTING);
				controlTask.setResponseMessage(ControlTaskEnum.EXECUTION_SUCCESS_RESPONSE);
				CacheDataBase.userControlData.notifyObservers(controlTask.getUserId(), controlTask.pushUserMessage());// 通知用户
			} else {
				// 多个设备开始失败会影响其他设备
				controlTask.setAddResult(ControlTaskEnum.EXECUTION_FAIL);
			}
		} else if ("stop".equals(commandCategory)) {
			controlTask.setStopReceived(true);
			controlTask.setStopTime(System.currentTimeMillis() / 1000);
			if (flag) {
				controlTask.setStopResult(ControlTaskEnum.STOP_SUCCESS);
				controlTask.setTaskState(ControlTaskEnum.STOPPED);
				CacheDataBase.userControlData.notifyObservers(controlTask.getUserId(), controlTask.pushUserMessage());// 通知用户
			} else {
				// 多个设备停止失败会影响其他设备
				controlTask.setStopResult(ControlTaskEnum.STOP_FAIL);
			}
		}
		CacheDataBase.taskService.updateControlTask(controlTask);
	}

	/**
	 * 撤销某次任务
	 * 
	 * @param collectorId
	 * @param controlId
	 * @return
	 */
	public static String revolationTask(String userId, Integer controlId, String controlType) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("response", ControlTaskEnum.NO_TASK);
		try {
			controlHandlerUtil_log.info("撤销的任务：" + controlId);
			if ("wfm".equals(controlType)) {
				if (CacheDataBase.wfmControlTaskRedisHelper.removeWfmControlTask(userId, controlId)) {
					CacheDataBase.wTaskService.deleteControlTask(controlId);
					jsonObject.put("response", ControlTaskEnum.SUCCESS);
				}
			} else {
				if (CacheDataBase.controlTaskRedisHelper.removeControlTask(userId, controlId)) {
					CacheDataBase.taskService.deleteControlTask(controlId);
					jsonObject.put("response", ControlTaskEnum.SUCCESS);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			controlHandlerUtil_log.error("revolationTask error",e);
			jsonObject.put("response", ControlTaskEnum.ERROR);
			return jsonObject.toString();
		}
		return jsonObject.toString();
	}

	/**
	 * 
	 * @param controlTask
	 * @param flat
	 *            区分于定时添加和Cache线程的不同
	 * @return
	 */
	public static boolean judgeControlTaskConflict(ControlTask controlTask, boolean flat) {
		String userId = controlTask.getUserId();
		List<ControlTask> controlTasks = CacheDataBase.controlTaskRedisHelper.getRedisListValues(userId);
		if (controlTasks.size() > 0) {
			Iterator<ControlTask> iterator = controlTasks.iterator();
			while (iterator.hasNext()) {
				ControlTask task = iterator.next();
				Integer systemId = controlTask.getSystemId();
				Integer deviceId = controlTask.getControlDeviceId();
				if (flat) {
					if (task.equals(controlTask)) {
						continue;
					}
				}
				if ((systemId != null && systemId.equals(task.getSystemId())) || (deviceId != null && deviceId.equals(task.getControlDeviceId()))) {
					if (flat) {
						if (ControlTaskEnum.EXECUTING.equals(task.getTaskState()) || ControlTaskEnum.BLOCKING.equals(task.getTaskState())) {
							return true;
						}
					}
					long originalTime = task.getStartExecutionTime().getTime() / 1000;
					long newestTime = controlTask.getStartExecutionTime().getTime() / 1000;
					boolean compare = (newestTime > (originalTime + task.getExecutionTime() + waitTime))
							| ((newestTime + controlTask.getExecutionTime() + waitTime) < originalTime);
					if (!compare) { // 时间冲突
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 用户操作指令类别应该有三种，ImmediateExecution,fixedTimeExecution,handStop
	 * 
	 * @param controlTask
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static JSONObject handlerControlMessage(ControlTask controlTask, String userId, FarmControlSystemService farmControlSystemService)
			throws Exception {
		JSONObject json = new JSONObject();
		json.put("response", ControlTaskEnum.ERROR);
		String commandCategory = controlTask.getCommandCategory();
		Object lock = CacheDataBase.controlTaskRedisHelper.getLock(userId);
		List<ControlTask> controlTasks = CacheDataBase.controlTaskRedisHelper.getRedisListValues(userId);
		if ("ImmediateExecution".equals(commandCategory)) {
			Timestamp timestamp = Timestamp.valueOf(getSimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			controlTask.setTaskTime(timestamp);
			controlTask.setStartExecutionTime(timestamp); // 立马执行
			controlTask.setLevel(2);
			controlTask.setTaskState(ControlTaskEnum.BLOCKING); // 任务下发不代表已经开始运行
			farmControlSystemService.produceControlTaskCommand(controlTask); // 更新deviceId和collectorId等生成bits数组
			if (controlTasks.size() > 0) {
				Iterator<ControlTask> iterator = controlTasks.iterator();
				while (iterator.hasNext()) {
					ControlTask task = iterator.next();
					Integer systemId = controlTask.getSystemId();
					Integer deviceId = controlTask.getControlDeviceId();
					if (systemId != null && systemId.equals(task.getSystemId()) || (deviceId != null && deviceId.equals(task.getControlDeviceId()))) {
						if (ControlTaskEnum.EXECUTING.equals(task.getTaskState()) || ControlTaskEnum.BLOCKING.equals(task.getTaskState())) {
							json.put("response", ControlTaskEnum.CONFLICT_CURRENT);
							return json;
						} else if (ControlTaskEnum.WAITTING.equals(task.getTaskState())) {
							long originalTime = task.getStartExecutionTime().getTime() / 1000;
							long newestTime = controlTask.getStartExecutionTime().getTime() / 1000;
							boolean compare = (newestTime > (originalTime + task.getExecutionTime() + waitTime))
									| ((newestTime + controlTask.getExecutionTime() + waitTime) < originalTime);
							if (!compare) { // 时间冲突
								json.put("response", ControlTaskEnum.CONFLICT);
								return json;
							}
						}
					}
				}
			}
			CacheDataBase.taskService.saveControlTask(controlTask);
			synchronized (lock) {
				CacheDataBase.controlTaskRedisHelper.addRedisListValue(userId, controlTask);
			}
			json.put("response", ControlTaskEnum.RUNNING);
			ControlCommand command = controlTask.buildCommand("execution"); // 将命令添加到缓存
			Long collectorId = controlTask.getCollectorId();
			if (collectorId != null) {
				CacheDataBase.commandRedisHelper.setCommandRedisListValue(collectorId.toString(), command);
			}
			CacheDataBase.ioControlData.notifyObservers(collectorId); // 推送到长连接设备
		} else if ("fixedTimeExecution".equals(commandCategory)) { // 暂时还未考虑时间冲突
			if (controlTask.getStartExecutionTime() == null) {
				json.put("response", ControlTaskEnum.ERROR);
				return json;
			}
			controlTask.setLevel(1);
			controlTask.setTaskTime(Timestamp.valueOf(getSimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
			controlTask.setTaskState(ControlTaskEnum.WAITTING);
			farmControlSystemService.produceControlTaskCommand(controlTask); // 更新deviceId和collectorId等生成bits数组
			if (judgeControlTaskConflict(controlTask, false)) {
				json.put("response", ControlTaskEnum.CONFLICT);
				return json;
			}
			CacheDataBase.taskService.saveControlTask(controlTask);
			synchronized (lock) {
				CacheDataBase.controlTaskRedisHelper.addRedisListValue(userId, controlTask);
			}
		} else if ("handStop".equals(commandCategory)) {
			json.put("response", ControlTaskEnum.NO_TASK);
			controlTask = CacheDataBase.controlTaskRedisHelper.queryControlTask(userId, controlTask);
			controlTask.setLevel(4);
			controlTask.setStartStopTime(System.currentTimeMillis() / 1000);
			ControlCommand command = controlTask.buildCommand("stop");
			Long collectorId = controlTask.getCollectorId();
			if (collectorId != null) {
				CacheDataBase.commandRedisHelper.setCommandRedisListValue(collectorId.toString(), command);
				controlTask.setTaskState(ControlTaskEnum.STOPPING);
				CacheDataBase.ioControlData.notifyObservers(collectorId); // 推送到长连接设备
				json.put("response", ControlTaskEnum.RUNNING);
				CacheDataBase.controlTaskRedisHelper.updateControlTaskListValu(userId, controlTask);
			}
		} else if ("query".equals(commandCategory)) {
			json.put("response", ControlTaskEnum.NO_TASK);
			Integer controllerLogId = controlTask.getControllerLogId();
			if (controlTasks.size() > 0) {
				Iterator<ControlTask> iterator = controlTasks.iterator();
				while (iterator.hasNext()) {
					ControlTask cTask = iterator.next();
					if (controllerLogId != null && controllerLogId.equals(cTask.getControllerLogId())) {
						json.put("response", cTask.buildQueryResult());
						return json;
					}
				}
			} else {
				controlHandlerUtil_log.error("该用户无任务:" + userId);
			}
		}
		return json;
	}

	/**
	 * 失效的指令需要用户确认
	 * 
	 * @param userId
	 * @param controlId
	 * @param controlType
	 * @return
	 */
	public static String userConfirmationUpdate(String userId, Integer controlId, String controlType, String userConfirmation) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("response", ControlTaskEnum.NO_TASK);
		try {
			controlHandlerUtil_log.info("撤销的任务：" + controlId);
			if ("wfm".equals(controlType)) {
				List<WFMControlTask> wfmControlTasks = CacheDataBase.wfmControlTaskRedisHelper.getRedisListValues(userId);
				for (int i = 0; i < wfmControlTasks.size(); i++) {
					WFMControlTask task = wfmControlTasks.get(i);
					if (task.getControllerLogId().equals(controlId)) {
						task.setUserConfirmation(userConfirmation);
						jsonObject.put("response", ControlTaskEnum.SUCCESS);
					}
				}
			} else {
				List<ControlTask> tasks = CacheDataBase.controlTaskRedisHelper.getRedisListValues(userId);
				for (int i = 0; i < tasks.size(); i++) {
					ControlTask task = tasks.get(i);
					if (task.getControllerLogId().equals(controlId)) {
						task.setUserConfirmation(userConfirmation);
						CacheDataBase.controlTaskRedisHelper.setRedisListIndexValue(userId, task, i);
						jsonObject.put("response", ControlTaskEnum.SUCCESS);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			controlHandlerUtil_log.error("userConfirmation error",e);
			jsonObject.put("response", ControlTaskEnum.ERROR);
			return jsonObject.toString();
		}
		return jsonObject.toString();
	}

	/***
	 * -------------- ------------------水肥药一体化的操作----------------- --------
	 */

	/**
	 * 设备回复的消息
	 * 
	 * @param command
	 * @param flag
	 * @return
	 */
	public static String wfmControlDeviceReturnMessage(WFMControlCommand command, boolean flag) {
		String commandCategory = command.getCommandCategory();
		WFMControlTask controlTask = command.getWfmControlTask();
		List<WFMControlCommand> list = controlTask.getWfmControlCommands();
		if ("execution".equals(commandCategory)) {
			command.setReceived(true);
			command.setReceiveTime(System.currentTimeMillis() / 1000);
			if (flag) { // 返回成功
				command.setReceivedResult(ControlTaskEnum.EXEUTION_SUCCESS);
				boolean isSuccess = true;
				for (int i = 0; i < list.size(); i++) {
					WFMControlCommand wCommand = list.get(i);
					isSuccess &= wCommand.isReceived();
					isSuccess &= (ControlTaskEnum.EXEUTION_SUCCESS.equals(wCommand.getReceivedResult()));
				}
				if (isSuccess) {
					controlTask.setAddReceived(true);
					controlTask.setAddResult(ControlTaskEnum.EXEUTION_SUCCESS);
					controlTask.setAddResultTime(System.currentTimeMillis() / 1000);
					controlTask.setTaskState(ControlTaskEnum.EXECUTING);
					controlTask.setResponseMessage(ControlTaskEnum.EXECUTION_SUCCESS_RESPONSE);
					CacheDataBase.userControlData.notifyObservers(controlTask.getUserId(), controlTask.pushUserMessage());// 通知用户
					CacheDataBase.wTaskService.updateControlTask(controlTask);
				}
			} else {
				command.setReceivedResult(ControlTaskEnum.EXECUTION_FAIL);
				controlTask.setAddResult(ControlTaskEnum.EXECUTION_FAIL);
				controlTask.setAddReceived(true);
				controlTask.setFaultIndentifying(command.getIndentifying());
				// 发生紧急事件，开启失败，推送给用户
				// CacheDataBase.userControlData.notifyObservers(controlTask.getUserId(),
				// controlTask.pushUserMessage());// 通知用户
			}
		} else if ("stop".equals(commandCategory)) {
			command.setReceived(true);
			command.setReceiveTime(System.currentTimeMillis() / 1000);
			if (flag) {
				command.setReceivedResult(ControlTaskEnum.STOP_SUCCESS);
				boolean isSuccess = true;
				for (int i = 0; i < list.size(); i++) {
					WFMControlCommand wCommand = list.get(i);
					isSuccess &= wCommand.isReceived();
					isSuccess &= (ControlTaskEnum.STOP_SUCCESS.equals(wCommand.getReceivedResult()));
				}
				if (isSuccess) {
					controlTask.setStopReceived(true);
					controlTask.setStopTime(System.currentTimeMillis() / 1000);
					controlTask.setStopResult(ControlTaskEnum.STOP_SUCCESS);
					controlTask.setTaskState(ControlTaskEnum.STOPPED);
					CacheDataBase.userControlData.notifyObservers(controlTask.getUserId(), controlTask.pushUserMessage());// 通知用户
					CacheDataBase.wTaskService.updateControlTask(controlTask);
				}
			} else {
				command.setReceivedResult(ControlTaskEnum.STOP_FAIL);
				controlTask.setStopReceived(true);
				controlTask.setStopResult(ControlTaskEnum.STOP_FAIL);
				controlTask.setFaultIndentifying(command.getIndentifying());
				// 发生紧急事件，开启失败，推送给用户
			}
		}
		return controlTask.pushUserMessage();

	}

	/**
	 * 
	 * @param controlTask
	 * @param flat
	 *            区分于定时添加和Cache线程的不同
	 * @return
	 */
	public static boolean wfmJudgeControlTaskConflict(WFMControlTask controlTask, boolean flat) {
		String userId = controlTask.getUserId();
		List<WFMControlTask> controlTasks = CacheDataBase.wfmControlTaskRedisHelper.getRedisListValues(userId);
		for (int i = 0; i < controlTasks.size(); i++) {
			WFMControlTask task = controlTasks.get(i);
			Integer systemId = controlTask.getSystemId();
			if (flat) {
				if (task.equals(controlTask)) {
					continue;
				}
			}
			if (systemId != null && systemId.equals(task.getSystemId())) {
				if (flat) {
					if (ControlTaskEnum.EXECUTING.equals(task.getTaskState()) || ControlTaskEnum.BLOCKING.equals(task.getTaskState())) {
						return true;
					}
				}
				long originalTime = task.getStartExecutionTime().getTime() / 1000;
				long newestTime = controlTask.getStartExecutionTime().getTime() / 1000;
				boolean compare = (newestTime > (originalTime + task.getExecutionTime() + waitTime))
						| ((newestTime + controlTask.getExecutionTime() + waitTime) < originalTime);
				if (!compare) { // 时间冲突
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 用户操作指令类别应该有三种，ImmediateExecution,fixedTimeExecution,handStop
	 * 
	 * @param wfmControlTask
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static JSONObject wfmHandlerControlMessage(WFMControlTask wfmControlTask, String userId, FarmControlSystemService farmControlSystemService)
			throws Exception {
		JSONObject object = new JSONObject();
		object.put("response", ControlTaskEnum.ERROR);
		String commandCategory = wfmControlTask.getCommandCategory();
		Object lock = CacheDataBase.wfmControlTaskRedisHelper.getLock(userId);
		List<WFMControlTask> wfmControlTasks = CacheDataBase.wfmControlTaskRedisHelper.getRedisListValues(userId);
		if ("ImmediateExecution".equals(commandCategory)) {
			Timestamp timestamp = Timestamp.valueOf(getSimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			wfmControlTask.setTaskTime(timestamp);
			wfmControlTask.setStartExecutionTime(timestamp); // 立马执行
			wfmControlTask.setLevel(2);
			wfmControlTask.setTaskState(ControlTaskEnum.BLOCKING); // 任务下发不代表已经开始运行
			if (wfmControlTasks != null && wfmControlTasks.size() > 0) {
				Iterator<WFMControlTask> iterator = wfmControlTasks.iterator();
				while (iterator.hasNext()) {
					WFMControlTask task = iterator.next();
					Integer systemId = wfmControlTask.getSystemId();
					if (systemId != null && systemId.equals(task.getSystemId())) {
						if (ControlTaskEnum.EXECUTING.equals(task.getTaskState()) || ControlTaskEnum.BLOCKING.equals(task.getTaskState())) {
							object.put("response", ControlTaskEnum.CONFLICT_CURRENT);
							return object;
						} else if (ControlTaskEnum.WAITTING.equals(task.getTaskState())) {
							long originalTime = task.getStartExecutionTime().getTime() / 1000;
							long newestTime = wfmControlTask.getStartExecutionTime().getTime() / 1000;
							boolean compare = (newestTime > (originalTime + task.getExecutionTime() + waitTime))
									| ((newestTime + wfmControlTask.getExecutionTime() + waitTime) < originalTime);
							if (!compare) { // 时间冲突
								object.put("response", ControlTaskEnum.CONFLICT);
								return object;
							}
						}
					}
				}
			}
			CacheDataBase.wTaskService.saveControlTask(wfmControlTask);
			farmControlSystemService.produceWFMControlTaskCommand(wfmControlTask); // 更新deviceId和collectorId等生成bits数组
			synchronized (lock) {
				CacheDataBase.wfmControlTaskRedisHelper.addRedisListValue(userId, wfmControlTask);
			}
			object.put("response", ControlTaskEnum.RUNNING);
			List<WFMControlCommand> list = wfmControlTask.getWfmControlCommands();
			for (int i = 0; i < list.size(); i++) {
				WFMControlCommand command = list.get(i);
				Long collectorId = command.getCollectorId();
				if (collectorId != null) {
					CacheDataBase.commandRedisHelper.setCommandRedisListValue(collectorId.toString(), command);
				}
				CacheDataBase.ioControlData.notifyObservers(collectorId); // 推送到长连接设备
			}
		} else if ("fixedTimeExecution".equals(commandCategory)) { // 暂时还未考虑时间冲突
			if (wfmControlTask.getStartExecutionTime() == null) {
				object.put("response", ControlTaskEnum.ERROR);
				return object;
			}
			wfmControlTask.setLevel(1);
			wfmControlTask.setTaskTime(Timestamp.valueOf(getSimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
			wfmControlTask.setTaskState(ControlTaskEnum.WAITTING);
			farmControlSystemService.produceWFMControlTaskCommand(wfmControlTask); // 更新deviceId和collectorId等生成bits数组
			if (wfmJudgeControlTaskConflict(wfmControlTask, false)) {
				object.put("response", ControlTaskEnum.CONFLICT);
				return object;
			}
			CacheDataBase.wTaskService.saveControlTask(wfmControlTask);
			synchronized (lock) {
				CacheDataBase.wfmControlTaskRedisHelper.addRedisListValue(userId, wfmControlTask);
			}
			object.put("response", ControlTaskEnum.RUNNING);
		} else if ("handStop".equals(commandCategory)) {
			object.put("response", ControlTaskEnum.NO_TASK);
			wfmControlTask = CacheDataBase.wfmControlTaskRedisHelper.queryControlTask(userId, wfmControlTask);
			wfmControlTask.setLevel(4);
			wfmControlTask.setStartStopTime(System.currentTimeMillis() / 1000);
			List<WFMControlCommand> list = wfmControlTask.getWfmControlCommands();
			// 关闭时操作顺序相反
			for (int i = list.size() - 1; i >= 0; i--) {
				WFMControlCommand command = list.get(i);
				Long collectorId = command.getCollectorId();
				command.setCommandCategory("stop");
				if (collectorId != null) {
					CacheDataBase.commandRedisHelper.setCommandRedisListValue(collectorId.toString(), command);
					CacheDataBase.ioControlData.notifyObservers(collectorId); // 推送到长连接设备
				}
			}
			wfmControlTask.setTaskState(ControlTaskEnum.STOPPING);
			CacheDataBase.wfmControlTaskRedisHelper.updateControlTaskListValu(userId, wfmControlTask);
			object.put("response", ControlTaskEnum.RUNNING);
		} else if ("query".equals(commandCategory)) {
			object.put("response", ControlTaskEnum.NO_TASK);
			Integer controllerLogId = wfmControlTask.getControllerLogId();
			if (wfmControlTasks != null) {
				Iterator<WFMControlTask> iterator = wfmControlTasks.iterator();
				while (iterator.hasNext()) {
					WFMControlTask cTask = iterator.next();
					if (controllerLogId != null && controllerLogId.equals(cTask.getControllerLogId())) {
						object.put("response", cTask.buildQueryResult());
						return object;
					}
				}
			} else {
				controlHandlerUtil_log.error("该用户无任务:" + userId);
			}
		}
		return object;
	}

}
