package com.sb.echelon.services;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.sb.echelon.ColType;
import com.sb.echelon.parsers.PrimitiveParser;

@Component
public class ParserRecommander {
	/**
	 * key: the SQL type
	 * value: a parser to the Java type
	 */
	private HashMap<String, ColumnParser<?>> suggestions;
	
	private ParserRecommander(TypeRecommander typeRecommander) {
		suggestions = defaultSuggestions(typeRecommander);
	}
	
	protected HashMap<String, ColumnParser<?>> defaultSuggestions(TypeRecommander typeRecommander) {
		HashMap<String, ColumnParser<?>> map = new HashMap<>();

		map.put(typeRecommander.getSuggestionFor(byte.class), PrimitiveParser::parseByte);
		map.put(typeRecommander.getSuggestionFor(Byte.class), PrimitiveParser::parseByteWrapper);
		map.put(typeRecommander.getSuggestionFor(short.class), PrimitiveParser::parseShort);
		map.put(typeRecommander.getSuggestionFor(Short.class), PrimitiveParser::parseShortWrapper);
		map.put(typeRecommander.getSuggestionFor(int.class), PrimitiveParser::parseInt);
		map.put(typeRecommander.getSuggestionFor(Integer.class), PrimitiveParser::parseInteger);
		map.put(typeRecommander.getSuggestionFor(long.class), PrimitiveParser::parseLong);
		map.put(typeRecommander.getSuggestionFor(Long.class), PrimitiveParser::parseLongWrapper);
		map.put(typeRecommander.getSuggestionFor(float.class), PrimitiveParser::parseFloat);
		map.put(typeRecommander.getSuggestionFor(Float.class), PrimitiveParser::parseFloatWrapper);
		map.put(typeRecommander.getSuggestionFor(double.class), PrimitiveParser::parseDouble);
		map.put(typeRecommander.getSuggestionFor(Double.class), PrimitiveParser::parseDoubleWrapper);

		map.put(typeRecommander.getSuggestionFor(char.class), PrimitiveParser::parseChar);
		map.put(typeRecommander.getSuggestionFor(Character.class), PrimitiveParser::parseCharacter);
		map.put(typeRecommander.getSuggestionFor(String.class), PrimitiveParser::parseString);
		map.put(ColType.TEXT.sql, PrimitiveParser::parseString);
		map.put(ColType.LONGTEXT.sql, PrimitiveParser::parseString);

		map.put(typeRecommander.getSuggestionFor(boolean.class), ColType.VARCHAR.sql);
		map.put(typeRecommander.getSuggestionFor(Boolean.class), ColType.VARCHAR.sql);

		map.put(typeRecommander.getSuggestionFor(byte[].class), ColType.BLOB.sql);
		map.put(typeRecommander.getSuggestionFor(Byte[].class), ColType.BLOB.sql);

		map.put(typeRecommander.getSuggestionFor(short[].class), ColType.JSON.sql);
		map.put(typeRecommander.getSuggestionFor(Short[].class), ColType.JSON.sql);
		map.put(typeRecommander.getSuggestionFor(int[].class), ColType.JSON.sql);
		map.put(typeRecommander.getSuggestionFor(Integer[].class), ColType.JSON.sql);
		map.put(typeRecommander.getSuggestionFor(long[].class), ColType.JSON.sql);
		map.put(typeRecommander.getSuggestionFor(Long[].class), ColType.JSON.sql);
		map.put(typeRecommander.getSuggestionFor(float[].class), ColType.JSON.sql);
		map.put(typeRecommander.getSuggestionFor(Float[].class), ColType.JSON.sql);
		map.put(typeRecommander.getSuggestionFor(double[].class), ColType.JSON.sql);
		map.put(typeRecommander.getSuggestionFor(Double[].class), ColType.JSON.sql);
		map.put(typeRecommander.getSuggestionFor(char[].class), ColType.VARCHAR.sql);
		map.put(typeRecommander.getSuggestionFor(Character[].class), ColType.VARCHAR.sql);
		map.put(typeRecommander.getSuggestionFor(String[].class), ColType.JSON.sql);
		
		return map;
	}
}
