package com.ifarm.redis.util;

import java.util.Iterator;
import java.util.List;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Component;

import com.ifarm.bean.WFMControlTask;

@Component
public class WfmControlTaskRedisHelper extends BaseLockRedisHelper<Long, WFMControlTask> {

	public WfmControlTaskRedisHelper() {
		setRedisKeyName(RedisContstant.WFM_CONTROL_TASK_CACHE);
	}

	public boolean removeWfmControlTask(String key, Integer controlTaskId) {
		String redisKey = redisKeyName + key;
		ListOperations<String, WFMControlTask> listOperations = valueRedisTemplate.opsForList();
		boolean removeFlat = false;
		List<WFMControlTask> list = listOperations.range(redisKey, 0, listOperations.size(redisKey));
		Object lock = getLock(key);
		if (list != null && list.size() > 0) {
			Iterator<WFMControlTask> iterator = list.iterator();
			while (iterator.hasNext()) {
				WFMControlTask task = iterator.next();
				if (task.getControllerLogId().equals(controlTaskId)) {
					iterator.remove();
					removeFlat = true;
				}
			}
			if (removeFlat) {
				synchronized (lock) {
					replaceRedisListValues(key, list);
				}
			}
		}
		return removeFlat;
	}

	public void updateControlTaskListValue(String key, WFMControlTask controlTask) {
		Object lock = getLock(key);
		synchronized (lock) {
			List<WFMControlTask> list = getRedisListValues(key);
			for (int i = 0; i < list.size(); i++) {
				WFMControlTask task = list.get(i);
				if (controlTask.getControllerLogId().equals(task.getControllerLogId())) {
					setRedisListIndexValue(key, controlTask, i);
				}
			}
		}
	}

	public WFMControlTask queryControlTask(String key, WFMControlTask controlTask) {
		List<WFMControlTask> list = getRedisListValues(key);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				WFMControlTask task = list.get(i);
				if (task.getControllerLogId().equals(controlTask.getControllerLogId())) {
					return task;
				}
			}
		}
		return controlTask;
	}
	
	public WFMControlTask queryControlTask(String key, Integer taskId) {
		List<WFMControlTask> list = getRedisListValues(key);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				WFMControlTask task = list.get(i);
				if (task.getControllerLogId().equals(taskId)) {
					return task;
				}
			}
		}
		return null;
	}
}
