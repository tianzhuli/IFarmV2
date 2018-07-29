package com.ifarm.redis.util;

import java.util.HashMap;
import java.util.Map;

public class BaseLockRedisHelper<K, V> extends BaseRedisHelper<K, V> {
	private static Map<String, Object> lockMap = new HashMap<String, Object>();

	public BaseLockRedisHelper() {

	}

	/**
	 * 后期考虑分布式锁
	 * 
	 * @param key
	 * @return
	 */
	public Object getLock(String key) {
		Object lock = lockMap.get(key);
		if (lock == null) {
			lock = new Object();
			lockMap.put(key, lock);
		}
		return lock;
	}
}
