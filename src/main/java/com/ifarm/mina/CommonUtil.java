package com.ifarm.mina;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ifarm.util.ConvertData;

public class CommonUtil {
	private static final ConvertData convertData = new ConvertData();
	private static final Logger MESSAGE_DIGEST_LOG = LoggerFactory
			.getLogger("MESSAGE—DIGEST");
	private static final Logger HEART_LOG = LoggerFactory
			.getLogger("HEART—DIGEST");
	private static final Logger HEART_INFO = LoggerFactory
			.getLogger("HEART—INFO");

	public static String byteToHex(byte b) {
		String hex = Integer.toHexString(b & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		return hex.toUpperCase();
	}

	public static String parseSofaVersion(byte[] arr, int pos) {
		StringBuilder builder = new StringBuilder("V");
		for (int i = 0; i < 4; i++) {
			builder.append((char) ('0' + arr[pos + i])).append(".");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

	public static Long parserHeartMessage(byte[] arr) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			buffer.append(Integer.toHexString(arr[i] & 0xFF)).append(" ");
		}
		HEART_INFO.info("buffer is {}:", buffer.toString());
		Long collectorId = convertData.byteToConvertLong(arr, 4, 4);
		String softVersion = parseSofaVersion(arr, 12);
		Date deviceWorkingTime = new Date(
				convertData.getdataType5(arr, 16) * 1000);
		Date deviceRunningTime = new Date(
				convertData.getdataType5(arr, 20) * 1000);
		// 运行代码、故障代码、故障时间暂时不需要
		String simNo = new String(arr, 32, 20); // 预留四个字节
		int networkSignal = arr[56];
		StringBuilder builder = new StringBuilder();
		builder.append("物联网通讯器4G编号:").append(collectorId).append(";软件版本:")
				.append(softVersion).append(";SIM:").append(simNo)
				.append(";设备时间:").append(deviceWorkingTime).append(";设备运行时间:")
				.append(deviceRunningTime).append(";信号强度:")
				.append(networkSignal);
		HEART_LOG.info(builder.toString());
		return collectorId;
	}

	public static void messageDigest(byte[] arr, String message) {
		String[] buffer = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			buffer[i] = Integer.toHexString(arr[i] & 0xFF);
		}
		MESSAGE_DIGEST_LOG.info("server {} buffer:{}", message, buffer);
	}
}
