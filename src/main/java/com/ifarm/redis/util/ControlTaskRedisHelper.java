package com.ifarm.redis.util;

import java.util.Iterator;
import java.util.List;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Component;

import com.ifarm.bean.ControlTask;

@Component
public class ControlTaskRedisHelper extends BaseLockRedisHelper<Long, ControlTask> {

	public ControlTaskRedisHelper() {
		setRedisKeyName(RedisContstant.CONTROL_TASK_CACHE);
	}

	/**
	 * 移除task，但是因为run里面的lock直接锁住了这个key
	 * 
	 * @param key
	 * @param controlTaskId
	 * @return
	 */
	public boolean removeControlTask(String key, Integer controlTaskId) {
		String redisKey = redisKeyName + key;
		ListOperations<String, ControlTask> listOperations = valueRedisTemplate.opsForList();
		boolean removeFlat = false;
		List<ControlTask> list = listOperations.range(redisKey, 0, listOperations.size(redisKey));
		Object lock = getLock(key);
		if (list != null && list.size() > 0) {
			Iterator<ControlTask> iterator = list.iterator();
			while (iterator.hasNext()) {
				ControlTask task = iterator.next();
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
	
	public void updateControlTaskListValue(String key,ControlTask controlTask) {
		Object lock = getLock(key);
		synchronized (lock) {
			List<ControlTask> list= getRedisListValues(key);
			for (int i = 0; i < list.size(); i++) {
				ControlTask task = list.get(i);
				if (controlTask.getControllerLogId().equals(task.getControllerLogId())) {
					setRedisListIndexValue(key, controlTask, i);
				}
			}
		}
	}
	
	public ControlTask queryControlTask(String key,ControlTask controlTask) {
		List<ControlTask> list = getRedisListValues(key);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				ControlTask task = list.get(i);
				if (task.getControllerLogId().equals(controlTask.getControllerLogId())) {
					return task;
				}
			}
		}
		return controlTask;
	}
	
	public ControlTask queryControlTask(String key,Integer taskId) {
		List<ControlTask> list = getRedisListValues(key);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				ControlTask task = list.get(i);
				if (task.getControllerLogId().equals(taskId)) {
					return task;
				}
			}
		}
		return null;
	}
}
