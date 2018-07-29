package com.ifarm.observer;

import org.apache.mina.core.session.IoSession;

public interface IoSessionObserver {
	public void update(Long key, IoSession ioSession) throws Exception;
}
