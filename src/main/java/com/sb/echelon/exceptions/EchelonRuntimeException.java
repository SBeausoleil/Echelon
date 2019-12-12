package com.sb.echelon.exceptions;

public class EchelonRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 5320048419808034928L;

	public EchelonRuntimeException() {
	}

	public EchelonRuntimeException(String message) {
		super(message);
	}

	public EchelonRuntimeException(Throwable cause) {
		super(cause);
	}

	public EchelonRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public EchelonRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
