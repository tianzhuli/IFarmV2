package com.ifarm.mina;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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
import com.ifarm.util.ByteUtil;
import com.ifarm.util.CacheDataBase;

@Component
public class CollectHandler extends IoHandlerAdapter {

	private static final Logger HANDLER_LOG = LoggerFactory
			.getLogger("MINA—DIGEST");
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
	
	private Random random = new Random();

	private Map<Integer, BaseCollectorDeviceParse> deviceParseMap = new HashMap<Integer, BaseCollectorDeviceParse>();

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionCreated(session);
		InetSocketAddress socketAddress = (InetSocketAddress) session.getRemoteAddress();
		InetAddress address = socketAddress.getAddress();
		HANDLER_LOG.info("client ip:" + address.getHostAddress());

	}

	public Map<Integer, BaseCollectorDeviceParse> getDeviceParseMap() {
		return deviceParseMap;
	}

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

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionOpened(session);
		HANDLER_LOG.info(this + "---" + session + "------" + "session open");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionClosed(session);
		HANDLER_LOG.info("session close");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		// TODO Auto-generated method stub
		session.closeNow();
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(session, cause);
		HANDLER_LOG.error("", cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		// 兼容老版本
		if (CacheDataBase.collectorVersion == 1) {
			if ((int) session.getAttribute("num") == 0) {
				session.closeOnFlush();
			}
			return;
		}
		byte[] arr = (byte[]) message;
		if (arr.length > 50 && ByteUtil.checkByteEqual(arr, "00000000", 8, 4)) { // 心跳包长度较长
			Long collectorId = CommonUtil.parserHeartMessage(arr);
			session.setAttribute("collectorId", collectorId);
			CollectorCommand command = collectorDeviceCommandRedisHelper.getRedisListValue(collectorId.toString());
			if (command != null) {
				// 随机参数避免设备初始化多个心跳包同时上来
				int factor = random.nextInt(60) + 5;
				TimeUnit.SECONDS.sleep(factor);
			}
			while (command != null) {
				session.write(command.commandToByte());
				command = collectorDeviceCommandRedisHelper.getRedisListValue(collectorId.toString());
				// 指令间隔
				TimeUnit.SECONDS.sleep(5 * CacheDataBase.commandDelayTime);
			}
		} else {
			CommonUtil.messageDigest(arr, "collecotor receive");
			Long collectorId = (Long) session.getAttribute("collectorId");
			if (collectorId != null) {
				int deviceType = arr[3];
				BaseCollectorDeviceParse parse = this.deviceParseMap.get(deviceType);
				parse.parse(arr, collectorId);
			} else {
				HANDLER_LOG.info("-----集中器不在线-----");
			}
		}

	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		byte[] arr = (byte[]) message;
		CommonUtil.messageDigest(arr, "collecotor send");
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.inputClosed(session);
		HANDLER_LOG.info("input closed");
	}

}
