package com.ifarm.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.springframework.stereotype.Component;

@Component
public class ByteArrayCollectEncoder extends AbstractArrayEncoder {

	@Override
	public void doEncode(IoSession session, Object message, ProtocolEncoderOutput out) {
		// TODO Auto-generated method stub
		/*byte[] bytes = (byte[]) message;
		byte len = (byte) (bytes.length & 0xff);
		byte checkNum = ByteConvert.checekByte(bytes, 0, bytes.length - 1);
		IoBuffer buffer = IoBuffer.allocate(256);
		buffer.setAutoExpand(true);
		buffer.put((byte) 0x68);
		buffer.put(len);
		buffer.put(len);
		buffer.put((byte) 0x68);
		buffer.put(bytes);
		buffer.put(checkNum);
		buffer.put((byte) 0x16);
		buffer.flip();
		out.write(buffer);
		out.flush();
		buffer.free();*/
	}

}
