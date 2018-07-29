package com.ifarm.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.ifarm.redis.util.CollectorDeviceValueRedisUtil;

@Component
public class SensorHandler extends TextWebSocketHandler {
	@Autowired
	private CollectorDeviceValueRedisUtil collectorDeviceValueRedisUtil;
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// TODO Auto-generated method stub
		String sensorId = message.getPayload();
		System.out.println(sensorId);
		String sendMessage = "";
		//sendMessage = CacheDataBase.collectorDeviceMainValueMap.get(sensorId).toString();
		TextMessage sendTextMessage = new TextMessage(sendMessage);
		session.sendMessage(sendTextMessage);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("websocket启动");
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(exception);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("连接关闭");
	}

}
