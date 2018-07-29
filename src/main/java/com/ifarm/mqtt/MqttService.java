package com.ifarm.mqtt;

import java.io.IOException;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.ifarm.util.CacheDataBase;

public class MqttService {
	public static String host = "";
	public static String clientId = "";
	public static String user = "";
	public static String password = "";
	public static int connectionTimeout = 0;
	public static int keepAliveInterval = 0;
	public static String[] topics;
	public static String[] qos;

	public MqttClient mqttClient;
	public MqttTopic[] mqttTopics;
	public MqttMessage message;

	public MqttService() throws MqttException {
		Properties properties = new Properties();
		try {
			properties.load(CacheDataBase.class.getClassLoader().getResourceAsStream("com/ifarm/mqtt/mqtt.properties"));
			host = properties.getProperty("mqtt.url");
			clientId = properties.getProperty("mqtt.client.id");
			user = properties.getProperty("mqtt.username");
			password = properties.getProperty("mqtt.password");
			topics = properties.getProperty("mqtt.topics").split(",");
			qos = properties.getProperty("mqtt.qos").split(",");
			connectionTimeout = Integer.valueOf(properties.getProperty("mqtt.connectionTimeout"));
			keepAliveInterval = Integer.valueOf(properties.getProperty("mqtt.keepAliveInterval"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mqttClient = new MqttClient(host, clientId, new MemoryPersistence());
		// connect();
	}

	public void connect() {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(false);
		options.setUserName(user);
		options.setPassword(password.toCharArray());
		// 设置超时时间
		options.setConnectionTimeout(connectionTimeout);
		// 设置会话心跳时间
		options.setKeepAliveInterval(keepAliveInterval);
		try {
			mqttClient.setCallback(new PushCallback());
			mqttClient.connect(options);
			mqttTopics = new MqttTopic[topics.length];
			for (int i = 0; i < topics.length; i++) {
				mqttTopics[i] = mqttClient.getTopic(topics[i]);
			}
		} catch (Exception mqttException) {
			System.out.println("重连服务器线程启动");
		}
	}

	public void publish(MqttTopic topic, MqttMessage message) throws MqttPersistenceException, MqttException {
		MqttDeliveryToken token = topic.publish(message);
		token.waitForCompletion();
		System.out.println("message is published completely! " + token.isComplete());
	}
}
