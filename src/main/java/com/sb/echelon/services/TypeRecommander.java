package com.sb.echelon.services;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.sb.echelon.ColType;

@Component
public class TypeRecommander {

	/**
	 * key: the Java type
	 * value: the SQL type
	 */
	private HashMap<Class<?>, String> suggestions = defaultSuggestions();
	
	protected HashMap<Class<?>, String> defaultSuggestions() {
		HashMap<Class<?>, String> map = new HashMap<>();
		
		map.put(byte.class, ColType.TINYINT.sql);
		map.put(Byte.class, ColType.TINYINT.sql);
		map.put(short.class, ColType.SMALLINT.sql);
		map.put(Short.class, ColType.SMALLINT.sql);
		map.put(int.class, ColType.INTEGER.sql);
		map.put(Integer.class, ColType.INTEGER.sql);
		map.put(long.class, ColType.BIGINT.sql);
		map.put(Long.class, ColType.BIGINT.sql);
		map.put(float.class, ColType.FLOAT.sql);
		map.put(Float.class, ColType.FLOAT.sql);
		map.put(double.class, ColType.DOUBLE.sql);
		map.put(Double.class, ColType.DOUBLE.sql);
		
		map.put(char.class, ColType.CHAR.sql);
		map.put(Character.class, ColType.CHAR.sql);
		map.put(String.class, ColType.VARCHAR.sql);
		
		map.put(boolean.class, ColType.BOOLEAN.sql);
		map.put(Boolean.class, ColType.BOOLEAN.sql);
		
		map.put(byte[].class, ColType.BLOB.sql);
		map.put(Byte[].class, ColType.BLOB.sql);
		
		map.put(short[].class, ColType.JSON.sql);
		map.put(Short[].class, ColType.JSON.sql);
		map.put(int[].class, ColType.JSON.sql);
		map.put(Integer[].class, ColType.JSON.sql);
		map.put(long[].class, ColType.JSON.sql);
		map.put(Long[].class, ColType.JSON.sql);
		map.put(float[].class, ColType.JSON.sql);
		map.put(Float[].class, ColType.JSON.sql);
		map.put(double[].class, ColType.JSON.sql);
		map.put(Double[].class, ColType.JSON.sql);
		map.put(char[].class, ColType.VARCHAR.sql);
		map.put(Character[].class, ColType.VARCHAR.sql);
		map.put(String[].class, ColType.JSON.sql);
		
		return map;
	}

	public String putSuggestion(Class<?> forType, String sqlType) {
		return suggestions.put(forType, sqlType);
	}
	
	public String putSuggestionIfAbsent(Class<?> forType, String sqlType) {
		return suggestions.putIfAbsent(forType, sqlType);
	}
	
	public String getSuggestionFor(Class<?> type) {
		return suggestions.get(type);
	}
}
