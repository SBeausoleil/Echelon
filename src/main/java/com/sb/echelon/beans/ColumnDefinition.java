package com.sb.echelon.beans;

import com.sb.echelon.interpreters.ColumnParser;

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
public class ColumnDefinition<T> {
	public static enum Primary {
		NOT_PRIMARY, PRIMARY, AUTO_GENERATE;
	};

	@NonNull
	private String name;
	@NonNull
	private String sqlType;
	@NonNull
	private Class<T> javaType;
	@NonNull
	private ColumnParser<T> parser;

	private AnalyzedClass<T> foreign;
	
	private Primary primary;

	public ColumnDefinition(@NonNull String name, @NonNull String sqlType, @NonNull Class<T> javaType,
			@NonNull ColumnParser<T> parser) {
		this(name, sqlType, javaType, parser, null, Primary.NOT_PRIMARY);
	}
	


	public ColumnDefinition(@NonNull String name, @NonNull String sqlType, @NonNull Class<T> javaType,
			@NonNull ColumnParser<T> parser, AnalyzedClass<T> foreign) {
		this(name, sqlType, javaType, parser, foreign, Primary.NOT_PRIMARY);
	}

	public ColumnDefinition(@NonNull String name, @NonNull String sqlType, @NonNull Class<T> javaType,
			@NonNull ColumnParser<T> parser, AnalyzedClass<T> foreign, Primary primary) {
		this.name = name;
		this.sqlType = sqlType;
		this.javaType = javaType;
		this.parser = parser;
		this.foreign = foreign;
		this.primary = primary;
	}

	public boolean isPrimary() {
		return primary == Primary.PRIMARY || primary == Primary.AUTO_GENERATE;
	}
}
