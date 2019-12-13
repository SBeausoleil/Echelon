package com.sb.echelon.interpreters;

import java.util.LinkedHashMap;
import java.util.function.BiFunction;

import org.springframework.lang.Nullable;

@FunctionalInterface
public interface ColumnParser<T> extends BiFunction<LinkedHashMap<String, Object>, String, T> {

	T parse(LinkedHashMap<String, Object> rowFragment, String colName);
	
	@Override
	default @Nullable T apply(LinkedHashMap<String, Object> rowFragment, String colName) {
		return parse(rowFragment, colName);
	}
}
