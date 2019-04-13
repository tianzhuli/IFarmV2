package com.ifarm.util;

import java.util.HashMap;
import java.util.Map;

public class EventContext {
	
	private Map<String, Object> map = new HashMap<>();
	
	public void setEvent(Object object) {
		map.put("event", object);
	}
	
	public Object getEvent() {
		return map.get("event");
	}
}
