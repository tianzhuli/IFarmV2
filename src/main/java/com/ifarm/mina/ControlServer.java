package com.ifarm.mina;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ifarm.util.CacheDataBase;

@Component
public class ControlServer {
	@Autowired
	private ControlByteIoHandler controlByteIoHandler;
	
	@Autowired
	private ControlByteArrayCodecFactory codecFactory;
	
	public void start() throws IOException {
		NioSocketAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 300);
		acceptor.setHandler(controlByteIoHandler);
		// 设置日志记录器
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(codecFactory));
		acceptor.bind(new InetSocketAddress(CacheDataBase.controlPort));
	}
}
