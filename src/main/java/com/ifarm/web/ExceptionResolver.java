package com.ifarm.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionResolver.class);

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public String defaultHandle(HttpServletRequest request, Exception e) {
		LOGGER.error("{} is {}", request.getRequestURL(), e);
		return e.getMessage();
	}

}
