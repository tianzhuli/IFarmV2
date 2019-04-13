package com.ifarm.util;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.ControlTask;
import com.ifarm.bean.MultiControlTask;
import com.ifarm.constant.ControlTaskEnum;

public class ControlTaskUtil {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(ControlTaskUtil.class);

	public static String queryTasks(LinkedBlockingQueue<ControlTask> list,
			JSONArray array) {
		Iterator<ControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			array.add((iterator.next().queryCurrentTask()));
		}
		return array.toString();
	}

	public static String queryExecutingTasks(
			LinkedBlockingQueue<ControlTask> list, JSONArray array) {
		Iterator<ControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			ControlTask controlTask = iterator.next();
			if (ControlTaskEnum.EXECUTING.equals(controlTask.getTaskState())) {
				array.add(controlTask.queryCurrentTask());
			}
		}
		return array.toString();
	}

	public static String queryTasks(LinkedBlockingQueue<ControlTask> list,
			String controlType, JSONArray array) {
		Iterator<ControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			ControlTask controlTask = iterator.next();
			if (controlType.equals(controlTask.getControlType())) {
				array.add((controlTask.queryCurrentTask()));
			}
		}
		return array.toString();
	}

	public static String queryWfmTasks(
			LinkedBlockingQueue<MultiControlTask> list, String controlType,
			JSONArray array) {
		Iterator<MultiControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			MultiControlTask controlTask = iterator.next();
			if (controlType.equals(controlTask.getControlType())) {
				array.add((controlTask.queryCurrentTask()));
			}
		}
		return array.toString();
	}

	public static String queryWfmTasks(
			LinkedBlockingQueue<MultiControlTask> list, JSONArray array) {
		Iterator<MultiControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			array.add((iterator.next().queryCurrentTask()));
		}
		return array.toString();
	}

	public static String queryWfmExecutingTasks(
			LinkedBlockingQueue<MultiControlTask> list, JSONArray array) {
		Iterator<MultiControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			MultiControlTask wfmControlTask = iterator.next();
			if (ControlTaskEnum.EXECUTING.equals(wfmControlTask.getTaskState())) {
				array.add((wfmControlTask.queryCurrentTask()));
			}
		}
		return array.toString();
	}

	public static String queryTasks(List<ControlTask> list, JSONArray array,
			ControlTask controlTask) {
		Iterator<ControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			ControlTask task = iterator.next();
			if (controlTask.getControlType() != null
					&& !controlTask.getControlType().equals(
							task.getControlType())) {
				continue;
			}
			if (controlTask.getUnitDistrict() != null
					&& !controlTask.getUnitDistrict().equals(
							task.getUnitDistrict())) {
				continue;
			}
			array.add((task.queryCurrentTask()));
		}
		return array.toString();
	}

	public static String queryExecutingTasks(List<ControlTask> list,
			JSONArray array, ControlTask controlTask) {
		Iterator<ControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			ControlTask task = iterator.next();
			if (controlTask.getControlType() != null
					&& !controlTask.getControlType().equals(
							task.getControlType())) {
				continue;
			}
			if (controlTask.getUnitDistrict() != null
					&& !controlTask.getUnitDistrict().equals(
							task.getUnitDistrict())) {
				continue;
			}
			if (ControlTaskEnum.EXECUTING.equals(task.getTaskState())) {
				array.add(task.queryCurrentTask());
			}
		}
		return array.toString();
	}

	public static String queryWfmTasks(List<MultiControlTask> list,
			JSONArray array, ControlTask multiControlTask) {
		Iterator<MultiControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			MultiControlTask controlTask = iterator.next();
			if (multiControlTask.getControlType() != null
					&& !multiControlTask.getControlType().equals(
							controlTask.getControlType())) {
				continue;
			}
			if (multiControlTask.getUnitDistrict() != null
					&& !multiControlTask.getUnitDistrict().equals(
							controlTask.getUnitDistrict())) {
				continue;
			}
			array.add((controlTask.queryCurrentTask()));
		}
		return array.toString();
	}

	public static String queryWfmExecutingTasks(List<MultiControlTask> list,
			JSONArray array, ControlTask multiControlTask) {
		Iterator<MultiControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			MultiControlTask wfmControlTask = iterator.next();
			if (multiControlTask.getControlType() != null
					&& !multiControlTask.getControlType().equals(
							wfmControlTask.getControlType())) {
				continue;
			}
			if (multiControlTask.getUnitDistrict() != null
					&& !multiControlTask.getUnitDistrict().equals(
							wfmControlTask.getUnitDistrict())) {
				continue;
			}
			if (ControlTaskEnum.EXECUTING.equals(wfmControlTask.getTaskState())) {
				array.add((wfmControlTask.queryCurrentTask()));
			}
		}
		return array.toString();
	}

	public static MultiControlTask fromControlTask(ControlTask controlTask,
			HttpServletRequest httpRequest) throws Exception {
		MultiControlTask wTask = fromJson(JsonObjectUtil
				.fromOriginalBean(controlTask));
		wTask.setCanNo(httpRequest.getParameter("canNo"));
		return wTask;
	}

	/**
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 *             该异常在contrlhandler处被处理，避免加入到cache中
	 */
	public static MultiControlTask fromJson(JSONObject jsonObject)
			throws Exception {
		MultiControlTask controlStrategy = new MultiControlTask();
		fillControlTaskFiled(jsonObject, controlStrategy);
		return controlStrategy;
	}

	public int getdataToShort(byte[] packedpackge, int pointer) {
		int value = (short) ((packedpackge[pointer] & 0xff) | (((packedpackge[pointer + 1] & 0xff) << 8)));
		return value & 0xffff;
	}

	public int getdataTypeToInt(byte[] packedpackge, int pointer) {// 四字节无符号整数(主要是和生成的标识一致)
		int value = (packedpackge[pointer] & 0xff)
				| ((packedpackge[pointer + 1] & 0xff) << 8)
				| ((packedpackge[pointer + 2] & 0xff) << 16)
				| ((packedpackge[pointer + 3] & 0xff) << 24);
		return value & 0xffffffff;
	}

	public static ControlTask fromJsonTotask(JSONObject jsonObject)
			throws Exception {
		ControlTask controlStrategy = new ControlTask();
		fillControlTaskFiled(jsonObject, controlStrategy);
		return controlStrategy;
	}

	public static void fillControlTaskFiled(JSONObject jsonObject,
			Object controlStrategy) {
		Field[] fields = controlStrategy.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			String key = fields[i].getName();
			if (jsonObject.containsKey(key)) {
				try {
					if (fields[i].getType() == Integer.class) {
						fields[i].set(controlStrategy,
								jsonObject.getInteger(key));
					} else if (fields[i].getType() == String.class) {
						fields[i].set(controlStrategy,
								jsonObject.getString(key));
					} else if (fields[i].getType() == Double.class) {
						fields[i].set(controlStrategy,
								jsonObject.getDouble(key));
					} else if (fields[i].getType() == Boolean.class) {
						fields[i].set(controlStrategy,
								jsonObject.getBoolean(key));
					} else if (fields[i].getType() == Long.class) {
						fields[i].set(controlStrategy, jsonObject.getLong(key));
					} else if (fields[i].getType() == Timestamp.class) {
						fields[i].set(controlStrategy,
								Timestamp.valueOf(jsonObject.getString(key)));
					} else {
						if (key.equals("format")
								|| key.equals("serialVersionUID")) {
							continue;
						}
						fields[i].set(controlStrategy, jsonObject.get(key));
					}
				} catch (Exception e) {
					// TODO: handle exception
					LOGGER.error("controltask parse errpr", e);
				}

			}
		}
	}
}
