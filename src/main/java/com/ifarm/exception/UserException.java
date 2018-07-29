package com.ifarm.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6601815524583688907L;
	
	private static final Log USER_EXCEPTION_LOG = LogFactory.getLog(UserException.class);
	
	@Override
	public void printStackTrace() {
		// TODO Auto-generated method stub
		super.printStackTrace();
		USER_EXCEPTION_LOG.error(getMessage());
		USER_EXCEPTION_LOG.error("userException", getCause());
	}
	
	

}
