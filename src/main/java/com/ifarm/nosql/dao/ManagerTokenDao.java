package com.ifarm.nosql.dao;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ifarm.nosql.bean.ManagerToken;
import com.ifarm.redis.util.RedisContstant;

@Repository
public class ManagerTokenDao {
	@Autowired
	private StringRedisTemplate redisTemplate;

	public void saveManagerToken(ManagerToken mToken) {
		redisTemplate.opsForValue().set(RedisContstant.MANAGER_TOKEN + mToken.getManagerId(), mToken.getToken(), 30, TimeUnit.MINUTES);
	}
	
	@ExceptionHandler()
	public String getManagerToken(String managerId) {
		return redisTemplate.opsForValue().get(RedisContstant.MANAGER_TOKEN + managerId);
	}
}
