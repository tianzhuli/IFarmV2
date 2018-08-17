package com.ifarm.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.springframework.stereotype.Component;

@Component
public class ByteArrayControlEncoder extends AbstractArrayEncoder {

	@Override
	public void doEncode(IoSession session, Object message, ProtocolEncoderOutput out) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				/*byte[] bytes = (byte[]) message;
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
				buffer.free();*/
		
		
				/*byte[] bytes = (byte[]) message;
				byte[] lens = ByteConvert.intToByte2(bytes.length + 5); //需要增加帧多、帧尾和校验
				IoBuffer buffer = IoBuffer.allocate(256);
				buffer.setAutoExpand(true);
				buffer.put((byte) 0x68);
				buffer.put(lens[0]);
				buffer.put(lens[1]);
				buffer.put(bytes);
				byte[] checkArray = buffer.array();
		 		byte checkNum = ByteConvert.checekByte(checkArray, 0, bytes.length - 1);
				buffer.put(checkNum);
				buffer.put((byte) 0x16);
				buffer.flip();
				out.write(buffer);
				out.flush();
				buffer.free();*/
	}
}
