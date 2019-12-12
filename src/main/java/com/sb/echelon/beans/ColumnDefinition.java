package com.sb.echelon.beans;

import com.sb.echelon.services.ColumnParser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Definition of an SQL Column.
 * 
 * @author Samuel Beausoleil
 *
 * @param <T>
 *            the matching Java type of the column's content.
 */
@Data
@AllArgsConstructor
public class ColumnDefinition<T> {

	@NonNull
	private String name;
	@NonNull
	private String sqlType;
	@NonNull
	private Class<T> javaType;
	@NonNull
	private ColumnParser<T> parser;

	private AnalyzedClass<T> foreign;
	
}
