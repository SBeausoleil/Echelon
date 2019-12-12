package com.sb.echelon.exceptions;

public class NoIdFieldException extends EchelonRuntimeException {
	private static final long serialVersionUID = -4308582206714518021L;
	
	public NoIdFieldException(Class<?> clazz) {
		super("The class " + clazz.getName() + " has no usable ID field. For a field to be used by Echelon, it must be of type long and be either named id or annotated with @Id");
	}
	
	public NoIdFieldException(String message, Exception cause) {
		super(message, cause);
	}
}
