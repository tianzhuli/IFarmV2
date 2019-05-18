package com.ifarm.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ifarm.bean.ControlCommand;
import com.ifarm.bean.ControlTask;
import com.ifarm.bean.MultiControlCommand;
import com.ifarm.bean.MultiControlTask;
import com.ifarm.constant.ControlTaskEnum;
import com.ifarm.redis.util.ControlCommandRedisHelper;
import com.ifarm.redis.util.ControlTaskRedisHelper;
import com.ifarm.redis.util.WfmControlTaskRedisHelper;
import com.ifarm.service.ControlTaskService;
import com.ifarm.service.MultiControlTaskService;
import com.ifarm.wrapper.BooleanWrapper;

/**
 * 
 * @author Administrator 控制缓存的自动清理，后期需要优化
 *         首先考虑已经有添加回复的task，就已经有开始执行时间starttime，之后获取当前时间
 *         -starttime-waittime-executiontime-偏差值>0即可回收
 *         此线程及其重要，关系到整个控制系统的正常运行，后期需要好好优化
 */
@Component
public class ControlCacheCollection implements Runnable {
	private ControlTaskService controlTaskService;
	private MultiControlTaskService wfmControlTaskService;
	private static final Log controlCacheCollection_log = LogFactory
			.getLog(ControlCacheCollection.class);
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private int offset = 0; // 这个就是一般的误差时间
	private int timeout = 0;
	private int distTime = 120;
	volatile private boolean isStartCollect = true;

	@Autowired
	private ControlCommandRedisHelper commandRedisHelper;

	@Autowired
	private ControlTaskRedisHelper controlTaskRedisHelper;

	@Autowired
	private WfmControlTaskRedisHelper wfmControlTaskRedisHelper;

	@Autowired
	public void setTaskService(ControlTaskService taskService) {
		this.controlTaskService = taskService;
	}

	public ControlTaskService getTaskService() {
		return controlTaskService;
	}

	public MultiControlTaskService getWfmControlTaskService() {
		return wfmControlTaskService;
	}

	@Autowired
	public void setWfmControlTaskService(
			MultiControlTaskService wfmControlTaskService) {
		this.wfmControlTaskService = wfmControlTaskService;
	}

	public boolean isStartCollect() {
		return isStartCollect;
	}

