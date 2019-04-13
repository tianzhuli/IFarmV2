package com.ifarm.netty;

import org.springframework.stereotype.Component;
import com.ifarm.util.ByteConvert;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@Component
public class CustomEncoder extends MessageToByteEncoder<byte[]> {

	@Override
	protected void encode(ChannelHandlerContext ctx, byte[] arr, ByteBuf out)
			throws Exception {
		// TODO Auto-generated method stub
		byte[] lens = ByteConvert.intToByte2(arr.length + 5); // 需要增加帧多、帧尾和校验
		byte[] outBytes = new byte[arr.length + 5];
		out = ctx.alloc().buffer(outBytes.length);
		outBytes[0] = 0x68;
		outBytes[1] = lens[0];
		outBytes[2] = lens[1];
		for (int i = 0; i < outBytes.length; i++) {
			outBytes[i + 3] = arr[i];
		}
		outBytes[arr.length + 3] = ByteConvert.checekByte(outBytes, 0,
				outBytes.length - 3);
		outBytes[arr.length + 4] = 0x16;
		out.writeBytes(outBytes);
		ctx.writeAndFlush(out);
	}

}
