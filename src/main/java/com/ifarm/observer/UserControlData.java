package com.ifarm.observer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.ifarm.redis.util.UserRedisUtil;
import com.ifarm.util.Constants;
import com.ifarm.websocket.ControlHandler;

@Component
public class UserControlData extends WebSocketSubjectDecorate {
	private Map<String, WebSocketSession> map = new ConcurrentHashMap<String, WebSocketSession>();
	private Map<String, WebSocketObserver> constant = new HashMap<String, WebSocketObserver>();
	private static final Logger LOGGER = LoggerFactory.getLogger(UserControlData.class); 
	
	@Autowired
	private UserRedisUtil userRedisUtil;
	
	public UserControlData(Subject subject) {
		super(subject);
		// TODO Auto-generated constructor stub
	}
	
	public UserControlData() {
		
	}	
	
	@Override
	public void registerObserver(String key, WebSocketObserver o) {
		// TODO Auto-generated method stub
		constant.put(key, o);
	}

	@Override
	public void registerObserver(String key, WebSocketSession o) {
		// TODO Auto-generated method stub
		map.put(key, o);
	}

	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	@Override
	public void removeObserver(WebSocketSession session) {
		// TODO Auto-generated method stub
		if (map.containsValue(session)) {
			for (String key : map.keySet()) {
				if (map.get(key) == session) {
					map.remove(key);
				}
			}
		}
	}

	@Override
	public void removeObserver(WebSocketObserver o) {
		// TODO Auto-generated method stub
		if (constant.containsKey(Constants.userControlHandler)) {
			constant.remove(Constants.userControlHandler);
		}
	}

	@Override
	public void notifyObservers(String key, String message) {
		// TODO Auto-generated method stub
		if (map.containsKey(key)) {
			ControlHandler observer = (ControlHandler) constant.get(Constants.userControlHandler);
			WebSocketSession session = map.get(key);
			observer.update(key, session, message);
		} else {
			LOGGER.info("用户" + key + "不在线");
			// 添加到缓存
			/*if (!CacheDataBase.userControlResultMessageCache.containsKey(key)) {
				CacheDataBase.userControlResultMessageCache.put(key, new LinkedBlockingQueue<String>());
			}
			CacheDataBase.userControlResultMessageCache.get(key).add(message);*/
			userRedisUtil.setUserControlResultMessageCache(key, message);
		}
	}

	public Map<String, WebSocketSession> getMap() {
		return map;
	}

	public void setMap(Map<String, WebSocketSession> map) {
		this.map = map;
	}

	public Map<String, WebSocketObserver> getConstant() {
		return constant;
	}

	public void setConstant(Map<String, WebSocketObserver> constant) {
		this.constant = constant;
	}
}