	public void setStartCollect(boolean isStartCollect) {
		this.isStartCollect = isStartCollect;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * 
	 * @param task
	 * @param collectorId
	 * @param userId
	 * @param isStop
	 *            标志位,是否从cache中移除task
	 */
	public void recoverStopControlTask(ControlTask task, String userId,
			BooleanWrapper booleanWrapper, boolean isStop, boolean isUpdateTask) {
		try {
			if (isStop) {
				controlTaskRedisHelper.removeControlTask(userId,
						task.getControllerLogId());
				booleanWrapper.setFlag(true);
				clearControlTaskCommand(task, task.getCollectorId());
			}
			if (isUpdateTask) {
				getTaskService().updateControlTask(task);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void recoverStopControlTask(MultiControlTask wTask, String userId,
			BooleanWrapper booleanWrapper, boolean isStop, boolean isUpdateTask) {
		try {
			if (isStop) {
				wfmControlTaskRedisHelper.removeWfmControlTask(userId,
						wTask.getControllerLogId());
				booleanWrapper.setFlag(true);
				wfmClearControlTaskCommand(wTask);
			}
			if (isUpdateTask) {
				getWfmControlTaskService().updateControlTask(wTask);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 系统计算时间，下发停止指令
	 * 
	 * @param controlTask
	 * @throws Exception
	 */
	public void controlTaskStopCommandProduce(ControlTask controlTask)
			throws Exception {
		controlTask.setLevel(3);
		ControlCommand command = controlTask.buildCommand("stop");
		Long collectorId = controlTask.getCollectorId();
		if (collectorId != null) {
			commandRedisHelper.setCommandRedisListValue(collectorId.toString(),
					command);
			controlTask.setStartStopTime(System.currentTimeMillis() / 1000);
			controlTask.setTaskState(ControlTaskEnum.STOPPING);
		}// 下发添加任务到设备
		CacheDataBase.ioControlData.notifyObservers(collectorId);
		controlTaskRedisHelper.updateControlTaskListValue(
				controlTask.getUserId(), controlTask);
		controlCacheCollection_log.info("任务正在停止中......");
	}

	public void clearControlTaskCommand(ControlTask controlTask,
			Long collectorId) {
		if (collectorId != null) {
			commandRedisHelper.removeControlCommand(collectorId.toString(),
					controlTask.getControllerLogId());
		}
	}

	public void wfmClearControlTaskCommand(MultiControlTask wfmControlTask) {
		commandRedisHelper.removeWfmControlCommand(wfmControlTask);
	}

	public void wfmControlTaskStopCommandProduce(MultiControlTask wfmControlTask)
			throws Exception {
		wfmControlTask.setLevel(3);
		wfmControlTask.setStartStopTime(System.currentTimeMillis() / 1000);
		List<MultiControlCommand> list = wfmControlTask.getWfmControlCommands();
		for (int i = list.size() - 1; i >= 0; i--) {
			MultiControlCommand command = list.get(i);
			Long collectorId = command.getCollectorId();
			command.setCommandCategory("stop");
			if (collectorId != null) {
				commandRedisHelper.setCommandRedisListValue(
						collectorId.toString(), command);
				CacheDataBase.ioControlData.notifyObservers(collectorId); // 推送到长连接设备
			}
		}
		wfmControlTask.setTaskState(ControlTaskEnum.STOPPING);
		wfmControlTaskRedisHelper.updateControlTaskListValue(
				wfmControlTask.getUserId(), wfmControlTask);
		controlCacheCollection_log.info("任务正在停止中......");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		controlCacheCollection_log.info("---回收线程开启---");
		while (true) {
			while (isStartCollect) {
				Set<String> keys = controlTaskRedisHelper.keysSet();
				for (String key : keys) {
					String[] splitArray = key.split("_");
					String userId = splitArray[splitArray.length - 1];
					List<ControlTask> controlTasks = controlTaskRedisHelper
							.getRedisListValues(userId);
					for (int i = 0; i < controlTasks.size(); i++) {
						ControlTask controlTask = controlTasks.get(i);
						BooleanWrapper flat = new BooleanWrapper();
						try {
							String taskState = controlTask.getTaskState();
							if (ControlTaskEnum.WAITTING.equals(taskState)
									|| ControlTaskEnum.CONFICTING
											.equals(taskState)) {
								long timeDev = Timestamp.valueOf(
										format.format(new Date())).getTime()
										/ 1000
										- controlTask.getStartExecutionTime()
												.getTime() / 1000;
								if (timeDev >= 0 && timeDev <= distTime) { // 执行任务的时间到了
									// if
									// (!ControlHandlerUtil.judgeControlTaskConflict(controlTask,
									// true)) {
									controlTask
											.setTaskState(ControlTaskEnum.BLOCKING); // 任务下发，不一定真的执行了
									controlTask
											.setStartExecutionTime(Timestamp.valueOf(format
													.format(new Date())));
									ControlCommand command = controlTask
											.buildCommand("execution");
									Long collectorId = controlTask
											.getCollectorId();
									// 这个地方需要考虑容错，假如上一个任务由于设备原因，延迟了30s，这个新的任务会对之前的任务有影响
									if (collectorId != null) {
										commandRedisHelper
												.setCommandRedisListValue(
														collectorId.toString(),
														command);
									}// 下发添加任务到设备
									CacheDataBase.ioControlData
											.notifyObservers(collectorId);
									// CacheDataBase.userControlData.notifyObservers(userId,
									// controlTask.pushUserMessage());//
									// 通知用户
									controlCacheCollection_log.info(userId
											+ "---发现到了时间应该执行的任务---");
									flat.setFlag(true);
									/*
									 * } else { //
									 * controlCacheCollection_log.info(userId //
									 * + "---任务冲突---");
									 * controlTask.setTaskState(
									 * ControlTaskEnum.CONFICTING); }
									 */
								} else if (timeDev > distTime) {
									controlTask
											.setResponseMessage(ControlTaskEnum.STOP_SUCESS_RESPONSE);
									recoverStopControlTask(controlTask, userId,
											flat, true, true);
									controlCacheCollection_log.info("指令超时："
											+ userId + "集中器回收");
									continue;
								}
							}
							Long collectorId = controlTask.getCollectorId();
							if (controlTask.isStopReceived()) {
								if (ControlTaskEnum.STOP_FAIL
										.equals(controlTask.getStopResult())) {
									controlTask
											.setResponseMessage(ControlTaskEnum.STOP_FAIL_RESPONSE);
									CacheDataBase.userControlData
											.notifyObservers(userId,
													controlTask
															.pushUserMessage());// 通知用户
									recoverStopControlTask(controlTask, userId,
											flat, true, true);
									controlCacheCollection_log.info("停止失败的："
											+ userId + "集中器回收");
								} else if (ControlTaskEnum.STOP_SUCCESS
										.equals(controlTask.getStopResult())) {
									controlTask
											.setResponseMessage(ControlTaskEnum.STOP_SUCESS_RESPONSE);
									recoverStopControlTask(controlTask, userId,
											flat, true, true);
									controlCacheCollection_log
											.info("已经收到停止指令的：" + userId
													+ "集中器回收");
								}
								continue;
							}
							if (controlTask.isAddReceived()) {
								if (controlTask.getStartStopTime() != 0) { // 已经下发了停止指令
									long compare = Timestamp.valueOf(
											format.format(new Date()))
											.getTime()
											/ 1000
											- controlTask.getStartStopTime();
									if (compare >= this.offset) {
										controlTask
												.setResponseMessage(ControlTaskEnum.STOP_TIMEOUT_RESPONSE);
										recoverStopControlTask(controlTask,
												userId, flat, true, true);
										CacheDataBase.userControlData
												.notifyObservers(
														userId,
														controlTask
																.pushUserMessage());// 通知用户
										// clearControlTaskCommand(controlTask,
										// collectorId);
										controlCacheCollection_log.info(compare
												+ "s长时间未收到停止回复：" + collectorId
												+ "集中器");
									}
									continue;
								}
								if (ControlTaskEnum.EXECUTION_FAIL
										.equals(controlTask.getAddResult())) {
									controlTask
											.setResponseMessage(ControlTaskEnum.EXECUTION_FAIL_RESPONSE);
									recoverStopControlTask(controlTask, userId,
											flat, true, true);
									CacheDataBase.userControlData
											.notifyObservers(userId,
													controlTask
															.pushUserMessage());// 通知用户
									controlCacheCollection_log.info("任务执行失败的："
											+ userId + "集中器回收");
									continue;
								} else if (ControlTaskEnum.EXEUTION_SUCCESS
										.equals(controlTask.getAddResult())) {
									long compare = System.currentTimeMillis()
											/ 1000
											- controlTask.getAddResultTime()
											- controlTask.getExecutionTime(); // this.offset是偏差系数
									if (compare >= 0) {
										controlTask
												.setResponseMessage(ControlTaskEnum.EXECUTION_COMPLETE_RESPONSE); // 任务执行完成
										controlCacheCollection_log
												.info("任务执行完成：" + userId
														+ "集中器");
										controlTaskStopCommandProduce(controlTask);
										CacheDataBase.userControlData
												.notifyObservers(
														userId,
														controlTask
																.pushUserMessage());// 通知用户
										continue;
									}
								}
							} else {
								long current = Timestamp.valueOf(
										format.format(new Date())).getTime()
										/ 1000
										- controlTask.getStartExecutionTime()
												.getTime() / 1000;
								if (current >= this.offset * this.timeout) {
									controlTask
											.setResponseMessage(ControlTaskEnum.EXECUTION_TIMEOUT_RESPONSE);
									recoverStopControlTask(controlTask, userId,
											flat, true, true);
									CacheDataBase.userControlData
											.notifyObservers(userId,
													controlTask
															.pushUserMessage());// 通知用户
									// clearControlTaskCommand(controlTask,
									// collectorId);
									controlCacheCollection_log.info(current
											+ "s长时间未收到添加回复：" + collectorId
											+ "集中器");
									continue;
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							controlCacheCollection_log.error("回收对象异常：" + e);
							recoverStopControlTask(controlTask, userId, flat,
									true, true);
						}
						if (flat.isFlag()) {
							controlTaskRedisHelper.updateControlTaskListValue(
									userId, controlTask);
						}
						// 更新状态
					}
				}

				/**
				 * 水肥药一体化的回收
				 */
				Set<String> wfmSet = wfmControlTaskRedisHelper.keysSet();
				for (String key : wfmSet) {
					String[] splitArray = key.split("_");
					String userId = splitArray[splitArray.length - 1];
					List<MultiControlTask> controlTasks = wfmControlTaskRedisHelper
							.getRedisListValues(userId);
					for (int index = 0; index < controlTasks.size(); index++) {
						MultiControlTask wfmControlTask = controlTasks
								.get(index);
						BooleanWrapper flat = new BooleanWrapper();
						try {
							String taskState = wfmControlTask.getTaskState();
							if (ControlTaskEnum.WAITTING.equals(taskState)) {
								long timeDev = Timestamp.valueOf(
										format.format(new Date())).getTime()
										/ 1000
										- wfmControlTask
												.getStartExecutionTime()
												.getTime() / 1000;
								if (timeDev >= 0 && timeDev <= distTime) { // 执行任务的时间到了
									// if
									// (!ControlHandlerUtil.wfmJudgeControlTaskConflict(wfmControlTask,
									// true)) {
									wfmControlTask
											.setTaskState(ControlTaskEnum.BLOCKING); // 任务下发，不一定真的执行了
									wfmControlTask
											.setStartExecutionTime(Timestamp.valueOf(format
													.format(new Date())));
									List<MultiControlCommand> list = wfmControlTask
											.getWfmControlCommands();
									for (int i = 0; i < list.size(); i++) {
										MultiControlCommand wfmControlCommand = list
												.get(i);
										Long collectorId = wfmControlCommand
												.getCollectorId();
										if (collectorId != null) {
											commandRedisHelper
													.setCommandRedisListValue(
															collectorId
																	.toString(),
															wfmControlCommand);
										}
										CacheDataBase.ioControlData
												.notifyObservers(collectorId); // 推送到长连接设备
									}
									controlCacheCollection_log.info(userId
											+ "---发现到了时间应该执行的任务---");
									flat.setFlag(true);
									/*
									 * } else { //
									 * controlCacheCollection_log.info(userId //
									 * + "---任务冲突---");
									 * wfmControlTask.setTaskState
									 * (ControlTaskEnum.CONFICTING); }
									 */
								} else if (timeDev > distTime) {
									wfmControlTask
											.setResponseMessage(ControlTaskEnum.STOP_SUCESS_RESPONSE);
									recoverStopControlTask(wfmControlTask,
											userId, flat, true, true);
									controlCacheCollection_log.info("超时的指令："
											+ userId + "集中器回收");
									continue;
								}
							}
							if (wfmControlTask.isStopReceived()) {
								if (ControlTaskEnum.STOP_FAIL
										.equals(wfmControlTask.getStopResult())) {
									wfmControlTask
											.setResponseMessage(ControlTaskEnum.STOP_FAIL_RESPONSE);
									recoverStopControlTask(wfmControlTask,
											userId, flat, true, true);
									CacheDataBase.userControlData
											.notifyObservers(userId,
													wfmControlTask
															.pushUserMessage());// 通知用户
									controlCacheCollection_log.info("停止失败的："
											+ userId + "集中器回收");
								} else if (ControlTaskEnum.STOP_SUCCESS
										.equals(wfmControlTask.getStopResult())) {
									wfmControlTask
											.setResponseMessage(ControlTaskEnum.STOP_SUCESS_RESPONSE);
									recoverStopControlTask(wfmControlTask,
											userId, flat, true, true);
									controlCacheCollection_log
											.info("已经收到停止指令的：" + userId
													+ "集中器回收");
								}
								// CacheDataBase.userControlData.notifyObservers(userId,
								// wfmControlTask.pushUserMessage());//
								// 通知用户
								continue;
							}
							if (wfmControlTask.isAddReceived()) {
								if (wfmControlTask.getStartStopTime() != 0) { // 已经下发了停止指令
									long compare = Timestamp.valueOf(
											format.format(new Date()))
											.getTime()
											/ 1000
											- wfmControlTask.getStartStopTime();
									if (compare >= this.offset) {
										wfmControlTask
												.setResponseMessage(ControlTaskEnum.STOP_TIMEOUT_RESPONSE);
										recoverStopControlTask(wfmControlTask,
												userId, flat, true, true);
										CacheDataBase.userControlData
												.notifyObservers(
														userId,
														wfmControlTask
																.pushUserMessage());// 通知用户
										// wfmClearControlTaskCommand(wfmControlTask);
										controlCacheCollection_log.info(compare
												+ "s长时间未收到停止回复："
												+ wfmControlTask
														.getControllerLogId()
												+ "任务");
									}
									continue;
								}
								if (ControlTaskEnum.EXECUTION_FAIL
										.equals(wfmControlTask.getAddResult())) {
									wfmControlTask
											.setResponseMessage(ControlTaskEnum.EXECUTION_FAIL_RESPONSE);
									recoverStopControlTask(wfmControlTask,
											userId, flat, false, true);
									// 需要紧急停止，下发停止指令
									// wfmControlTaskStopCommandProduce(wfmControlTask);
									CacheDataBase.userControlData
											.notifyObservers(userId,
													wfmControlTask
															.pushUserMessage());// 通知用户
									controlCacheCollection_log.info("任务执行失败的："
											+ userId + "集中器回收");
									continue;
								} else if (ControlTaskEnum.EXEUTION_SUCCESS
										.equals(wfmControlTask.getAddResult())) {
									long compare = System.currentTimeMillis()
											/ 1000
											- wfmControlTask.getAddResultTime()
											- wfmControlTask.getExecutionTime(); // this.offset是偏差系数
									if (compare >= 0) {
										wfmControlTask
												.setResponseMessage(ControlTaskEnum.EXECUTION_COMPLETE_RESPONSE); // 任务执行完成
										controlCacheCollection_log
												.info("任务执行完成：" + userId
														+ "集中器");
										wfmControlTaskStopCommandProduce(wfmControlTask);
										CacheDataBase.userControlData
												.notifyObservers(
														userId,
														wfmControlTask
																.pushUserMessage());// 通知用户
										continue;
									}
								}
							} else {
								long current = Timestamp.valueOf(
										format.format(new Date())).getTime()
										/ 1000
										- wfmControlTask
												.getStartExecutionTime()
												.getTime() / 1000;
								if (current >= this.offset * this.timeout) {
									wfmControlTask
											.setResponseMessage(ControlTaskEnum.EXECUTION_TIMEOUT_RESPONSE);
									recoverStopControlTask(wfmControlTask,
											userId, flat, true, true);
									CacheDataBase.userControlData
											.notifyObservers(userId,
													wfmControlTask
															.pushUserMessage());// 通知用户
									// wfmClearControlTaskCommand(wfmControlTask);
									// 考虑是否下发停止命令，有可能是部分设备未收到，其他设备已经开始运行了
									controlCacheCollection_log.info(current
											+ "s长时间未收到添加回复："
											+ wfmControlTask
													.getControllerLogId()
											+ "任务");
									continue;
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							controlCacheCollection_log.error("回收对象异常：", e);
							recoverStopControlTask(wfmControlTask, userId,
									flat, true, true);
							// 清除异常的任务
						}
						if (flat.isFlag()) {
							wfmControlTaskRedisHelper
									.updateControlTaskListValue(userId,
											wfmControlTask);
							;
						}
					}

				}
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
