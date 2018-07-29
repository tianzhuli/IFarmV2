package com.ifarm.util;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.ControlTask;
import com.ifarm.bean.WFMControlTask;
import com.ifarm.constant.ControlTaskEnum;

public class ControlTaskUtil {

	public static final Logger LOGGER = LoggerFactory.getLogger(ControlTaskUtil.class);

	public static String queryTasks(LinkedBlockingQueue<ControlTask> list, JSONArray array) {
		Iterator<ControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			array.add((iterator.next().queryCurrentTask()));
		}
		return array.toString();
	}

	public static String queryExecutingTasks(LinkedBlockingQueue<ControlTask> list, JSONArray array) {
		Iterator<ControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			ControlTask controlTask = iterator.next();
			if (ControlTaskEnum.EXECUTING.equals(controlTask.getTaskState())) {
				array.add(controlTask.queryCurrentTask());
			}
		}
		return array.toString();
	}

	public static String queryTasks(LinkedBlockingQueue<ControlTask> list, String controlType, JSONArray array) {
		Iterator<ControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			ControlTask controlTask = iterator.next();
			if (controlType.equals(controlTask.getControlType())) {
				array.add((controlTask.queryCurrentTask()));
			}
		}
		return array.toString();
	}

	public static String queryWfmTasks(LinkedBlockingQueue<WFMControlTask> list, String controlType, JSONArray array) {
		Iterator<WFMControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			WFMControlTask controlTask = iterator.next();
			if (controlType.equals(controlTask.getControlType())) {
				array.add((controlTask.queryCurrentTask()));
			}
		}
		return array.toString();
	}

	public static String queryWfmTasks(LinkedBlockingQueue<WFMControlTask> list, JSONArray array) {
		Iterator<WFMControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			array.add((iterator.next().queryCurrentTask()));
		}
		return array.toString();
	}

	public static String queryWfmExecutingTasks(LinkedBlockingQueue<WFMControlTask> list, JSONArray array) {
		Iterator<WFMControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			WFMControlTask wfmControlTask = iterator.next();
			if (ControlTaskEnum.EXECUTING.equals(wfmControlTask.getTaskState())) {
				array.add((wfmControlTask.queryCurrentTask()));
			}
		}
		return array.toString();
	}

	public static String queryTasks(List<ControlTask> list, JSONArray array) {
		Iterator<ControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			array.add((iterator.next().queryCurrentTask()));
		}
		return array.toString();
	}

	public static String queryExecutingTasks(List<ControlTask> list, JSONArray array) {
		Iterator<ControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			ControlTask controlTask = iterator.next();
			if (ControlTaskEnum.EXECUTING.equals(controlTask.getTaskState())) {
				array.add(controlTask.queryCurrentTask());
			}
		}
		return array.toString();
	}

	public static String queryTasks(List<ControlTask> list, String controlType, JSONArray array) {
		Iterator<ControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			ControlTask controlTask = iterator.next();
			if (controlType.equals(controlTask.getControlType())) {
				array.add((controlTask.queryCurrentTask()));
			}
		}
		return array.toString();
	}

	public static String queryWfmTasks(List<WFMControlTask> list, String controlType, JSONArray array) {
		Iterator<WFMControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			WFMControlTask controlTask = iterator.next();
			if (controlType.equals(controlTask.getControlType())) {
				array.add((controlTask.queryCurrentTask()));
			}
		}
		return array.toString();
	}

	public static String queryWfmTasks(List<WFMControlTask> list, JSONArray array) {
		Iterator<WFMControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			array.add((iterator.next().queryCurrentTask()));
		}
		return array.toString();
	}

	public static String queryWfmExecutingTasks(List<WFMControlTask> list, JSONArray array) {
		Iterator<WFMControlTask> iterator = list.iterator();
		while (iterator.hasNext()) {
			WFMControlTask wfmControlTask = iterator.next();
			if (ControlTaskEnum.EXECUTING.equals(wfmControlTask.getTaskState())) {
				array.add((wfmControlTask.queryCurrentTask()));
			}
		}
		return array.toString();
	}

	public static WFMControlTask fromControlTask(ControlTask controlTask) throws Exception {
		WFMControlTask wTask = fromJson(JsonObjectUtil.fromOriginalBean(controlTask));
		return wTask;
	}

	/**
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 *             该异常在contrlhandler处被处理，避免加入到cache中
	 */
	public static WFMControlTask fromJson(JSONObject jsonObject) throws Exception {
		WFMControlTask controlStrategy = new WFMControlTask();
		fillControlTaskFiled(jsonObject, controlStrategy);
		return controlStrategy;
	}

	public int getdataToShort(byte[] packedpackge, int pointer) {
		int value = (short) ((packedpackge[pointer] & 0xff) | (((packedpackge[pointer + 1] & 0xff) << 8)));
		return value & 0xffff;
	}

	public int getdataTypeToInt(byte[] packedpackge, int pointer) {// 四字节无符号整数(主要是和生成的标识一致)
		int value = (packedpackge[pointer] & 0xff) | ((packedpackge[pointer + 1] & 0xff) << 8) | ((packedpackge[pointer + 2] & 0xff) << 16)
				| ((packedpackge[pointer + 3] & 0xff) << 24);
		return value & 0xffffffff;
	}

	public static ControlTask fromJsonTotask(JSONObject jsonObject) throws Exception {
		ControlTask controlStrategy = new ControlTask();
		fillControlTaskFiled(jsonObject, controlStrategy);
		return controlStrategy;
	}

	public static void fillControlTaskFiled(JSONObject jsonObject, Object controlStrategy) {
		Field[] fields = controlStrategy.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			String key = fields[i].getName();
			if (jsonObject.containsKey(key)) {
				try {
					if (fields[i].getType() == Integer.class) {
						fields[i].set(controlStrategy, jsonObject.getInteger(key));
					} else if (fields[i].getType() == String.class) {
						fields[i].set(controlStrategy, jsonObject.getString(key));
					} else if (fields[i].getType() == Double.class) {
						fields[i].set(controlStrategy, jsonObject.getDouble(key));
					} else if (fields[i].getType() == Boolean.class) {
						fields[i].set(controlStrategy, jsonObject.getBoolean(key));
					} else if (fields[i].getType() == Long.class) {
						fields[i].set(controlStrategy, jsonObject.getLong(key));
					} else if (fields[i].getType() == Timestamp.class) {
						fields[i].set(controlStrategy, Timestamp.valueOf(jsonObject.getString(key)));
					} else {
						if (key.equals("format") || key.equals("serialVersionUID")) {
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
