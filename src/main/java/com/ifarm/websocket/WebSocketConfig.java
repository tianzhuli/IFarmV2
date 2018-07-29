package com.ifarm.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.ifarm.interceptor.ControlInterceptor;
import com.ifarm.interceptor.HandshakeInterceptor;
import com.ifarm.service.FarmControlSystemService;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	@Autowired
	private FarmControlSystemService farmControlService;

	@Autowired
	private ControlHandler controlHandler;
	
	@Autowired
	private ControlInterceptor controlInterceptor;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfig.class);
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// TODO Auto-generated method stub
		LOGGER.info("启动websocket");
		registry.addHandler(new SensorHandler(), "/sensorMainValues").addInterceptors(new HandshakeInterceptor());
		registry.addHandler(new CollectorHandler(), "/collectorValues").addInterceptors(new HandshakeInterceptor());
		registry.addHandler(controlHandler, "/controlServer").addInterceptors(controlInterceptor);
	}

}
