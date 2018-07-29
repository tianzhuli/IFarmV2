package com.ifarm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class WriteOrReaderUtil {
	public static final Logger io_Handle = Logger.getLogger(WriteOrReaderUtil.class);

	public static PrintWriter getWriter(Socket socket) throws IOException {
		OutputStream socketOut = socket.getOutputStream();
		return new PrintWriter(socketOut, true);
	}

	public static OutputStream getOutputStream(Socket socket) throws IOException {
		return socket.getOutputStream();
	}

	public static BufferedReader getReader(Socket socket) throws IOException {
		InputStream socketIn = socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn, "UTF-8"));
	}

	public static InputStream getInputStream(Socket socket) throws IOException {
		return socket.getInputStream();
	}

	public static byte[] readStream(InputStream inStream, Socket socket) throws Exception {
		int count = 0;
		int k = 0;
		while (count == 0) {
			if (k == 100 || !socket.isConnected() || socket.isClosed()) {
				throw new SocketException();
			}
			count = inStream.available();
			TimeUnit.MILLISECONDS.sleep(200);
			k++;
		}
		byte[] b = new byte[count];
		inStream.read(b);
		return b;
	}

	public static void close(Socket socket, InputStream inputStream, OutputStream outputStream) {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			io_Handle.error(e);
		}
		try {
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			io_Handle.error(e);
		}
		try {
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			io_Handle.error(e);
		}
	}

	public static void close(Socket socket, Reader br, Writer out) {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			io_Handle.error(e);
		}
		try {
			if (br != null) {
				br.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			io_Handle.error(e);
		}
		try {
			if (out != null) {
				out.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			io_Handle.error(e);
		}
	}
}
