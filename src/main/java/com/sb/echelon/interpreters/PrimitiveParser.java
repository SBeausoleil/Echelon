package com.sb.echelon.interpreters;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PrimitiveParser {

	public static byte parseByte(LinkedHashMap<String, Object> row, String colName) {
		Object obj = row.getOrDefault(colName, 0);
		return (byte) (obj != null ? obj : 0);
	}

	public static Byte parseByteWrapper(LinkedHashMap<String, Object> row, String colName) {
		return (Byte) row.get(colName);
	}

	public static short parseShort(LinkedHashMap<String, Object> row, String colName) {
		Object obj = row.getOrDefault(colName, 0);
		return (short) (obj != null ? obj : 0);
	}

	public static Short parseShortWrapper(LinkedHashMap<String, Object> row, String colName) {
		return (Short) row.get(colName);
	}

	/**
	 * Retrieves the value of the designated column as an int in the Java
	 * programming language.
	 * 
	 * @param row
	 * @param colName
	 * @return the column value; if the value is SQL NULL, the value returned is 0
	 */
	public static int parseInt(LinkedHashMap<String, Object> row, String colName) {
		Object obj = row.getOrDefault(colName, 0);
		return (int) (obj != null ? obj : 0);
	}

	public static Integer parseInteger(LinkedHashMap<String, Object> row, String colName) {
		return (Integer) row.get(colName);
	}

	public static long parseLong(LinkedHashMap<String, Object> row, String colName) {
		Object obj = row.getOrDefault(colName, 0l);
		return (long) (obj != null ? obj : 0l);
	}

	public static Long parseLongWrapper(LinkedHashMap<String, Object> row, String colName) {
		return (Long) row.get(colName);
	}

	public static float parseFloat(LinkedHashMap<String, Object> row, String colName) {
		Object obj = row.getOrDefault(colName, 0f);
		return (float) (obj != null ? obj : 0f);
	}

	public static Float parseFloatWrapper(LinkedHashMap<String, Object> row, String colName) {
		return (Float) row.get(colName);
	}

	public static double parseDouble(LinkedHashMap<String, Object> row, String colName) {
		Object obj = row.getOrDefault(colName, 0.0);
		return (byte) (obj != null ? obj : 0.0);
	}

	public static Double parseDoubleWrapper(LinkedHashMap<String, Object> row, String colName) {
		return (Double) row.get(colName);
	}

	public static char parseChar(LinkedHashMap<String, Object> row, String colName) {
		String val = (String) row.get(colName);
		if (val != null && val.length() > 0)
			return val.charAt(0);
		return '\0';
	}

	public static Character parseCharacter(LinkedHashMap<String, Object> row, String colName) {
		String val = (String) row.get(colName);
		if (val != null && val.length() > 0)
			return val.charAt(0);
		return null;
	}

	public static String parseString(LinkedHashMap<String, Object> row, String colName) {
		return (String) row.get(colName);
	}

	public static boolean parseBoolean(LinkedHashMap<String, Object> row, String colName) {
		Object val = row.getOrDefault(colName, false);
		return (boolean) (val != null ? val : false);
	}

	public static Boolean parseBooleanWrapper(LinkedHashMap<String, Object> row, String colName) {
		return (Boolean) row.get(colName);
	}

	public static byte[] parseBlob(LinkedHashMap<String, Object> row, String colName) {
		return (byte[]) row.get(colName);
	}

	public static short[] parseShortJson(LinkedHashMap<String, Object> row, String colName) {
		try {
			return new ObjectMapper().readValue((String) row.get(colName), short[].class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Short[] parseShortWrapperJson(LinkedHashMap<String, Object> row, String colName) {
		try {
			return new ObjectMapper().readValue((String) row.get(colName), Short[].class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static int[] parseIntJson(LinkedHashMap<String, Object> row, String colName) {
		try {
			return new ObjectMapper().readValue((String) row.get(colName), int[].class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Integer[] parseIntWrapperJson(LinkedHashMap<String, Object> row, String colName) {
		try {
			return new ObjectMapper().readValue((String) row.get(colName), Integer[].class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static long[] parseLongJson(LinkedHashMap<String, Object> row, String colName) {
		try {
			return new ObjectMapper().readValue((String) row.get(colName), long[].class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Long[] parseLongWrapperJson(LinkedHashMap<String, Object> row, String colName) {
		try {
			return new ObjectMapper().readValue((String) row.get(colName), Long[].class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static float[] parseFloatJson(LinkedHashMap<String, Object> row, String colName) {
		try {
			return new ObjectMapper().readValue((String) row.get(colName), float[].class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Float[] parseFloatWrapperJson(LinkedHashMap<String, Object> row, String colName) {
		try {
			return new ObjectMapper().readValue((String) row.get(colName), Float[].class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}


	public static double[] parseDoubleJson(LinkedHashMap<String, Object> row, String colName) {
		try {
			return new ObjectMapper().readValue((String) row.get(colName), double[].class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Double[] parseDoubleWrapperJson(LinkedHashMap<String, Object> row, String colName) {
		try {
			return new ObjectMapper().readValue((String) row.get(colName), Double[].class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static char[] parseCharacterJson(LinkedHashMap<String, Object> row, String colName) {
		try {
			return new ObjectMapper().readValue((String) row.get(colName), char[].class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Character[] parseCharacterWrapperJson(LinkedHashMap<String, Object> row, String colName) {
		try {
			return new ObjectMapper().readValue((String) row.get(colName), Character[].class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String[] parseStringJson(LinkedHashMap<String, Object> row, String colName) {
		try {
			return new ObjectMapper().readValue((String) row.get(colName), String[].class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static boolean[] parseBooleanJson(LinkedHashMap<String, Object> row, String colName) {
		try {
			return new ObjectMapper().readValue((String) row.get(colName), boolean[].class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Boolean[] parseBooleanWrapperJson(LinkedHashMap<String, Object> row, String colName) {
		try {
			return new ObjectMapper().readValue((String) row.get(colName), Boolean[].class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
