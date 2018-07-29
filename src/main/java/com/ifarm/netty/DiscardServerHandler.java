package com.ifarm.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
	private static final Log severHandler_log = LogFactory.getLog(DiscardServerHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("receive:" + msg.toString());
		ByteBuf in = (ByteBuf) msg;
		ByteBuf outBuf = null;
		while (in.isReadable()) {
			byte[] arr = new byte[in.readableBytes()];
			in.readBytes(arr);
			if (arr[0] == 0x11) {
				severHandler_log.info("-------心跳包--------");
			} else {
				outBuf = ctx.alloc().buffer(1);
				outBuf.writeByte(0x10);
				ctx.writeAndFlush(outBuf);
			}
		}
		// ReleaseUtil.release(in, outBuf);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("-------------连接关闭-------------");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("-------------连接开启-------------" + ctx);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		// System.out.println("send message");
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		cause.printStackTrace();
		ctx.close();
	}

}
