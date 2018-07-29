package com.ifarm.observer;

import org.springframework.web.socket.WebSocketSession;

public abstract class WebSocketSubjectDecorate implements Subject {
	private Subject subject;

	protected abstract void registerObserver(String key, WebSocketObserver o);

	protected abstract void registerObserver(String key, WebSocketSession o);

	protected abstract void removeObserver(WebSocketSession session);

	protected abstract void removeObserver(WebSocketObserver o);

	protected abstract void notifyObservers(String key, String message);

	public WebSocketSubjectDecorate(Subject subject) {
		this.subject = subject;
	}
	
	public WebSocketSubjectDecorate() {
		
	}

	@Override
	public void registerObserver(Observer o) {
		// TODO Auto-generated method stub
		subject.registerObserver(o);
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
