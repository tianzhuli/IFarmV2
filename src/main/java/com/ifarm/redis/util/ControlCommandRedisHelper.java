package com.ifarm.redis.util;

import java.util.Iterator;
import java.util.List;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Component;

import com.ifarm.bean.ControlCommand;
import com.ifarm.bean.WFMControlCommand;
import com.ifarm.bean.WFMControlTask;

@Component
public class ControlCommandRedisHelper extends BaseLockRedisHelper<Long, ControlCommand> {

	public ControlCommandRedisHelper() {
		setRedisKeyName(RedisContstant.CONTROL_COMMAND_CACHE);
	}

	/*private void controlCommandIdProduce(ControlCommand command) {
		String commandId = CacheDataBase.machineCode + String.valueOf(command.hashCode());
		command.setCommandId(commandId);
	}*/

	public void setCommandRedisListValue(String key, ControlCommand value) {
		// TODO Auto-generated method stub
		//controlCommandIdProduce(value);
		super.addRedisListValue(key, value);
	}
	
	public boolean removeControlCommand(String key, Integer controlTaskId) {
		Object lock = getLock(key);
		String redisKey = redisKeyName + key;
		ListOperations<String, ControlCommand> listOperations = valueRedisTemplate.opsForList();
		boolean removeFlat = false;
		List<ControlCommand> list = listOperations.range(redisKey, 0, listOperations.size(redisKey));
		if (list != null && list.size() > 0) {
			Iterator<ControlCommand> iterator = list.iterator();
			while (iterator.hasNext()) {
				ControlCommand command = iterator.next();
				if (controlTaskId.equals(command.getTaskId())) {
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

	public void removeWfmControlCommand(WFMControlTask wfmControlTask) {
		List<WFMControlCommand> list = wfmControlTask.getWfmControlCommands();
		for (int i = 0; i < list.size(); i++) {
			WFMControlCommand wCommand = list.get(i);
			Long collectorId = wCommand.getCollectorId();
			if (collectorId != null) {
				Object lock = getLock(collectorId.toString());
				String redisKey = redisKeyName + collectorId;
				ListOperations<String, ControlCommand> listOperations = valueRedisTemplate.opsForList();
				List<ControlCommand> commands = listOperations.range(redisKey, 0, listOperations.size(redisKey));
				if (commands != null && commands.size() > 0) {
					boolean removeFalt = false;
					Iterator<ControlCommand> iterator = commands.iterator();
					while (iterator.hasNext()) {
						ControlCommand command = iterator.next();
						if (wfmControlTask.getControllerLogId().equals(command.getTaskId())) {
							iterator.remove();
							removeFalt = true;
						}
					}
					if (removeFalt) {
						synchronized (lock) {
							replaceRedisListValues(collectorId.toString(), commands);
						}
					}
				}
			}
		}

	}
}
