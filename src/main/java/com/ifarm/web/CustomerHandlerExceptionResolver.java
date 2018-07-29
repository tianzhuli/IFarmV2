package com.ifarm.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

public class CustomerHandlerExceptionResolver extends SimpleMappingExceptionResolver {
	private static final Logger handlerException_log = Logger.getLogger(CustomerHandlerExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		// TODO Auto-generated method stub
		handlerException_log.error(ex.getMessage());
		ModelAndView modelAndView = super.doResolveException(request, response, handler, ex);
		;
		/*
		 * ResponseStatus responseStatus =
		 * AnnotationUtils.getAnnotation(ex.getClass(), ResponseStatus.class);
		 * // 自定义的异常 if (responseStatus != null) {
		 * modelAndView.setViewName("error/" + responseStatus.value().value());
		 * } else { // 其他异常，做一些其他的处理,如发送错误报警邮件，记录日志
		 * logger.error(ex.getMessage(), ex); }
		 */
		return modelAndView;
	}

}
