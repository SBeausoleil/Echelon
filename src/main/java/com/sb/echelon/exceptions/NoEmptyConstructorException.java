package com.sb.echelon.exceptions;

public class NoEmptyConstructorException extends RuntimeException {
	private static final long serialVersionUID = 8830937257787148984L;
	
	public static final String DEFAULT_MESSAGE = "Echelon ORM requires the presence of an empty constructor.";

	public NoEmptyConstructorException() {
		super(DEFAULT_MESSAGE);
	}

	public NoEmptyConstructorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoEmptyConstructorException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoEmptyConstructorException(String message) {
		super(message);
	}

	public NoEmptyConstructorException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}
	
	

}
