package com.sb.echelon.interpreters;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sb.echelon.Echelon;
import com.sb.echelon.beans.AnalyzedClass;
import com.sb.echelon.beans.ColumnDefinition;
import com.sb.echelon.exceptions.EchelonRuntimeException;
import com.sb.echelon.exceptions.NoEmptyConstructorException;
import com.sb.echelon.util.BeanUtil;

@Service
public class ObjectInterpreter {

	public <T> T parse(LinkedHashMap<String, Object>[] resultRow, AnalyzedClass<T> analyzed) {
		return parse(resultRow, analyzed, null, 0);
	}

	public <T> T parse(LinkedHashMap<String, Object>[] resultRow, AnalyzedClass<T> analyzed,
			LinkedList<Object> previous, int index) {
		if (previous == null)
			previous = new LinkedList<>();

		LinkedHashMap<String, Object> rowFragment = resultRow[index];
		Iterator<Map.Entry<String, Object>> i = rowFragment.entrySet().iterator();
		sanityCheck(analyzed, i);

		T loaded;
		try {
			loaded = analyzed.getTargetClass().getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new NoEmptyConstructorException(e);
		}
		previous.add(loaded);

		try {
			while (i.hasNext()) {
				Map.Entry<String, Object> entry = i.next();
				if (entry.getValue() == null)
					continue;

				Map.Entry<Field, ColumnDefinition<?>> definition = analyzed.getFieldsByColName().get(entry.getKey());
				if (definition.getValue().getParser() != null)
					definition.getKey().set(loaded,
							definition.getValue().getParser().parse(rowFragment, entry.getKey()));
				else if (definition.getValue().isForeign()) {
					handleForeignRelation(resultRow, previous, loaded, entry, definition);
				} else if (definition.getValue().isPolymorphic()) {
					long idForeign = (long) entry.getValue();
					String type = (String) rowFragment.get(definition.getValue().polymorphicTypeColName());
					if (type == null)
						throw new EchelonRuntimeException("Error: the field " + definition.getKey().getName()
								+ " is polymorphic, yet there is no identifier for the type of the linked object.");
					Echelon echelon =  BeanUtil.getBean(Echelon.class);
					AnalyzedClass<?> polymorphicInstance = echelon.analyze(type);
					definition.getKey().set(loaded, echelon.load(polymorphicInstance.getTargetClass(), idForeign));
				} else
					throw new IllegalStateException("The column definition " + definition.getValue().getName()
							+ " of type " + analyzed.getTargetClass().getName()
							+ " has no parser yet is not a foreign relation.");
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("The class pointed to by a polymorphic field could not be found in this Java runtime.", e);
		}

		return loaded;
	}

	private <T> void handleForeignRelation(LinkedHashMap<String, Object>[] resultRow, LinkedList<Object> previous,
			T loaded, Map.Entry<String, Object> entry, Map.Entry<Field, ColumnDefinition<?>> definition)
			throws IllegalAccessException {
		// Handle foreign relation
		long foreignId = (long) entry.getValue();
		AnalyzedClass<?> targetClass = definition.getValue().getForeign();
		// Check in previous if it has already started being unmarshalled
		Object relationTarget = findIn(previous, targetClass, foreignId);

		if (relationTarget == null) {
			// Check in the fragments
			int newTargetIndex = findIndexOfTargetedFragment(resultRow, targetClass, foreignId);
			if (newTargetIndex != -1)
				relationTarget = parse(resultRow, targetClass, previous, newTargetIndex);
		}

		definition.getKey().set(loaded, relationTarget);
	}

	private void sanityCheck(AnalyzedClass<?> analyzed, Iterator<Map.Entry<String, Object>> i) {
		// First column must always be the class name
		Map.Entry<String, Object> firstColumn = i.next();
		if (!firstColumn.getKey().equals(AnalyzedClass.CLASS_COLUMN_NAME))
			throw new EchelonRuntimeException("The first column of a row segment must be the class column.");
		if (!firstColumn.getValue().equals(analyzed.getTargetClass().getName()))
			throw new EchelonRuntimeException("The given analyzed class (" + analyzed.getTargetClass().getName()
					+ ") does not match the class of this row segment (" + firstColumn.getValue() + ")");
	}

	private Object findIn(LinkedList<Object> previous, AnalyzedClass<?> targetClass, long id)
			throws IllegalArgumentException, IllegalAccessException {
		for (Object obj : previous) {
			if (obj.getClass() == targetClass.getClass() && (long) targetClass.getIdField().get(obj) == id)
				return obj;
		}
		return null;
	}

	protected int findIndexOfTargetedFragment(LinkedHashMap<String, Object>[] resultRow, AnalyzedClass<?> toFind,
			long id) {
		for (int index = 0; index < resultRow.length; index++) {
			if (resultRow[index].get(AnalyzedClass.CLASS_COLUMN_NAME).equals(toFind.getTargetClass().getName())
					&& (long) resultRow[index].get(toFind.getIdCol().getName()) == id)
				return index;
		}
		return -1;
	}
}
