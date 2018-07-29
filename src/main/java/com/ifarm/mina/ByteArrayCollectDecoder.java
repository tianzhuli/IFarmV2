package com.ifarm.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ifarm.bean.CollectorDeviceValue;
import com.ifarm.bean.DeviceValueBase;
import com.ifarm.service.CollectorDeviceValueService;
import com.ifarm.service.CollectorValueService;
import com.ifarm.util.ConvertData;

@Component
public class ByteArrayCollectDecoder extends CumulativeProtocolDecoder {
	private static final Logger DECORE_LOG = LoggerFactory.getLogger(ByteArrayCollectDecoder.class);
	private ConvertData convertData = new ConvertData();
	int offset = 2; // 底层的bug，服务器解决
	int headerLen = 18;
	private static final int dataSize = 10;
	@Autowired
	private CollectorValueService collectorValuesService;
	@Autowired
	private CollectorDeviceValueService cValueService;
	
	public void byteLogRecord(byte[] arr, String name) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			builder.append(byteToHex(arr[i])).append(" ");
		}
		DECORE_LOG.info("{} : {}", name, builder);
	}

	public String byteToHex(byte b) {
		String hex = Integer.toHexString(b & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		return hex.toUpperCase();
	}

	public void byteLogRecord(byte[] arr1, byte[] arr2, byte[] arr3, String name) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < arr1.length; i++) {
			builder.append(byteToHex(arr1[i])).append(" ");
		}
		for (int i = 0; i < arr2.length; i++) {
			builder.append(byteToHex(arr2[i])).append(" ");
		}
		for (int i = 0; i < arr3.length; i++) {
			builder.append(byteToHex(arr3[i])).append(" ");
		}
		DECORE_LOG.info("{} : {}", name, builder);
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		Object isHeader = session.getAttribute("state");
		if (isHeader == null) {
			if (in.remaining() >= headerLen) { // 包头最少14个字节,长度不需要知道
				byte[] headerBytes = new byte[headerLen];
				in.get(headerBytes);
				byteLogRecord(headerBytes, "header");
				if (headerBytes[0] != 0x68 || headerBytes[3] != 0x68 || headerBytes[headerBytes.length - 1] != 0x16) {
					DECORE_LOG.info("------数据包错误------");
					return false;
				}
				session.setAttribute("state", new Object());
				int num = convertData.getdataType3(headerBytes, 8);
				int validNum = convertData.getdataType3(headerBytes, 10);
				DECORE_LOG.info("sum device is {} and valid data is {}", num, validNum);
				// 从10开始解析，表示在线有效数量，解析表头，后面改成了解析所有设备，之后通过设备的状态位去判断是否有效
				collectorValuesService.saveCollectorValues(headerBytes, 12);
				// 判断缓存里面是否有指令下发
				session.setAttribute("num", num);
				resolveCollectorData(session, in, num, out);
			}
		} else {
			int num = (int) session.getAttribute("num");
			resolveCollectorData(session, in, num, out);
		}
		// 处理成功，让父类进行接收下个包
		return false;
	}

	public void resolveCollectorData(IoSession session, IoBuffer in, int num, ProtocolDecoderOutput out) {
		while (in.remaining() >= 8 && num > 0) {
			in.mark();
			byte[] numberIdArray = new byte[4];
			in.get(numberIdArray);
			byte[] sizeArray = new byte[4]; // 4个字节，其中包括信号强度2个字节，一个采集状态一个字节，一个长度一个字节
			in.get(sizeArray);
			long id = convertData.byteToConvertLong(numberIdArray, 0, 4);
			int valid = convertData.getdataType1(sizeArray, 2);
			int size = convertData.getdataType1(sizeArray, 3);
			size += offset; // 设备的BUG
			DECORE_LOG.info("collector id is {} and size is {}", id, size);
			if (in.remaining() >= size) {
				byte[] data = new byte[size];
				in.get(data);
				// 得到一组数据
				// 根据不同的设备类型应该要创建不同的对象
				byteLogRecord(numberIdArray, sizeArray, data, "data");
				if (size != offset + dataSize) {
					if (size == offset) {
						DECORE_LOG.error("数据包无数据");
					} else if (size < offset + dataSize) {
						DECORE_LOG.error("数据包格式错误");
					}
					num--;
					session.setAttribute("num", num);
					continue;
				}
				DeviceValueBase collectorDeviceValue = new CollectorDeviceValue();
				collectorDeviceValue.setCollectData(data, size);
				collectorDeviceValue.setDeviceId(id);
				cValueService.saveCollectorDeviceValues(collectorDeviceValue);
				cValueService.saveCollectorDeviceCache(collectorDeviceValue.getDeviceId(), collectorDeviceValue);
				/*if (CacheDataBase.collcetorDeviceMainValueCacheMap.containsKey(collectorDeviceValue.getDeviceId())) {
					List<DeviceValueBase> list = CacheDataBase.collcetorDeviceMainValueCacheMap.get(collectorDeviceValue.getDeviceId());
					if (list.size() >= CacheDataBase.cacheSize) {
						list.remove(0);
						list.add(collectorDeviceValue);
					} else {
						list.add(collectorDeviceValue);
					}
				} else {
					List<DeviceValueBase> list = new ArrayList<DeviceValueBase>();
					list.add(collectorDeviceValue);
					CacheDataBase.collcetorDeviceMainValueCacheMap.put(collectorDeviceValue.getDeviceId(), list);
				}
				CacheDataBase.collectorDeviceMainValueMap.put(collectorDeviceValue.getDeviceId(), collectorDeviceValue);*/
				String validString = "valid";
				if (valid == 0) {
					validString = "no valid"; // 后期考虑要不要添加到内存中
				}
				DECORE_LOG.info("{} package {} data collectorValue is {}", num, validString, collectorDeviceValue);
				num--;
				session.setAttribute("num", num);
			} else {
				in.reset();
				return;
			}
		}
		if (num == 0) {
			DECORE_LOG.info("数据包处理完成");
			out.write("");
		}
	}
}
