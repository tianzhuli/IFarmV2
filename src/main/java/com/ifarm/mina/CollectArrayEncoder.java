package com.ifarm.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.springframework.stereotype.Component;

@Component
public class CollectArrayEncoder extends AbstractArrayEncoder {

	@Override
	public void doEncode(IoSession session, Object message, ProtocolEncoderOutput out) {
		
	}

}
