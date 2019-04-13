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
import com.ifarm.bean.MultiControlTask;
import com.ifarm.enums.ControlSystemEnum;
import com.ifarm.enums.SystemReturnCodeEnum;
import com.ifarm.nosql.service.CombinationControlTaskService;
import com.ifarm.observer.UserControlData;
import com.ifarm.observer.WebSocketObserver;
import com.ifarm.redis.util.UserRedisUtil;
import com.ifarm.service.FarmControlSystemService;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.Constants;
import com.ifarm.util.ControlHandlerUtil;
import com.ifarm.util.ControlTaskUtil;
import com.ifarm.util.EventContext;
import com.ifarm.util.SystemResultEncapsulation;

@Component
public class ControlHandler extends TextWebSocketHandler implements
		WebSocketObserver {
	private static final Logger controlHandler_log = LoggerFactory
			.getLogger(ControlHandler.class);
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
	public void setCombinationControlTaskService(
			CombinationControlTaskService combinationControlTaskService) {
		this.combinationControlTaskService = combinationControlTaskService;
	}

	@Autowired
	public void setFarmControlSystemService(
			FarmControlSystemService farmControlSystemService) {
		this.farmControlSystemService = farmControlSystemService;
	}

	@PostConstruct
	public void initControlHandler() {
		if (CacheDataBase.userControlData == null) {
			CacheDataBase.userControlData = userControlData;
		}
		CacheDataBase.userControlData.registerObserver(
				Constants.userControlHandler, this);
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
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		// TODO Auto-generated method stub
		controlHandler_log.info(session + "建立连接");
		if (!(boolean) session.getAttributes().get("authState")) {
			session.sendMessage(new TextMessage("signature error"));
			// session.close();
		} else {
			String userId = (String) session.getAttributes().get("userId");
			List<String> list = userRedisUtil
					.getUserControlResultMessageCache(userId);
			for (int i = 0; i < list.size(); i++) {
				session.sendMessage(new TextMessage(list.get(i)));
			}
		}
	}

	public boolean authorityManager(JSONObject jsonObject, String userId) {
		if (!jsonObject.containsKey("systemId")
				|| !jsonObject.containsKey("farmId")) {
			return false;
		}
		Integer systemId = jsonObject.getInteger("systemId");
		Integer farmId = jsonObject.getInteger("farmId");
		return farmControlSystemService.farmControlSystemVerification(userId,
				farmId, systemId);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session,
			TextMessage message) throws Exception {
		String messgaeDetail = message.getPayload();
		controlHandler_log.info("receive:" + messgaeDetail);
		JSONObject messageJson = null;
		String userId = (String) session.getAttributes().get("userId");
		String sendMessage = null;
		EventContext eventContext = new EventContext();
		try {
			messageJson = JSONObject.parseObject(messgaeDetail);
			controlHandler(userId, session, messageJson, eventContext);
			if (eventContext.getEvent() != null) {
				sendMessage = (String) eventContext.getEvent();
			} else {
				sendMessage = SystemResultEncapsulation
						.fillResultCode(SystemReturnCodeEnum.SUCCESS);
			}
		} catch (JSONException je) {
			if (messgaeDetail.contains("[")) {
				try {
					JSONArray jsonArray = JSONArray.parseArray(message
							.getPayload());
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						controlHandler(userId, session, jsonObject,
								eventContext);
					}
					// 历史配置信息存在mongo
					combinationControlTaskService.saveCombinationControlTask(
							jsonArray, userId);
					sendMessage = SystemResultEncapsulation
							.fillResultCode(SystemReturnCodeEnum.SUCCESS);
				} catch (Exception e) {
					// TODO: handle exception
					controlHandler_log.error("组合任务失败", e);
				}
			} else {
				controlHandler_log.error("json parse error", je);
				sendMessage = SystemResultEncapsulation.fillErrorCode(je);
			}
		} catch (Exception e) {
			controlHandler_log.error("添加任务异常", e);
			sendMessage = SystemResultEncapsulation.fillErrorCode(e);
		}
		session.sendMessage(new TextMessage(sendMessage));
	}

	public void controlHandler(String userId, WebSocketSession session,
			JSONObject messageJson, EventContext eventContext) throws Exception {
		// 替换之前用户的session
		CacheDataBase.userControlData.registerObserver(userId, session);
		String command = messageJson.getString("command");
		if ("execute".equals(command)) {
			String controlType = messageJson.getString("controlType");
			ControlSystemEnum systemEnum = ControlSystemEnum.getValueByType(controlType);
			if (systemEnum == null) {
				throw new Exception("controlType no exist");
			}
			if (ControlSystemEnum.WATER_FERTILIZER_MEDICINDE.equals(systemEnum)) {
				MultiControlTask wfmControlTask = ControlTaskUtil
						.fromJson(messageJson);
				wfmControlTask.setUserId(userId);
				ControlHandlerUtil.mHandlerControlMessage(wfmControlTask,
						userId, farmControlSystemService, eventContext);
			} else {
				ControlTask controlTask = ControlTaskUtil
						.fromJsonTotask(messageJson);
				controlTask.setUserId(userId);
				ControlHandlerUtil.handlerControlMessage(controlTask, userId,
						farmControlSystemService, eventContext);
			}
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session,
			Throwable exception) throws Exception {
		CacheDataBase.userControlData.removeObserver(session);
		if ("java.io.EOFException".equals(exception.toString())) {
			return;
		}
		controlHandler_log.error("handleTransportError", exception);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus status) throws Exception {
		controlHandler_log.info(session + ":连接关闭");
		CacheDataBase.userControlData.removeObserver(session);
	}

}
