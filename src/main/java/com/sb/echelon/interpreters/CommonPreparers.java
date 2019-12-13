package com.sb.echelon.interpreters;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.echelon.Echelon;

@Component
public class CommonPreparers {
	
	private static Echelon echelon;
	
	private CommonPreparers(Echelon echelon) {
		CommonPreparers.echelon = echelon;
	}

	public static String prepareChar(Object c) {
		char character = (char) c;
		return new String(new char[] { character });
	}

	public static String prepareCharWrapper(Object c) {
		Character character = (Character) c;
		return new String(new char[character]);
	}
	
	public static String prepareCharArray(Object c) {
		return new String((char[]) c);
	}
	
	public static String toJson(Object obj) {
		try {
			return new ObjectMapper().writer().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static long toForeignId(Object foreign) {
		foreign = echelon.save(foreign);
		try {
			return (long) echelon.getAnalyzed(foreign.getClass()).getIdField().get(foreign);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
