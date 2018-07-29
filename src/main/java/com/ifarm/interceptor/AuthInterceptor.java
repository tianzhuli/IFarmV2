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
import com.ifarm.redis.util.UserRedisUtil;


public class AuthInterceptor implements HandlerInterceptor {
	
	@Autowired
	private UserRedisUtil userRedisUtil;
	
	private static final Log AUTHINTERCEPTOR_LOG = LogFactory.getLog(AuthInterceptor.class);

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
		AUTHINTERCEPTOR_LOG.info(request.getRequestURI() + "---" + JSONObject.toJSONString(request.getParameterMap()));
		String signature = request.getParameter("signature");
		String userId = request.getParameter("userId");
		String managerId = request.getParameter("managerId");
		if (managerId != null) {
			
		}
		if (signature != null || userId != null) {
			String sign = "";
			//sign = CacheDataBase.userSignature.get(userId);
			sign = userRedisUtil.getUserSignature(userId);
			if (sign != null && signature != null && signature.equals(sign)) {
				return true;
			} else {
				InterceptorOutputMessage.outStreamMeassge(response, SystemResultCodeEnum.EXPIRED_TOKEN);
				return false;
			}
		} else {
			InterceptorOutputMessage.outStreamMeassge(response, SystemResultCodeEnum.NO_USER);
			return false;
		}
	}
}
