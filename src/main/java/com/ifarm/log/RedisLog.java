package com.ifarm.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ifarm.nosql.dao.InitRedisDao;
import com.ifarm.service.CollectorDeviceValueService;

public class RedisLog {
	public static final Logger CONNECTREDISLOG_LOG = LoggerFactory.getLogger(InitRedisDao.class);
	public static final Logger COLLECTOR_DEVICE_VALUE_LOG = LoggerFactory.getLogger(CollectorDeviceValueService.class);
}
