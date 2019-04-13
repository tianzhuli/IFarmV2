package com.ifarm.exception;


public class ControlException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5144189904956047505L;

	public ControlException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	
	public ControlException(String message) {
		super(message);
	}

}
