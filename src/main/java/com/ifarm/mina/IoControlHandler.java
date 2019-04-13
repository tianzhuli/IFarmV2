package com.ifarm.mina;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ifarm.bean.CollectorCommand;
import com.ifarm.bean.ControlCommand;
import com.ifarm.observer.IoSessionObserver;
import com.ifarm.parse.AirTemHumDeviceParse;
import com.ifarm.parse.BaseCollectorDeviceParse;
import com.ifarm.parse.Co2DeviceParse;
import com.ifarm.parse.FiveWithOneCollectorDeviceParse;
import com.ifarm.parse.IlluminationDeviceParse;
import com.ifarm.parse.OxygenDeviceParse;
import com.ifarm.parse.SevenWithOneCollectorDeviceParse;
import com.ifarm.parse.SoilTemHumDeviceParse;
import com.ifarm.parse.WeatherMonitorDeviceParse;
import com.ifarm.redis.util.CollectorDeviceCommandRedisHelper;
import com.ifarm.redis.util.ControlCommandRedisHelper;
import com.ifarm.service.CommandConvertService;
import com.ifarm.util.ByteUtil;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.Constants;
import com.ifarm.util.ControlHandlerUtil;
import com.ifarm.util.ConvertData;

/**
 * 合并采集和控制的处理逻辑
 * 
 * @author qican
 * 
 */
