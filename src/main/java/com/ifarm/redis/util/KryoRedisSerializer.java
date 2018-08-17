package com.ifarm.redis.util;

import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

@SuppressWarnings("unchecked")
public class KryoRedisSerializer<T> implements RedisSerializer<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(KryoRedisSerializer.class);
	private static final int bufferSize = 1024;
	private static final int maxBufferSize = 20480;

	@Override
	public byte[] serialize(T t) throws SerializationException {
		// TODO Auto-generated method stub
		if (t == null) {
			throw new NullPointerException();
		}
		Kryo kryo = new Kryo();
		Output output = new Output(bufferSize, maxBufferSize);
		try {
			kryo.writeClassAndObject(output, t);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("kryo serialize error", e);
		} finally {
			closeOutputStream(output);
		}
		return output.toBytes();
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		// TODO Auto-generated method stub
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		Kryo kryo = new Kryo();
		Input input = new Input();
		try {
			input.setBuffer(bytes);
			return (T) kryo.readClassAndObject(input);
		} finally {
			closeInputStream(input);
		}
	}

	private void closeOutputStream(OutputStream output) {
		if (output != null) {
			try {
				output.flush();
				output.close();
			} catch (Exception e) {
				LOGGER.error("serialize object close outputStream exception", e);
			}
		}
	}

	private void closeInputStream(InputStream input) {
		if (input != null) {
			try {
				input.close();
			} catch (Exception e) {
				LOGGER.error("serialize object close inputStream exception", e);
			}
		}
	}

}
