package com.sb.echelon.interpreters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonPreparers {

	public static Object prepareChar(Object c) {
		char character = (char) c;
		return new String(new char[] { character });
	}

	public static String prepareCharWrapper(Object c) {
		Character character = (Character) c;
		return new String(new char[character]);
	}
	
	public static String toJson(Object obj) {
		try {
			return new ObjectMapper().writer().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
