package com.sb.echelon.services;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.sb.echelon.interpreters.ColumnParser;
import com.sb.echelon.interpreters.PrimitiveParser;

@Component
public class ParserRecommander {
	/**
	 * key: the Java type
	 * value: a parser to the Java type
	 */
	protected HashMap<Class<?>, ColumnParser<?>> suggestions;

	protected ParserRecommander() {
		suggestions = defaultSuggestions();
	}

	protected HashMap<Class<?>, ColumnParser<?>> defaultSuggestions() {
		HashMap<Class<?>, ColumnParser<?>> map = new HashMap<>(33);
		map.put(byte.class, PrimitiveParser::parseByte);
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
		map.put(Short[].class, PrimitiveParser::parseShortWrapperJson);
		map.put(int[].class, PrimitiveParser::parseIntJson);
		map.put(Integer[].class, PrimitiveParser::parseIntWrapperJson);
		map.put(long[].class, PrimitiveParser::parseLongJson);
		map.put(Long[].class, PrimitiveParser::parseLongWrapperJson);
		map.put(float[].class, PrimitiveParser::parseFloatJson);
		map.put(Float[].class, PrimitiveParser::parseFloatWrapperJson);
		map.put(double[].class, PrimitiveParser::parseDoubleJson);
		map.put(Double[].class, PrimitiveParser::parseDoubleWrapperJson);
		map.put(char[].class, PrimitiveParser::parseVarcharToCharArray);
		map.put(Character[].class, PrimitiveParser::parseCharacterWrapperJson);
		map.put(String[].class, PrimitiveParser::parseStringJson);
		map.put(boolean[].class, PrimitiveParser::parseBooleanJson);
		map.put(Boolean[].class, PrimitiveParser::parseBooleanWrapperJson);
		return map;
	}

	/**
	 * Get an appropriate column parser for the desired type.
	 * @param type
	 * @return
	 * @throws ClassCastException
	 *             if the parser that is associated with the given type does not
	 *             actually support the type. Unlikely to happen if it was placed in
	 *             the suggestions list with
	 *             {@link #putParser(Class, ColumnParser)}.
	 */
	@SuppressWarnings("unchecked") // Desired risk of exception.
	public <T> ColumnParser<T> getParserFor(Class<T> type) {
		return (ColumnParser<T>) suggestions.get(type);
	}

	@SuppressWarnings("unchecked")
	public <T> ColumnParser<T> putParser(Class<T> type, ColumnParser<T> parser) {
		return (ColumnParser<T>) suggestions.put(type, parser);
	}
	
	@SuppressWarnings("unchecked")
	public <T> ColumnParser<T> putParserIfAbsent(Class<T> type, ColumnParser<T> parser) {
		return (ColumnParser<T>) suggestions.putIfAbsent(type, parser);
	}
}
