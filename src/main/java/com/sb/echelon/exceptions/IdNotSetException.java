package com.sb.echelon.exceptions;

public class IdNotSetException extends EchelonRuntimeException {
	private static final long serialVersionUID = 4908370425400761519L;

	public IdNotSetException() {
		super();
	}

	public IdNotSetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IdNotSetException(String message, Throwable cause) {
		super(message, cause);
	}

	public IdNotSetException(String message) {
		super(message);
	}

	public IdNotSetException(Throwable cause) {
		super(cause);
	}
}
