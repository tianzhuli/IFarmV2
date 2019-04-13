package com.ifarm.netty;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ifarm.bean.CollectorCommand;
import com.ifarm.bean.ControlCommand;
import com.ifarm.mina.CommonUtil;
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
import com.ifarm.util.ByteConvert;
import com.ifarm.util.ByteUtil;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.ControlHandlerUtil;
import com.ifarm.util.ConvertData;

@SuppressWarnings("deprecation")
@Component
@Sharable
public class CustomServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger NETTY_DIGEST_LOG = LoggerFactory
			.getLogger("NETTY—DIGEST");

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomServerHandler.class);

	private AttributeKey<LinkedList<ControlCommand>> controlCommandAttributeKey = AttributeKey
			.valueOf("controlCommands");

	private AttributeKey<Long> collecotrAttributeKey = AttributeKey
			.valueOf("controlCommands");
	ConvertData convertData = new ConvertData();
	@Autowired
	private CommandConvertService commandConvertService;
	@Autowired
	private ControlCommandRedisHelper commandRedisHelper;
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
	public void initServer() {
		CacheDataBase.ioControlData.registerObserver(this);
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
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// TODO Auto-generated method stub
		NETTY_DIGEST_LOG.info("receive:" + msg.toString());
		ByteBuf in = (ByteBuf) msg;
		while (in.isReadable()) {
			byte[] arr = new byte[in.readableBytes()];
			in.readBytes(arr);
			Attribute<LinkedList<ControlCommand>> commandsAttribute = ctx
					.attr(controlCommandAttributeKey);
			if (arr.length > 50
					&& ByteUtil.checkByteEqual(arr, "00000000", 8, 4)) { // 心跳包长度较长
				Long collectorId = CommonUtil.parserHeartMessage(arr);
				ctx.attr(collecotrAttributeKey).set(collectorId);
				CacheDataBase.channelHandlerContextMap.put(collectorId, ctx);
				ControlCommand command = commandRedisHelper
						.getRedisListValue(collectorId.toString());
				while (command != null) {
					sendMessage(ctx,
							commandConvertService.commandToByte(command));
					commandsAttribute.get().add(command);
					command = commandRedisHelper.getRedisListValue(collectorId
							.toString());
					// 时间间歇
					TimeUnit.SECONDS.sleep(CacheDataBase.commandDelayTime);
				}
				// 采集的指令
				CollectorCommand collectCommand = collectorDeviceCommandRedisHelper
						.getRedisListValue(collectorId.toString());
				while (collectCommand != null) {
					sendMessage(ctx, collectCommand.commandToByte());
					collectCommand = collectorDeviceCommandRedisHelper
							.getRedisListValue(collectorId.toString());
					// 指令间隔
					TimeUnit.SECONDS.sleep(CacheDataBase.commandDelayTime);
				}
			} else { // 设备相应回复
				CommonUtil.messageDigest(arr, "collecotor receive");
				Long collectorId = ctx.attr(collecotrAttributeKey).get();
				if (collectorId != null) {
					int deviceType = arr[3];
					BaseCollectorDeviceParse parse = this.deviceParseMap
							.get(deviceType);
					// 判断是采集还是控制的相应，主要是通过设备类型
					if (parse != null) {
						parse.parse(arr, collectorId);
					} else if (commandsAttribute.get().size() > 0) {
						// 控制命令的响应
						ControlCommand command = macthCommand(
								commandsAttribute.get(), arr);
						if (command == null) {
							LOGGER.error("{},匹配不成功", arr);
							return;
						}
						boolean flag = false;
						if (arr.length <= 14) { // 长度判断设备操作失败，后续还需要确定
							flag = false;
						} else {
							flag = true;
						}
						ControlHandlerUtil.controlDeviceReturnMessage(command,
								flag);
					}
				}
			}
			/*
			 * outBuf = ctx.alloc().buffer(1); outBuf.writeByte(0x10);
			 * ctx.writeAndFlush(outBuf);
			 */
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		LOGGER.info("-------------连接关闭-------------", ctx);
		CacheDataBase.channelHandlerContextMap.remove(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		LinkedList<ControlCommand> linkedList = new LinkedList<ControlCommand>();
		ctx.attr(controlCommandAttributeKey).set(linkedList);
		LOGGER.info("-------------连接开启-------------", ctx);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		LOGGER.error("netty error:", cause);
		CacheDataBase.channelHandlerContextMap.remove(ctx);
		ctx.close();
	}

	public void sendMessage(ChannelHandlerContext ctx, byte[] arr) {
		byte[] outbytes = convertCommandByte(arr);
		ByteBuf outBuf = ctx.alloc().buffer(outbytes.length);
		outBuf.writeBytes(outbytes);
		ctx.writeAndFlush(outBuf);
	}

	public byte[] convertCommandByte(byte[] arr) {
		byte[] lens = ByteConvert.intToByte2(arr.length + 5); // 需要增加帧多、帧尾和校验
		byte[] outBytes = new byte[arr.length + 5];
		outBytes[0] = 0x68;
		outBytes[1] = lens[0];
		outBytes[2] = lens[1];
		for (int i = 0; i < arr.length; i++) {
			outBytes[i + 3] = arr[i];
		}
		outBytes[arr.length + 3] = ByteConvert.checekByte(outBytes, 0,
				outBytes.length - 3);
		outBytes[arr.length + 4] = 0x16;
		return outBytes;
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

	public void notifySend(ChannelHandlerContext ctx) throws Exception {
		Long collectorId = ctx.attr(collecotrAttributeKey).get();
		Attribute<LinkedList<ControlCommand>> commandsAttribute = ctx
				.attr(controlCommandAttributeKey);
		ControlCommand command = commandRedisHelper
				.getRedisListValue(collectorId.toString());
		while (command != null) {
			sendMessage(ctx, commandConvertService.commandToByte(command));
			commandsAttribute.get().add(command);
			command = commandRedisHelper.getRedisListValue(collectorId
					.toString());
			// 时间间歇
			TimeUnit.SECONDS.sleep(CacheDataBase.commandDelayTime);
		}
	}

}
