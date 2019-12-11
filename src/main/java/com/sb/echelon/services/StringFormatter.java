package com.sb.echelon.services;

import java.util.function.Function;

public interface StringFormatter extends Function<String, String> {
	
	String format(String str);
	
	@Override
	default String apply(String str) {
		return format(str);
	}
}
