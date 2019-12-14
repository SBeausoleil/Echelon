package com.sb.echelon.beans;

import org.springframework.lang.Nullable;

import com.sb.echelon.exceptions.EchelonRuntimeException;
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
	
	private ColumnParser<T> parser;

	private AnalyzedClass<T> foreign;

	private Primary primary;
	
	private boolean isPolymorphic;

	@Nullable
	private SqlInsertionPreparer<T> preparer;
	
	public ColumnDefinition(@NonNull String name, @NonNull String sqlType, @NonNull Class<T> javaType,
			ColumnParser<T> parser) {
		this(name, sqlType, javaType, parser, null, Primary.NOT_PRIMARY, false, null);
	}

	public ColumnDefinition(@NonNull String name, @NonNull String sqlType, @NonNull Class<T> javaType,
			ColumnParser<T> parser, AnalyzedClass<T> foreign, Primary primary, boolean isPolymorphic,
			SqlInsertionPreparer<T> preparer) {
		
		if (parser == null && foreign == null && !isPolymorphic)
			throw new NullPointerException("parser may only be null for a column with a foreign value or that is polymorphic.");
		
		this.name = name;
		this.sqlType = sqlType;
		this.javaType = javaType;
		this.parser = parser;
		this.foreign = foreign;
		this.primary = primary;
		this.isPolymorphic = isPolymorphic;
		this.preparer = preparer;
	}

	public boolean isPrimary() {
		return primary == Primary.PRIMARY || primary == Primary.AUTO_GENERATE;
	}
	
	public boolean hasPreparer() {
		return preparer != null;
	}
	
	public boolean isForeign() {
		return foreign != null;
	}
	
	public void setPolymorphic(boolean isPolymorphic) {
		if (isPolymorphic && foreign != null)
			throw new EchelonRuntimeException("A field may not be both polymorphic and a foreign key.");
		this.isPolymorphic = isPolymorphic;
	}
	
	public String getPolymorphicTypeColName() {
		if (isPolymorphic)
			return name + "_type";
		return null;
	}

}
