package com.sb.echelon.beans;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.sb.echelon.ColType;
import com.sb.echelon.beans.ColumnDefinition.Primary;
import com.sb.echelon.exceptions.EchelonRuntimeException;
import com.sb.echelon.interpreters.PrimitiveParser;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@Data
public class AnalyzedClass<T> {
	public static final String CLASS_COLUMN_NAME = "class";
	public static final ColumnDefinition<String> CLASS_COLUMN = new ColumnDefinition<>(AnalyzedClass.CLASS_COLUMN_NAME,
			ColType.VARCHAR.sql, String.class, PrimitiveParser::parseString);

	@NonNull
	private Class<T> targetClass;
	@NonNull
	private Field idField;
	/**
	 * A linked hashmap containing the data for each fields of the Java object and
	 * their associated SQL column.
	 * Key NULL is reserved for the column identifying the class of this row. That
	 * information should be also at the start of the list of informations of this
	 * map.
	 */
	@NonNull
	private LinkedHashMap<Field, ColumnDefinition<?>> fields;
	@NonNull
	private String table;

	@Getter(AccessLevel.NONE)
	private transient final boolean USE_AUTO_GENERATION;

	public AnalyzedClass(@NonNull Class<T> targetClass, @NonNull Field idField,
			@NonNull LinkedHashMap<Field, ColumnDefinition<?>> fields, @NonNull String table) {
		Entry<Field, ColumnDefinition<?>> firstEntry = fields.entrySet().iterator().next();
		if (firstEntry.getValue() != CLASS_COLUMN)
			throw new EchelonRuntimeException("The fields map must start with the class column!");
		if (firstEntry.getKey() != null)
			throw new EchelonRuntimeException("The class column's key must be null!");
		this.targetClass = targetClass;
		this.idField = idField;
		this.fields = fields;
		this.table = table;

		USE_AUTO_GENERATION = fields.values().stream().anyMatch((col) -> col.getPrimary() == Primary.AUTO_GENERATE);
		fields.keySet().forEach(field -> {
			if (field != null)
				field.setAccessible(true);
		});
	}

	public boolean useAutoGeneration() {
		return USE_AUTO_GENERATION;
	}
}
