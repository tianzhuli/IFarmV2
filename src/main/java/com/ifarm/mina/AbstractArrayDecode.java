package com.ifarm.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ifarm.util.ConvertData;

public abstract class AbstractArrayDecode extends CumulativeProtocolDecoder {
	protected static final Logger LOGGER = LoggerFactory.getLogger("MINA——DIGEST");
	private static int minLen = 10;
	protected ConvertData convertData = new ConvertData();
	
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		if (in.remaining() > 0) {
			in.mark(); // 标记位置
			byte[] requestArray = new byte[in.remaining()];
			in.get(requestArray);
			byteLogRecord(requestArray, "request Array");
			if (!controlRequestValidator(requestArray)) {
				LOGGER.info("byte request error");
				return false;
			}
			int len = convertData.getdataType2(requestArray, 1);
			if (len == requestArray.length) {
				out.write(requestArray);
			}
		}
		// 处理成功，让父类进行接收下个包
		return false;
	}

	public void byteLogRecord(byte[] arr, String name) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			builder.append(CommonUtil.byteToHex(arr[i])).append(" ");
		}
		LOGGER.info("{} : {}", name, builder);
	}

	public boolean controlRequestValidator(byte[] arr) {
		if (arr != null && arr.length >= minLen && arr[0] == 0x68 && arr[arr.length - 1] == 0x16) {
			return true;
		}
		return false;
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
		LOGGER.info("{} : {}", name, builder);
	}

	
	protected abstract boolean canDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out);

}
