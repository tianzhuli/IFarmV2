package com.ifarm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SendDataClient implements Runnable {
	public static String makeCollectorMsg() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = simpleDateFormat.format(new Date());
		Random random = new Random();
		StringBuffer buffer = new StringBuffer("<FFFFFFFF:START=V1.0,");
		buffer.append(random.nextInt(30));
		buffer.append(",");
		buffer.append(random.nextInt(15));
		buffer.append(",");
		buffer.append(random.nextInt(15));
		buffer.append(",");
		buffer.append(now.replace(" ", ","));
		buffer.append(">\n");
		return buffer.toString();
	}

	public static String makeSensorString() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = simpleDateFormat.format(new Date());
		Random random = new Random();
		StringBuffer buffer = new StringBuffer();
		for (int i = 1; i <= 4; i++) {
			buffer.append("<DATA=4,");
			buffer.append(i);
			buffer.append(",");
			buffer.append(now.replace(" ", ","));
			buffer.append(",");
			buffer.append("2016000" + i);
			buffer.append(",");
			if (i == 1) {
				buffer.append(random.nextFloat() + 5);
				buffer.append(">\n");
				continue;
			}
			buffer.append(random.nextFloat() + 10);
			buffer.append(",");
			buffer.append(random.nextFloat() + 5);
			buffer.append(">\n");
		}
		return buffer.toString();
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			Thread thread = new Thread(new SendDataClient());
			thread.start();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		// System.out.println(makeSensorString());
		Socket socket = null;
		int port = 9009;
		OutputStream socketOut = null;
		BufferedReader br = null;
		try {
			String res = "";
			socket = new Socket("47.93.91.45", port);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// 发送消息
			// String msg =
			// "<201601:START=V1.0,30,15,15,255,2016-12-03,15:58:30>\n";
			String msg = makeCollectorMsg();
			socketOut = socket.getOutputStream();
			socketOut.write(msg.getBytes());
			socketOut.flush();
			// 接收服务器的反馈
			while ((res = br.readLine()) != null) {
				System.out.println(res);
				if (res.equals("<DATA>")) {
					// String sendMsg =
					// "<DATA=2,1,2016-12-31,15:30:30,201601001,15.5,22.1><DATA=2,2,2016-12-31,15:30:30,201601002,3.89,4.51>\n";
					String sendMsg = makeSensorString();
					socketOut.write(sendMsg.getBytes());
					socketOut.flush();
				} else if (res.contains("<STOP>")) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (socketOut != null) {
				try {
					socketOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
