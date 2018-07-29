package com.ifarm.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

@SuppressWarnings("resource")
public class ControlClient implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		Socket socket = null;
		int port = 9008;
		OutputStream socketOut = null;
		InputStream in = null;
		try {
			socket = new Socket("172.20.33.237", port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (scanner.hasNext()) {
			int count = scanner.nextInt();
			byte[] buffer = null;
			if (count == 0) {
				int collectorId = scanner.nextInt();
				buffer = ByteConvert.intToByte4(collectorId);
			} else if (count == -1) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				buffer = new byte[count];
				for (int i = 0; i < buffer.length; i++) {
					buffer[i] = scanner.nextByte();
				}
			}
			for (int i = 0; i < buffer.length; i++) {
				System.out.println(buffer[i]);
			}
			try {

				in = socket.getInputStream();
				socketOut = socket.getOutputStream();
				socketOut.write(buffer);
				socketOut.flush();
				System.out.println("发送成功");
				byte[] byt = WriteOrReaderUtil.readStream(in, socket);
				for (int i = 0; i < byt.length; i++) {
					System.out.print(Integer.toHexString(byt[i]) + " ");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			Thread thread = new Thread(new ControlClient());
			thread.start();
		}
	}
}
