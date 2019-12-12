package com.sb.echelon.services;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sb.echelon.Column;
import com.sb.echelon.Echelon;
import com.sb.echelon.Id;
import com.sb.echelon.Table;
import com.sb.echelon.beans.AnalyzedClass;
import com.sb.echelon.beans.ColumnDefinition;
import com.sb.echelon.beans.ColumnDefinition.Primary;
import com.sb.echelon.exceptions.NoIdFieldException;
import com.sb.echelon.interpreters.ColumnParser;
import com.sb.echelon.exceptions.EchelonRuntimeException;
import com.sb.echelon.util.BeanUtil;
import com.sb.echelon.util.FieldUtil;

@Service
public class ClassAnalyzer {

	@Autowired
	private SnakeCaseFormatter formatter;
	@Autowired
	private ColumnService colService;
	@Autowired
	private TypeRecommander typeRecommander;
	@Autowired
	private ParserRecommander parserRecommander;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> AnalyzedClass<T> analyze(Class<T> clazz) {
		String table = tableName(clazz);

		Field[] fields = FieldUtil.getFields(clazz, FieldUtil::isTransient);
		Field idField = findIdField(fields);
		if (idField == null)
			throw new NoIdFieldException(clazz);

		LinkedHashMap<Field, ColumnDefinition<?>> cols = new LinkedHashMap<>(fields.length);
		cols.put(null, AnalyzedClass.CLASS_COLUMN);
		for (int i = 0; i < fields.length; i++) {
			String colName = colName(fields[i]);
			String sqlType = sqlType(fields[i]);
			AnalyzedClass<?> foreign = null;
			if (sqlType == null) {
				foreign = attemptForeign(fields[i]);
				if (foreign == null) {
					throw new EchelonRuntimeException("The class analyzer does not support the type " + fields[i].getType() + " of field " + fields[i].getName() + ".");
				} else {
					sqlType = typeRecommander.getSuggestionFor(foreign.getIdField().getType());
				}
			}
			ColumnParser<?> parser = parserRecommander.getParserFor(fields[i].getType());
			ColumnDefinition definition = new ColumnDefinition(colName, sqlType, fields[i].getType(), parser, foreign);
			if (fields[i] == idField) {
				definition.setPrimary(primary(idField));
			}
			cols.put(fields[i], definition);
		}

		return new AnalyzedClass<>(clazz, idField, cols, table);
	}
	
	private Primary primary(Field idField) {
		var primary = Primary.PRIMARY;
		Id annotation = idField.getAnnotation(Id.class);
		if (annotation != null && annotation.autoGenerate())
			primary = Primary.AUTO_GENERATE;
		return primary;
	}

	private AnalyzedClass<?> attemptForeign(Field field) {
		Echelon echelon = BeanUtil.getBean(Echelon.class);
		AnalyzedClass<?> analyzed = echelon.getAnalyzed(field.getType());
		if (analyzed == null) {
			try {
				analyzed = echelon.analyze(field.getType());
			} catch (NoIdFieldException e) {
				throw new NoIdFieldException(
						"The class " + field.getDeclaringClass().getName() + " member field " + field.getName()
								+ " references a class that is not supported by Echelon."
								+ " You may fix this by either registering your own AnalyzedClass with Echelon"
								+ " and/or by registering a suggested SQL type with Echelon for the vexating type.",
						e);
			}
		}
		return null;
	}

	public String sqlType(Field field) {
		Column annotation = field.getAnnotation(Column.class);
		String type = colService.columnType(annotation);
		if (type == null) { type = typeRecommander.getSuggestionFor(field.getType()); }
		return type;
	}

	/**
	 * Look for a field named ID or with the @Id annotation
	 * 
	 * @param fields
	 * @return the id field or null if none was found.
	 */
	private Field findIdField(Field[] fields) {
		Field idNamed = null;
		Field annotated = null;
		for (Field f : fields) {
			if (f.isAnnotationPresent(Id.class)) {
				annotated = f;
				break;
			}
			if (f.getName().equalsIgnoreCase("id") && (f.getType() == long.class || f.getType() == Long.class))
				idNamed = f;
		}
		return annotated != null ? annotated : idNamed;
	}

	public String colName(Field field) {
		String name;
		Column annotation = field.getAnnotation(Column.class);
		if (annotation != null && !annotation.name().equals(Column.GENERATE_COL_NAME)) {
			name = annotation.name();
		} else {
			name = formatter.apply(field.getName());
		}
		return name;
	}

	public String tableName(Class<?> clazz) {
		String name;
		Table annotation = clazz.getAnnotation(Table.class);
		if (annotation != null) {
			name = annotation.name();
			if (name.equals(Table.GENERATE_TABLE_NAME)) { name = formatter.format(clazz.getSimpleName()); }
		} else {
			throw new IllegalArgumentException("The class " + clazz.getCanonicalName()
					+ " does not have the @Table annotation and thus is not recognized as an Echelon-compatible entity.");
		}
		return name;
	}
}
