package com.sb.echelon.parsers;

import java.util.LinkedHashMap;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PrimitiveParser {
	
	public static byte parseByte(LinkedHashMap<String, Object> row, String colName) {
		return (byte) row.getOrDefault(colName, 0);
	}
	
	public static Byte parseByteWrapper(LinkedHashMap<String, Object> row, String colName) {
		return (Byte) row.get(colName);
	}
	
	public static short parseShort(LinkedHashMap<String, Object> row, String colName) {
		return (short) row.getOrDefault(colName, 0);
	}
	
	public static Short parseShortWrapper(LinkedHashMap<String, Object> row, String colName) {
		return (Short) row.get(colName);
	}

	/**
	 * Retrieves the value of the designated column as an int in the Java programming language.
	 * @param row
	 * @param colName
	 * @return the column value; if the value is SQL NULL, the value returned is 0
	 */
	public static int parseInt(LinkedHashMap<String, Object> row, String colName) {
		return (int) row.getOrDefault(colName, 0);
	}
	
	public static Integer parseInteger(LinkedHashMap<String, Object> row, String colName) {
		return (Integer) row.get(colName);
	}
	
	public static long parseLong(LinkedHashMap<String, Object> row, String colName) {
		return (long) row.getOrDefault(colName, 0l);
	}
	
	public static Long parseLongWrapper(LinkedHashMap<String, Object> row, String colName) {
		return (Long) row.get(colName);
	}
	
	public static float parseFloat(LinkedHashMap<String, Object> row, String colName) {
		return (float) row.getOrDefault(colName, 0f);
	}
	
	public static Float parseFloatWrapper(LinkedHashMap<String, Object> row, String colName) {
		return (Float) row.get(colName);
	}
	
	public static double parseDouble(LinkedHashMap<String, Object> row, String colName) {
		return (double) row.getOrDefault(colName, 0.0);
	}
	
	public static Double parseDoubleWrapper(LinkedHashMap<String, Object> row, String colName) {
		return (Double) row.get(colName);
	}
	
	public static char parseChar(LinkedHashMap<String, Object> row, String colName) {
		String val = (String) row.get(colName);
		if (val != null)
			return val.charAt(0);
		return '\0';
	}
	
	public static Character parseCharacter(LinkedHashMap<String, Object> row, String colName) {
		String val = (String) row.get(colName);
		if (val != null)
			return val.charAt(0);
		return null;
	}
	
	public static String parseString(LinkedHashMap<String, Object> row, String colName) {
		return (String) row.get(colName);
	}
}
