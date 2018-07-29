package com.ifarm.observer;

import org.springframework.web.socket.WebSocketSession;

public interface WebSocketObserver {

	public void update(String key, WebSocketSession session, String message);

}
