package com.sb.echelon.beans;

import org.springframework.lang.Nullable;

import com.sb.echelon.interpreters.ColumnParser;
import com.sb.echelon.interpreters.SqlInsertionPreparer;

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

	@Nullable
	private SqlInsertionPreparer<T> preparer;

	public ColumnDefinition(@NonNull String name, @NonNull String sqlType, @NonNull Class<T> javaType,
			@NonNull ColumnParser<T> parser) {
		this(name, sqlType, javaType, parser, null, null, Primary.NOT_PRIMARY);
	}

	public ColumnDefinition(@NonNull String name, @NonNull String sqlType, @NonNull Class<T> javaType,
			@NonNull ColumnParser<T> parser, AnalyzedClass<T> foreign) {
		this(name, sqlType, javaType, parser, foreign, null, Primary.NOT_PRIMARY);
	}

	public ColumnDefinition(@NonNull String name, @NonNull String sqlType, @NonNull Class<T> javaType,
			@NonNull ColumnParser<T> parser, AnalyzedClass<T> foreign,
			SqlInsertionPreparer<T> preparer) {
		this(name, sqlType, javaType, parser, foreign, preparer, Primary.NOT_PRIMARY);
	}

	public ColumnDefinition(@NonNull String name, @NonNull String sqlType, @NonNull Class<T> javaType,
			@NonNull ColumnParser<T> parser, AnalyzedClass<T> foreign, SqlInsertionPreparer<T> preparer,
			Primary primary) {
		this.name = name;
		this.sqlType = sqlType;
		this.javaType = javaType;
		this.parser = parser;
		this.foreign = foreign;
		this.preparer = preparer;
		this.primary = primary;
	}

	public boolean isPrimary() {
		return primary == Primary.PRIMARY || primary == Primary.AUTO_GENERATE;
	}
	
	public boolean hasPreparer() {
		return preparer != null;
	}
}
