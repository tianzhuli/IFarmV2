package com.ifarm.netty;

import io.netty.buffer.ByteBuf;

public class ReleaseUtil {
	public static void release(ByteBuf in, ByteBuf out) {
		if (in != null) {
			in.release();
		}
		if (out != null) {
			out.release();
		}
	}
}
