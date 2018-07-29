package com.ifarm.observer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ifarm.mina.ControlByteIoHandler;
import com.ifarm.util.Constants;

public class IoControlData extends IoSessionSubjectDecorator {
	private static final Logger LOGGER = LoggerFactory.getLogger(IoControlData.class);
	private Map<Long, IoSession> map = new ConcurrentHashMap<Long, IoSession>();
	private Map<String, IoSessionObserver> constant = new HashMap<String, IoSessionObserver>();

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

	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	@Override
	public void removeObserver(ControlByteIoHandler ioHandler) {
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
		if (map.containsKey(key)) {
			ControlByteIoHandler observer = (ControlByteIoHandler) constant.get(Constants.ioControlHandler);
			IoSession session = map.get(key);
			observer.update(key, session);
		} else {
			LOGGER.info("底层集中器" + key + "已离线");
		}
	}

}
