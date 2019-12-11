package com.sb.echelon.services;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sb.echelon.Column;
import com.sb.echelon.Table;
import com.sb.echelon.beans.AnalyzedClass;
import com.sb.echelon.beans.ColumnDefinition;
import com.sb.echelon.util.FieldUtil;

@Service
public class ClassAnalyzer {

	@Autowired
	private SnakeCaseFormatter formatter;
	@Autowired
	private ColumnService colService;
	@Autowired
	private TypeRecommander typeRecommander;

	public <T> AnalyzedClass<T> analyze(Class<T> clazz) {
		String table = tableName(clazz);

		Field[] fields = FieldUtil.getFields(clazz, FieldUtil::isTransient);
		ColumnDefinition<?>[] cols = new ColumnDefinition[fields.length];

		for (int i = 0; i < fields.length; i++) {
			String colName = colName(fields[i]);
			String sqlType = sqlType(fields[i]);
			ColumnParser<?> parser = parser(fields[i]);
		}
		return null;
	}

	public ColumnParser<?> parser(Field field) {
		// TODO Auto-generated method stub
		return null;
	}

	public String sqlType(Field field) {
		Column annotation = field.getAnnotation(Column.class);
		String type = colService.columnType(annotation);
		if (type == null) {
			type = typeRecommander.getSuggestionFor(field.getType());
		}
		return type;
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
			if (name.equals(Table.GENERATE_TABLE_NAME)) {
				name = formatter.format(clazz.getSimpleName());
			}
		} else {
			throw new IllegalArgumentException("The class " + clazz.getCanonicalName() + " does not have the @Table annotation and thus is not recognized as an Echelon-compatible entity.");
		}
		return name;
	}
}
