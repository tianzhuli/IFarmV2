package com.ifarm.mina;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CollectServer {
	
	@Autowired
	private CollectHandler collectHandler;
	
	@Autowired
	private CollectArrayCodecFactory collectByteArrayCodecFactory;
	
	public void start() throws IOException {
		/*NioSocketAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 120);
		acceptor.setHandler(collectHandler);
		// 设置日志记录器
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(collectByteArrayCodecFactory));
		acceptor.bind(new InetSocketAddress(CacheDataBase.port));*/
	}
}
