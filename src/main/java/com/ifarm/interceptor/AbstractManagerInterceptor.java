package com.ifarm.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ifarm.constant.SystemResultCodeEnum;
import com.ifarm.nosql.dao.ManagerTokenDao;

public abstract  class AbstractManagerInterceptor implements HandlerInterceptor {
	
	protected ManagerTokenDao managerTokenDao;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		String managerId = request.getParameter("managerId");
		String token = request.getParameter("token");
		if (managerId == null) {
			InterceptorOutputMessage.outStreamMeassge(response, SystemResultCodeEnum.NO_ID);
			return false;
		}
		if (token == null) {
			InterceptorOutputMessage.outStreamMeassge(response, SystemResultCodeEnum.NO_TOKEN);
			return false;
		}
		if (!token.equals(managerTokenDao.getManagerToken(managerId))) {
			InterceptorOutputMessage.outStreamMeassge(response, SystemResultCodeEnum.EXPIRED_TOKEN);
			return false;
		}
		return true;
	}
	
	public abstract boolean doPreHandler(HttpServletRequest request, HttpServletResponse response, Object handler);

}
