package com.ifarm.nosql.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.ifarm.log.RedisLog;

@Repository
public class InitRedisDao {
	@Autowired
	private RedisTemplate<String, String> template;
	
	public boolean redisConnect() {
		try {
			String pong = template.getConnectionFactory().getConnection().ping();
			RedisLog.CONNECTREDISLOG_LOG.info("redis返回：" + pong);
		} catch (Exception e) {
			RedisLog.CONNECTREDISLOG_LOG.error("redis connect:" + e.toString());
			return false;
		}
		return true;
	}
}
