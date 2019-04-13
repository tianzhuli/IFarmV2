package com.ifarm.processor;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.ifarm.constant.SystemConfigCache;
import com.ifarm.mina.CollectServer;
import com.ifarm.mina.ControlServer;
import com.ifarm.netty.NettyServer;
import com.ifarm.nosql.dao.InitRedisDao;
import com.ifarm.nosql.service.ControlSystemStateService;
import com.ifarm.observer.UserControlData;
import com.ifarm.redis.util.ControlCommandRedisHelper;
import com.ifarm.redis.util.ControlTaskRedisHelper;
import com.ifarm.redis.util.WfmControlTaskRedisHelper;
import com.ifarm.service.CollectorDeviceValueService;
import com.ifarm.service.CollectorValueService;
import com.ifarm.service.ControlTaskService;
import com.ifarm.service.FarmControlSystemService;
import com.ifarm.service.FarmControlSystemWFMService;
import com.ifarm.service.FarmControlUnitService;
import com.ifarm.service.MultiControlTaskService;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.ControlCacheCollection;

public class InitializeProcessor implements
		ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(InitializeProcessor.class);

	@Autowired
	private CollectorValueService collectorValuesService;

	@Autowired
	private CollectorDeviceValueService sensorValuesService;

	@Autowired
	private InitRedisDao initRedisDao;

	@Autowired
	private ControlTaskService taskService;

	@Autowired
	private MultiControlTaskService wService;

	@Autowired
	private ControlCacheCollection controlCacheCollection;

	@Autowired
	private ControlSystemStateService controlSystemStateService;

	@Autowired
	private CollectorDeviceValueService cValueService;

	@Autowired
	private FarmControlSystemService farmControlSystemService;

	@Autowired
	private FarmControlSystemWFMService farmControlSystemWFMService;

	@Autowired
	private FarmControlUnitService farmControlUnitService;

	@Autowired
	private UserControlData userControlData;

	@Autowired
	private CollectServer collectServer;

	@Autowired
	private ControlServer controlServer;

	@Autowired
	private NettyServer nettyServer;

	@Autowired
	private ControlCommandRedisHelper commandRedisHelper;

	@Autowired
	private ControlTaskRedisHelper controlTaskRedisHelper;

	@Autowired
	private WfmControlTaskRedisHelper wfmControlTaskRedisHelper;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getApplicationContext().getParent() == null) {
			try {
				CacheDataBase.cacheCollection = controlCacheCollection;
				CacheDataBase.initialize();// 静态的数据库初始化
				init();// 静态service初始化
				if (!initRedisDao.redisConnect()) {
					LOGGER.info("redis连接异常");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOGGER.error("初始化系统异常", e);
			}
		}
	}

	public void init() {
		try {
			collectServer.start();
			if ((boolean) CacheDataBase.systemConfigCacheMap
					.get(SystemConfigCache.SWITCH_CHANNEL_TYPE)) {
				CacheDataBase.controlService.execute(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						nettyServer.init(CacheDataBase.controlPort);
					}
				});
			} else {
				controlServer.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error("init server error", e);
		}
		CacheDataBase.collectorDeviceValueService = sensorValuesService;
		CacheDataBase.taskService = taskService;
		CacheDataBase.initRedisDao = initRedisDao;
		CacheDataBase.wTaskService = wService;
		CacheDataBase.farmControlSystemService = farmControlSystemService;
		CacheDataBase.farmControlSystemWFMService = farmControlSystemWFMService;
		CacheDataBase.farmControlUnitService = farmControlUnitService;
		if (CacheDataBase.userControlData == null) {
			CacheDataBase.userControlData = userControlData;
		}
		CacheDataBase.commandRedisHelper = commandRedisHelper;
		CacheDataBase.controlTaskRedisHelper = controlTaskRedisHelper;
		CacheDataBase.wfmControlTaskRedisHelper = wfmControlTaskRedisHelper;
	}
}
