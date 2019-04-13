package com.ifarm.util;

import java.sql.Timestamp;

import org.springframework.core.convert.converter.Converter;

public class TimestampConverter implements Converter<String, Timestamp>{

	@Override
	public Timestamp convert(String source) {
		// TODO Auto-generated method stub
		Timestamp timestamp = null;
		if (!source.isEmpty()) {
			timestamp = Timestamp.valueOf(source);
		}
		return timestamp;
	}

}
