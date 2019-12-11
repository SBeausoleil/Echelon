package com.sb.echelon.services;

import java.util.LinkedHashMap;
import java.util.function.BiFunction;

public interface ColumnParser<R> extends BiFunction<LinkedHashMap<String, Object>, String, R> {

	R parse(LinkedHashMap<String, Object> row, String colName);
	
	@Override
	default R apply(LinkedHashMap<String, Object> row, String colName) {
		return parse(row, colName);
	}
}
