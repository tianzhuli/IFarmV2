package com.ifarm.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.springframework.stereotype.Component;

import com.ifarm.util.ByteConvert;

@Component
public class ByteArrayControlEncoder extends ProtocolEncoderAdapter {

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		byte[] bytes = (byte[]) message;
		byte[] lens = ByteConvert.intToByte1(bytes.length);
		lens[0] += 2; //校验位和结束位
		byte checkNum = ByteConvert.checekByte(bytes, 0, bytes.length - 1);
		bytes[1] = lens[0];
		IoBuffer buffer = IoBuffer.allocate(256);
		buffer.setAutoExpand(true);
		buffer.put(bytes);
		buffer.put(checkNum);
		buffer.put((byte) 0x16);
		buffer.flip();
		out.write(buffer);
		out.flush();
		buffer.free();
	}
}
