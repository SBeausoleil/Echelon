package com.sb.echelon.services;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.sb.echelon.interpreters.StatementArgumentWriter;

@Component
public class ArgumentWriterRecommander {

	protected HashMap<Class<?>, StatementArgumentWriter<?>> suggestions;
	
	protected ArgumentWriterRecommander() {
		
	}
	
	protected HashMap<Class<?>, StatementArgumentWriter<?>> defaultSuggestions() {
		HashMap<Class<?>, StatementArgumentWriter<?>> map = new HashMap<>(33);
		
		/*map.put(byte.class, PrimitiveParser::parseByte);
		map.put(Byte.class, PrimitiveParser::parseByteWrapper);
		map.put(short.class, PrimitiveParser::parseShort);
		map.put(Short.class, PrimitiveParser::parseShortWrapper);
		map.put(int.class, PrimitiveParser::parseInt);
		map.put(Integer.class, PrimitiveParser::parseInteger);
		map.put(long.class, PrimitiveParser::parseLong);
		map.put(Long.class, PrimitiveParser::parseLongWrapper);
		map.put(float.class, PrimitiveParser::parseFloat);
		map.put(Float.class, PrimitiveParser::parseFloatWrapper);
		map.put(double.class, PrimitiveParser::parseDouble);
		map.put(Double.class, PrimitiveParser::parseDoubleWrapper);

		map.put(char.class, PrimitiveParser::parseChar);
		map.put(Character.class, PrimitiveParser::parseCharacter);
		map.put(String.class, PrimitiveParser::parseString);

		map.put(boolean.class, PrimitiveParser::parseBoolean);
		map.put(Boolean.class, PrimitiveParser::parseBooleanWrapper);

		map.put(byte[].class, PrimitiveParser::parseBlob);
		map.put(Byte[].class, PrimitiveParser::parseBlob);

		map.put(short[].class, PrimitiveParser::parseShortJson);
		map.put(Short[].class, PrimitiveParser::parseShortJson);
		map.put(int[].class, PrimitiveParser::parseIntJson);
		map.put(Integer[].class, PrimitiveParser::parseIntJson);
		map.put(long[].class, PrimitiveParser::parseLongJson);
		map.put(Long[].class, PrimitiveParser::parseLongJson);
		map.put(float[].class, PrimitiveParser::parseFloatJson);
		map.put(Float[].class, PrimitiveParser::parseFloatJson);
		map.put(double[].class, PrimitiveParser::parseDoubleJson);
		map.put(Double[].class, PrimitiveParser::parseDoubleJson);
		map.put(char[].class, PrimitiveParser::parseString);
		map.put(Character[].class, PrimitiveParser::parseCharacterJson);
		map.put(String[].class, PrimitiveParser::parseJson);*/
		
		return map;
	}
}
