package com.ifarm.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ifarm.annotation.AuthPassport;
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
		try {
			Map<String, String[]> map = request.getParameterMap();
			StringBuffer paramBuffer = new StringBuffer("{");
			for (String key : map.keySet()) {
				paramBuffer.append(key + "=[");
				String[] values = map.get(key);
				for (int i = 0; i < values.length; i++) {
					paramBuffer.append(values[i] + ",");
				}
				if (paramBuffer.charAt(paramBuffer.length() - 1) == ',') {
					paramBuffer.deleteCharAt(paramBuffer.length() - 1);
				}
				paramBuffer.append("],");
			}
			if (paramBuffer.charAt(paramBuffer.length() - 1) == ',') {
				paramBuffer.deleteCharAt(paramBuffer.length() - 1);
			}
			paramBuffer.append("}");
			MANAGERINTERCEPTOR_LOG.info("url:" + request.getRequestURI() + " param:" + paramBuffer);
		} catch (Exception e) {
			// TODO: handle exception
			MANAGERINTERCEPTOR_LOG.error("param error", e);
		}
		String managerId = request.getParameter("managerId");
		AuthPassport authPassport = ((HandlerMethod) handler).getMethodAnnotation(AuthPassport.class);
		if (authPassport != null && authPassport.validate() == false) {
			if (managerId == null) {
				return false;
			} else {
				return true;
			}
		}
		String token = request.getParameter("token");
		if (token == null || managerId == null) {
			return false;
		}
		if (!token.equals(managerTokenDao.getManagerToken(managerId))) {
			InterceptorOutputMessage.outStreamMeassge(response, SystemResultCodeEnum.EXPIRED_TOKEN);
			return false;
		}
		return true;
	}
}
