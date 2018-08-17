package com.ifarm.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ifarm.util.ConvertData;

@Component
public class ByteArrayControlDecoder extends CumulativeProtocolDecoder {
	private static final Logger decode_log = LoggerFactory.getLogger(ByteArrayControlDecoder.class);
	ConvertData convertData = new ConvertData();
	int minLen = 10;

	public void byteLogRecord(byte[] arr, String name) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			builder.append(ByteArrayCommonUtil.byteToHex(arr[i])).append(" ");
		}
		decode_log.info("{} : {}", name, builder);
	}

	public boolean controlRequestValidator(byte[] arr) {
		if (arr != null && arr.length >= minLen && arr[0] == 0x68 && arr[arr.length - 1] == 0x16) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		if (in.remaining() > 0) {
			in.mark(); // 标记位置
			byte[] requestArray = new byte[in.remaining()];
			in.get(requestArray);
			if (!controlRequestValidator(requestArray)) {
				decode_log.info("byte request error");
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
}
