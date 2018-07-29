package com.ifarm.observer;

public interface Subject {
	public void registerObserver(Observer object);

	public void removeObserver(Observer key);

	public void notifyObservers(Observer key);

	public boolean containsKey(String key);
}
