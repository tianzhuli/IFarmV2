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
	int minLen = 4;
	public void byteLogRecord(byte[] arr, String name) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			builder.append(ByteArrayCommonUtil.byteToHex(arr[i])).append(" ");
		}
		decode_log.info("{} : {}", name, builder);
	}
	
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		if (in.remaining() > 0) {
			in.mark(); // 标记位置
			if (in.remaining() < minLen) {
				return false;
			}
			byte[] header = new byte[minLen];
			byte[] bytes = null;
			in.get(header);
			if (header[0] == '\r') {
				in.reset();
				bytes = new byte[in.remaining()];
				in.get(bytes);
				int bytLen = bytes.length;
				if (bytLen >= 2 && bytes[bytLen - 2] == '\r' && bytes[bytLen - 1] == '\n') {
					byteLogRecord(bytes, "heart packet");
					out.write(bytes);
				}
				return false;
			}
			if (header[0] != 0x68 || header[3] != 0x68) {
				decode_log.info("------数据包错误------");
				return false;
			}
			int len = convertData.getdataType1(header, 1);
			in.reset();
			if (len > in.remaining()) {
				in.reset(); // 可能出现了拆包现象
				return false;
			} else {
				bytes = new byte[len];
				in.get(bytes);
				byteLogRecord(bytes, "device reply data");
				out.write(bytes);
				if (bytes[bytes.length - 1] != 0x16) {
					return false; // 错误包
				}
			}
		}
		// 处理成功，让父类进行接收下个包
		return false;
	}
}
