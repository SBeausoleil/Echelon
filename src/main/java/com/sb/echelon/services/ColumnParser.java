package com.sb.echelon.services;

import java.util.LinkedHashMap;
import java.util.function.BiFunction;

import org.springframework.lang.Nullable;

@FunctionalInterface
public interface ColumnParser<R> extends BiFunction<LinkedHashMap<String, Object>, String, R> {

	R parse(LinkedHashMap<String, Object> row, String colName);
	
	@Override
	default @Nullable R apply(LinkedHashMap<String, Object> row, String colName) {
		return parse(row, colName);
	}
}
