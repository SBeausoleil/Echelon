package com.sb.echelon.interpreters;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonPreparers {

	public static Object prepareChar(Object c) {
		char character = (char) c;
		return new String(new char[] { character });
	}

	public static String prepareCharWrapper(Object c) {
		if (c != null) {
			Character character = (Character) c;
			return new String(new char[character]);
		}
		return null;
	}
}
