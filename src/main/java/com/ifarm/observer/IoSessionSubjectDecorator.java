package com.ifarm.observer;

import org.apache.mina.core.session.IoSession;

import com.ifarm.mina.ControlByteIoHandler;

public abstract class IoSessionSubjectDecorator implements Subject {
	private Subject subject;

	protected abstract void registerObserver(Long key, IoSession o);

	protected abstract void registerObserver(String key, IoSessionObserver o);

	protected abstract void removeObserver(ControlByteIoHandler ioHandler);

	protected abstract void removeObserver(IoSession session);

	protected abstract void notifyObservers(Long key) throws Exception;

	public IoSessionSubjectDecorator(Subject subject) {
		this.subject = subject;
	}

	@Override
	public void registerObserver(Observer object) {
		// TODO Auto-generated method stub
		subject.registerObserver(object);
	}

	@Override
	public void removeObserver(Observer key) {
		// TODO Auto-generated method stub
		subject.removeObserver(key);
	}

	@Override
	public void notifyObservers(Observer key) {
		// TODO Auto-generated method stub
		subject.notifyObservers(key);
	}

}
