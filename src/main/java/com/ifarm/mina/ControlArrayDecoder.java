package com.ifarm.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.springframework.stereotype.Component;

@Component
public class ControlArrayDecoder extends AbstractArrayDecode {

	@Override
	protected boolean canDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) {
		return false;
		// TODO Auto-generated method stub
		
	}
}