@SuppressWarnings("unchecked")
@Component
public class IoControlHandler extends IoHandlerAdapter implements
		IoSessionObserver {

	private static final Logger inHandler_log = LoggerFactory
			.getLogger("MINA—DIGEST");

	private static final Logger LOGGER = LoggerFactory
			.getLogger(IoControlHandler.class);

	ConvertData convertData = new ConvertData();
	@Autowired
	private ControlCommandRedisHelper commandRedisHelper;

	@Autowired
	private CommandConvertService commandConvertService;

	@Autowired
	private CollectorDeviceCommandRedisHelper collectorDeviceCommandRedisHelper;
	@Autowired
	private AirTemHumDeviceParse airTemHumDeviceParse;
	@Autowired
	private Co2DeviceParse co2DeviceParse;
	@Autowired
	private FiveWithOneCollectorDeviceParse fiveWithOneCollectorDeviceParse;
	@Autowired
	private IlluminationDeviceParse illuminationDeviceParse;
	@Autowired
	private OxygenDeviceParse oxygenDeviceParse;
	@Autowired
	private SevenWithOneCollectorDeviceParse sevenWithOneCollectorDeviceParse;
	@Autowired
	private SoilTemHumDeviceParse soilTemHumDeviceParse;
	@Autowired
	private WeatherMonitorDeviceParse weatherMonitorDeviceParse;

	private Map<Integer, BaseCollectorDeviceParse> deviceParseMap = new HashMap<Integer, BaseCollectorDeviceParse>();

	@PostConstruct
	public void setDeviceParseMap() {
		this.deviceParseMap.put(6, fiveWithOneCollectorDeviceParse);
		this.deviceParseMap.put(7, illuminationDeviceParse);
		this.deviceParseMap.put(8, airTemHumDeviceParse);
		this.deviceParseMap.put(9, soilTemHumDeviceParse);
		this.deviceParseMap.put(10, oxygenDeviceParse);
		this.deviceParseMap.put(11, co2DeviceParse);
		this.deviceParseMap.put(12, weatherMonitorDeviceParse);
		this.deviceParseMap.put(15, sevenWithOneCollectorDeviceParse);
	}

	public IoControlHandler() {
		CacheDataBase.ioControlData.registerObserver(
				Constants.ioControlHandler, this);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionCreated(session);
		InetSocketAddress socketAddress = (InetSocketAddress) session
				.getRemoteAddress();
		InetAddress address = socketAddress.getAddress();
		inHandler_log.info("client ip:" + address.getHostAddress());
		LinkedList<ControlCommand> linkedList = new LinkedList<ControlCommand>();
		session.setAttribute("controlCommands", linkedList);

	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionOpened(session);
		inHandler_log.info("io session:" + session);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionClosed(session);
		CacheDataBase.ioControlData.removeObserver(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		// TODO Auto-generated method stub
		super.sessionIdle(session, status);
		session.closeNow();
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(session, cause);
		CacheDataBase.ioControlData.removeObserver(session);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// 兼容老版本
		if (CacheDataBase.collectorVersion == 1) {
			if ((int) session.getAttribute("num") == 0) {
				session.closeOnFlush();
			}
			return;
		}
		byte[] arr = (byte[]) message;
		LinkedList<ControlCommand> commandsLinkedList = (LinkedList<ControlCommand>) session
				.getAttribute("controlCommands");
		if (arr.length > 50 && ByteUtil.checkByteEqual(arr, "00000000", 8, 4)) { // 心跳包长度较长
			Long collectorId = CommonUtil.parserHeartMessage(arr);
			session.setAttribute("collectorId", collectorId);
			CacheDataBase.ioControlData.registerObserver(collectorId, session);
			ControlCommand command = commandRedisHelper
					.getRedisListValue(collectorId.toString());
			while (command != null) {
				if (collectorDeviceIsIdle(collectorId)) {
					sendMessage(session,
							commandConvertService.commandToByte(command));
					// session.write(commandConvertService.commandToByte(command));
					commandsLinkedList.add(command);
					command = commandRedisHelper.getRedisListValue(collectorId
							.toString());
				}
				// 时间间歇
				TimeUnit.SECONDS.sleep(CacheDataBase.commandDelayTime);
			}
			// 采集的指令
			CollectorCommand collectCommand = collectorDeviceCommandRedisHelper
					.getRedisListValue(collectorId.toString());
			while (collectCommand != null) {
				if (collectorDeviceIsIdle(collectorId)) {
					sendMessage(session, collectCommand.commandToByte());
					// session.write(collectCommand.commandToByte());
					collectCommand = collectorDeviceCommandRedisHelper
							.getRedisListValue(collectorId.toString());
				}
				// 指令间隔
				TimeUnit.SECONDS.sleep(CacheDataBase.commandDelayTime);
			}
		} else { // 设备相应回复
			CommonUtil.messageDigest(arr, "collecotor receive");
			Long collectorId = (Long) session.getAttribute("collectorId");
			if (collectorId != null) {
				int deviceType = arr[3];
				BaseCollectorDeviceParse parse = this.deviceParseMap
						.get(deviceType);
				// 判断是采集还是控制的相应，主要是通过设备类型
				if (parse != null) {
					parse.parse(arr, collectorId);
				} else if (commandsLinkedList.size() > 0) {
					// 控制命令的响应
					ControlCommand command = macthCommand(commandsLinkedList,
							arr);
					if (command == null) {
						inHandler_log.error("{},匹配不成功", arr);
						return;
					}
					boolean flag = false;
					if (arr.length <= 14) { // 长度判断设备操作失败，后续还需要确定
						flag = false;
					} else {
						flag = true;
					}
					ControlHandlerUtil
							.controlDeviceReturnMessage(command, flag);
				}
			}
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		byte[] arr = (byte[]) message;
		CommonUtil.messageDigest(arr, "send");
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.inputClosed(session);
	}

	public void sendMessage(IoSession session, byte[] arr) {
		session.write(arr);
	}

	@Override
	public void update(Long collectorId, IoSession ioSession) throws Exception {
		// TODO Auto-generated method stub
		LinkedList<ControlCommand> commandsLinkedList = (LinkedList<ControlCommand>) ioSession
				.getAttribute("controlCommands");
		ControlCommand command = commandRedisHelper
				.getRedisListValue(collectorId.toString());
		while (command != null) {
			if (collectorDeviceIsIdle(collectorId)) {
				sendMessage(ioSession,
						commandConvertService.commandToByte(command));
				// ioSession.write(commandConvertService.commandToByte(command));
				// // 如果抛出异常下面的就不执行，该指令下次依旧可以发送下去
				commandsLinkedList.add(command);
				command = commandRedisHelper.getRedisListValue(collectorId
						.toString());
			}
			// 指令集间歇
			TimeUnit.SECONDS.sleep(CacheDataBase.commandDelayTime);
		}
	}

	private ControlCommand macthCommand(LinkedList<ControlCommand> commands,
			byte[] cmd) {
		for (int i = 0; i < commands.size(); i++) {
			ControlCommand command = commands.get(i);
			if (mathchCommand(command, cmd)) {
				commands.remove(command);
				return command;
			}
		}
		return null;
	}

	private boolean mathchCommand(ControlCommand command, byte[] cmd) {
		byte[] commandBytes = commandConvertService.commandToByte(command);
		// 设备端和功能标识总共9位
		for (int i = 0, j = 3; i < 9; i++, j++) {
			if (commandBytes[i] != cmd[j]) {
				return false;
			}
		}
		return true;
	}

	// 集中器设备是否空闲
	private boolean collectorDeviceIsIdle(Long collectorId) {
		long currentTime = System.currentTimeMillis() / 1000;
		boolean res = false;
		if (!CacheDataBase.collectorLastCommandTimeMap.containsKey(collectorId)) {
			CacheDataBase.collectorLastCommandTimeMap.put(collectorId,
					currentTime);
			res = true;
		} else {
			Long lastCommandTime = CacheDataBase.collectorLastCommandTimeMap
					.get(collectorId);
			if (currentTime - lastCommandTime >= CacheDataBase.commandDelayTime) {
				CacheDataBase.collectorLastCommandTimeMap.put(collectorId,
						currentTime);
				res = true;
			}
		}
		LOGGER.info("collecotor={} is {}", collectorId, res);
		return res;
	}
}
