package com.ifarm.observer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ifarm.constant.SystemConfigCache;
import com.ifarm.mina.IoControlHandler;
import com.ifarm.netty.CustomServerHandler;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.Constants;

public class IoControlData extends IoSessionSubjectDecorator {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(IoControlData.class);
	private Map<Long, IoSession> map = new ConcurrentHashMap<Long, IoSession>();
	private Map<String, IoSessionObserver> constant = new HashMap<String, IoSessionObserver>();

	private CustomServerHandler handler;

	public IoControlData(Subject subject) {
		super(subject);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void registerObserver(Long key, IoSession o) {
		map.put(key, o);
	}

	@Override
	public void registerObserver(String key, IoSessionObserver o) {
		// TODO Auto-generated method stub
		constant.put(key, o);
	}

	public void registerObserver(CustomServerHandler hanler) {
		// TODO Auto-generated method stub
		this.handler = hanler;
	}

	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	@Override
	public void removeObserver(IoControlHandler ioHandler) {
		// TODO Auto-generated method stub
		if (constant.containsKey(Constants.ioControlHandler)) {
			constant.remove(Constants.ioControlHandler);
		}
	}

	@Override
	public void removeObserver(IoSession session) {
		// TODO Auto-generated method stub
		if (map.containsValue(session)) {
			for (Long key : map.keySet()) {
				if (map.get(key) == session) {
					map.remove(key);
				}
			}
		}
	}

	@Override
	public void notifyObservers(Long key) throws Exception {
		// TODO Auto-generated method stub
		if ((boolean) CacheDataBase.systemConfigCacheMap
				.get(SystemConfigCache.SWITCH_CHANNEL_TYPE)) {
			if (CacheDataBase.channelHandlerContextMap.containsKey(key)) {
				ChannelHandlerContext ctx = CacheDataBase.channelHandlerContextMap
						.get(key);
				if (handler != null) {
					handler.notifySend(ctx);
				}
			} else {
				LOGGER.info("底层集中器" + key + "已离线");
			}
		}
		if (map.containsKey(key)) {
			IoControlHandler observer = (IoControlHandler) constant
					.get(Constants.ioControlHandler);
			IoSession session = map.get(key);
			observer.update(key, session);
		} else {
			LOGGER.info("底层集中器" + key + "已离线");
		}
	}
}
