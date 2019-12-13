package com.sb.echelon.interpreters;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sb.echelon.beans.AnalyzedClass;
import com.sb.echelon.beans.ColumnDefinition;
import com.sb.echelon.exceptions.EchelonRuntimeException;
import com.sb.echelon.exceptions.NoEmptyConstructorException;

@Service
public class ObjectInterpreter {

	public <T> T parse(LinkedHashMap<String, Object> resultRow, AnalyzedClass<T> analyzed) {
		Iterator<Map.Entry<String, Object>> i = resultRow.entrySet().iterator();
		// First column must always be the class name
		Map.Entry<String, Object> firstColumn = i.next();
		if (!firstColumn.getKey().equals(AnalyzedClass.CLASS_COLUMN_NAME))
			throw new EchelonRuntimeException("The first column of a row segment must be the class column.");
		if (!firstColumn.getValue().equals(analyzed.getTargetClass().getName()))
			throw new EchelonRuntimeException("The given analyzed class (" + analyzed.getTargetClass().getName()
					+ ") does not match the class of this row segment (" + firstColumn.getValue() + ")");

		T loaded;
		try {
			loaded = analyzed.getTargetClass().getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new NoEmptyConstructorException(e);
		}
		
		try {
			while (i.hasNext()) {
				Map.Entry<String, Object> col = i.next();
				Map.Entry<Field, ColumnDefinition<?>> definition = analyzed.getFieldsByColName().get(col.getKey());
				definition.getKey().set(loaded, definition.getValue().getParser().parse(resultRow, col.getKey()));
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		return loaded;
	}
}
