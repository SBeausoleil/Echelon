package com.sb.echelon.beans;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import lombok.Data;
import lombok.NonNull;

@Data
public class AnalyzedClass<T> {
	@NonNull
	private Class<T> clazz;
	@NonNull
	private LinkedHashMap<Field, ColumnDefinition<?>> fields;
	@NonNull
	private String table;
}
