package com.sb.echelon.exceptions;

public class RuntimeEchelonException extends RuntimeException {
	private static final long serialVersionUID = 5320048419808034928L;

	public RuntimeEchelonException() {
	}

	public RuntimeEchelonException(String message) {
		super(message);
	}

	public RuntimeEchelonException(Throwable cause) {
		super(cause);
	}

	public RuntimeEchelonException(String message, Throwable cause) {
		super(message, cause);
	}

	public RuntimeEchelonException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
