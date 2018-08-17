package com.ifarm.websocket;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.ControlTask;
import com.ifarm.bean.WFMControlTask;
import com.ifarm.constant.ControlTaskEnum;
import com.ifarm.nosql.service.CombinationControlTaskService;
import com.ifarm.observer.UserControlData;
import com.ifarm.observer.WebSocketObserver;
import com.ifarm.redis.util.UserRedisUtil;
import com.ifarm.service.FarmControlSystemService;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.Constants;
import com.ifarm.util.ControlHandlerUtil;
import com.ifarm.util.ControlTaskUtil;

@Component
public class ControlHandler extends TextWebSocketHandler implements WebSocketObserver {
	private static final Logger controlHandler_log = LoggerFactory.getLogger(ControlHandler.class);
	private FarmControlSystemService farmControlSystemService;
	private CombinationControlTaskService combinationControlTaskService;

	@Autowired
	private UserControlData userControlData;

	@Autowired
	private UserRedisUtil userRedisUtil;

	public CombinationControlTaskService getCombinationControlTaskService() {
		return combinationControlTaskService;
	}

	@Autowired
	public void setCombinationControlTaskService(CombinationControlTaskService combinationControlTaskService) {
		this.combinationControlTaskService = combinationControlTaskService;
	}

	@Autowired
	public void setFarmControlSystemService(FarmControlSystemService farmControlSystemService) {
		this.farmControlSystemService = farmControlSystemService;
	}

	@PostConstruct
	public void initControlHandler() {
		if (CacheDataBase.userControlData == null) {
			CacheDataBase.userControlData = userControlData;
		}
		CacheDataBase.userControlData.registerObserver(Constants.userControlHandler, this);
	}

	public FarmControlSystemService getFarmControlSystemService() {
		return farmControlSystemService;
	}

	@Override
	public void update(String key, WebSocketSession session, String message) {
		// TODO Auto-generated method stub
		try {
			controlHandler_log.info("发送给" + key + "的message：" + message);
			session.sendMessage(new TextMessage(message));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// TODO Auto-generated method stub
		controlHandler_log.info(session + "建立连接");
		if (!(boolean) session.getAttributes().get("authState")) {
			session.sendMessage(new TextMessage("signature error"));
			// session.close();
		} else {
			String userId = (String) session.getAttributes().get("userId");
			List<String> list = userRedisUtil.getUserControlResultMessageCache(userId);
			for (int i = 0; i < list.size(); i++) {
				session.sendMessage(new TextMessage(list.get(i)));
			}
		}
	}

	public boolean authorityManager(JSONObject jsonObject, String userId) {
		if (!jsonObject.containsKey("systemId") || !jsonObject.containsKey("farmId")) {
			return false;
		}
		Integer systemId = jsonObject.getInteger("systemId");
		Integer farmId = jsonObject.getInteger("farmId");
		return farmControlSystemService.farmControlSystemVerification(userId, farmId, systemId);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String messgaeDetail = message.getPayload();
		controlHandler_log.info("receive:" + messgaeDetail);
		JSONObject messageJson = null;
		JSONObject resultJson = new JSONObject();
		String userId = (String) session.getAttributes().get("userId");
		try {
			messageJson = JSONObject.parseObject(messgaeDetail);
			// 权限验证
			/*
			 * boolean flag = authorityManager(messageJson, userId); if (!flag)
			 * { session.sendMessage(new TextMessage("no_auth")); return; }
			 */
			resultJson = controlHandler(userId, session, messageJson);
		} catch (JSONException je) {
			if (messgaeDetail.contains("[")) {
				JSONArray resultArray = new JSONArray();
				try {
					JSONArray jsonArray = JSONArray.parseArray(message.getPayload());
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						resultJson = controlHandler(userId, session, jsonObject);
						resultArray.add(resultJson);
					}
					// 历史配置信息存在mongo
					combinationControlTaskService.saveCombinationControlTask(jsonArray, userId);
				} catch (Exception e) {
					// TODO: handle exception
					controlHandler_log.error(e.toString());
					resultJson.put("response", ControlTaskEnum.ERROR);
					resultArray.add(resultJson);
				}
				session.sendMessage(new TextMessage(resultArray.toString()));
				return;
			} else {
				controlHandler_log.error(je.toString());
				resultJson.put("response", ControlTaskEnum.ERROR);
			}
		} catch (Exception e) {
			controlHandler_log.error(e.toString());
			resultJson.put("response", ControlTaskEnum.ERROR);
		}
		session.sendMessage(new TextMessage(resultJson.toString()));
	}

	public JSONObject controlHandler(String userId, WebSocketSession session, JSONObject messageJson) throws Exception {
		// 替换之前用户的session
		JSONObject resultJson = new JSONObject();
		CacheDataBase.userControlData.registerObserver(userId, session);
		String command = messageJson.getString("command");
		if ("execute".equals(command)) {
			try {
				String controlType = messageJson.getString("controlType");
				if ("wfm".equals(controlType)) {
					WFMControlTask wfmControlTask = ControlTaskUtil.fromJson(messageJson);
					wfmControlTask.setUserId(userId);
					resultJson = ControlHandlerUtil.wfmHandlerControlMessage(wfmControlTask, userId, farmControlSystemService);
				} else {
					ControlTask controlTask = ControlTaskUtil.fromJsonTotask(messageJson);
					controlTask.setUserId(userId);
					resultJson = ControlHandlerUtil.handlerControlMessage(controlTask, userId, farmControlSystemService);
				}
			} catch (Exception e) {
				controlHandler_log.error("control handler", e);
				resultJson.put("response", ControlTaskEnum.ERROR);
			}
		}
		return resultJson;
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		CacheDataBase.userControlData.removeObserver(session);
		if (exception.getMessage().equals("java.io.EOFException")) {
			controlHandler_log.error("java.io.EOFException");
		}
		controlHandler_log.error("handleTransportError", exception);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		controlHandler_log.info(session + ":连接关闭");
		CacheDataBase.userControlData.removeObserver(session);
	}

}
