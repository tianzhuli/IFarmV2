package com.ifarm.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.ifarm.constant.SystemResultCodeEnum;
import com.ifarm.nosql.dao.ManagerTokenDao;

public class ManagerInterceptor implements HandlerInterceptor {
	@Autowired
	private ManagerTokenDao managerTokenDao;

	private static final Log MANAGERINTERCEPTOR_LOG = LogFactory.getLog(ManagerInterceptor.class);

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		MANAGERINTERCEPTOR_LOG.info(request.getRequestURI() + "---" + JSONObject.toJSONString(request.getParameterMap()));
		String managerId = request.getParameter("managerId");
		String token = request.getParameter("token");
		if (managerId == null) {
			InterceptorOutputMessage.outStreamMeassge(response, SystemResultCodeEnum.NO_ID);
			return false;
		}
		if (token == null) {
			InterceptorOutputMessage.outStreamMeassge(response, SystemResultCodeEnum.NO_TOKEN  );
			return false;
		}
		if (!token.equals(managerTokenDao.getManagerToken(managerId))) {
			InterceptorOutputMessage.outStreamMeassge(response, SystemResultCodeEnum.EXPIRED_TOKEN);
			return false;
		}
		return true;
	}
}
