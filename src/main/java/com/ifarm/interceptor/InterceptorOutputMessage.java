package com.ifarm.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class InterceptorOutputMessage {
	
	public static void outStreamMeassge(HttpServletResponse response,Object message) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(message);
		out.flush();
		out.close();
	}
}
