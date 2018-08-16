package com.duszak.exception;

public class InvalidDataException extends Exception {

	private static final long serialVersionUID = 102822471584268627L;

	public InvalidDataException (String message) {
		super(message);
	}
}
