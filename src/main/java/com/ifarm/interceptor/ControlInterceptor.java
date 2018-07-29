package com.ifarm.interceptor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.ifarm.redis.util.UserRedisUtil;

@Component
public class ControlInterceptor extends HttpSessionHandshakeInterceptor {

	private static final Logger CONTROLINTERCEPTOR_LOGGER = LoggerFactory.getLogger(ControlInterceptor.class);
	
	@Autowired
	private UserRedisUtil userRedisUtil;


	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes)
			throws Exception {
		// TODO Auto-generated method stub
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			String userId = servletRequest.getServletRequest().getParameter("userId");
			String signature = servletRequest.getServletRequest().getParameter("signature");
			if (userId == null || signature == null) {
				return false;
			}
			CONTROLINTERCEPTOR_LOGGER.info("userId:" + userId);// 验证用户合法性
			servletRequest.getServletRequest().getSession().setAttribute("userId", userId);
			if (!signature.equals(userRedisUtil.getUserSignature(userId))) {
				servletRequest.getServletRequest().getSession().setAttribute("authState", false);
			} else {
				servletRequest.getServletRequest().getSession().setAttribute("authState", true);
			}
		}
		return super.beforeHandshake(request, response, wsHandler, attributes);
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
		// TODO Auto-generated method stub
		// System.out.println("控制系统后拦截");
		super.afterHandshake(request, response, wsHandler, ex);
	}

}
