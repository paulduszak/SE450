package com.duszak.exception;

public class NullParameterException extends Exception {
	
	private static final long serialVersionUID = 870138007348408855L;

	public NullParameterException (String message) {
		super(message);
	}
}
