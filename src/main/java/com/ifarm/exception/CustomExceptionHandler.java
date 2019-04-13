package com.ifarm.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.ifarm.util.SystemResultEncapsulation;

public class CustomExceptionHandler implements HandlerExceptionResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		// TODO Auto-generated method stub
		LOGGER.error("error", ex);
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(SystemResultEncapsulation.fillErrorCode(ex));
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error("io exception", e);
		}
		return null;
	}

}
