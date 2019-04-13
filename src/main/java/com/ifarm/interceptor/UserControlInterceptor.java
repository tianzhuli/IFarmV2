package com.ifarm.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ifarm.bean.User;
import com.ifarm.constant.SystemResultCodeEnum;
import com.ifarm.redis.util.UserRedisUtil;
import com.ifarm.service.UserService;

public class UserControlInterceptor implements HandlerInterceptor {
	@Autowired
	private UserService userService;

	@Autowired
	private UserRedisUtil userRedisUtil;

	private static final Log CONTROLTERCEPTOR_LOG = LogFactory.getLog(UserControlInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		//CONTROLTERCEPTOR_LOG.info(request.getRequestURI() + "---" + JSONObject.toJSONString(request.getParameterMap()));
		String signature = request.getParameter("signature");
		String userId = request.getParameter("userId");
		String managerId = request.getParameter("managerId");
		if (managerId != null) {
			return true;
		}
		if (signature != null || userId != null) {
			String sign = userRedisUtil.getUserSignature(userId);
			if (sign != null && signature != null && signature.equals(sign)) {
				User user = userService.qeuryUserById(userId);
				if (ControlAuthConstans.ONLY_SEE.equals(user.getUserRole())) {
					InterceptorOutputMessage.outStreamMeassge(response, SystemResultCodeEnum.NO_AUTH);
					CONTROLTERCEPTOR_LOG.info("request no auth");
					return false;
				}
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

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// TODO Auto-generated method stub

	}

}
