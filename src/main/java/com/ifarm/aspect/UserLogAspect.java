package com.ifarm.aspect;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

import com.ifarm.annotation.FarmControllerLog;
import com.ifarm.service.UserLogService;

@Aspect
public class UserLogAspect {
	@Autowired
	private UserLogService userLogService;
	private final static Log log = LogFactory.getLog(UserLogAspect.class);

	@Pointcut("execution(* com.ifarm.service.*.*(..))")
	public void userLogAspect() {

	}

	@Pointcut("@annotation(com.ifarm.annotation.FarmServiceLog)")
	public void serviceLog() {

	}

	@Pointcut("@annotation(com.ifarm.annotation.FarmControllerLog)")
	public void controllerLog() {

	}

	@AfterReturning(pointcut = "controllerLog()", returning = "message")
	public void saveUserLog(JoinPoint join, String message) {
		String methodName = join.getSignature().getName();
		Object[] args = join.getArgs();
		try {
			Method[] methods = join.getTarget().getClass().getMethods();
			for (Method m : methods) {
				if (m.getName().equals(methodName)) {
					Class<?>[] clazz = m.getParameterTypes();
					if (clazz.length == args.length) {
						String value = m.getAnnotation(FarmControllerLog.class).value();
						String param = m.getAnnotation(FarmControllerLog.class).param();
						log.info(methodName + ":" + value + "--" + param);
						break;
					}
				}
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info(join);
		//System.out.println("后置通知" + message);
	}
}
