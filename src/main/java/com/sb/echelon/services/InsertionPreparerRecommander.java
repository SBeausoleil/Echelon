package com.sb.echelon.services;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.sb.echelon.interpreters.ColumnParser;
import com.sb.echelon.interpreters.CommonPreparers;
import com.sb.echelon.interpreters.SqlInsertionPreparer;

@Component
public class InsertionPreparerRecommander {

	private HashMap<Class<?>, SqlInsertionPreparer<?>> suggestions;

	protected InsertionPreparerRecommander() {
		suggestions = defaultSuggestions();
	}

	protected HashMap<Class<?>, SqlInsertionPreparer<?>> defaultSuggestions() {
		HashMap<Class<?>, SqlInsertionPreparer<?>> map = new HashMap<>();
		map.put(char.class, CommonPreparers::prepareChar);
		map.put(Character.class, CommonPreparers::prepareCharWrapper);
		return map;
	}

	/**
	 * Get an appropriate column parser for the desired type.
	 * @param type
	 * @return
	 * @throws ClassCastException
	 *             if the parser that is associated with the given type does not
	 *             actually support the type. Unlikely to happen if it was placed in
	 *             the suggestions list with
	 *             {@link #putParser(Class, ColumnParser)}.
	 */
	@SuppressWarnings("unchecked") // Desired risk of exception.
	public <T> SqlInsertionPreparer<T> getParserFor(Class<T> type) {
		return (SqlInsertionPreparer<T>) suggestions.get(type);
	}

	@SuppressWarnings("unchecked")
	public <T> SqlInsertionPreparer<T> putParser(Class<T> type, SqlInsertionPreparer<T> parser) {
		return (SqlInsertionPreparer<T>) suggestions.put(type, parser);
	}
	
	@SuppressWarnings("unchecked")
	public <T> SqlInsertionPreparer<T> putParserIfAbsent(Class<T> type, SqlInsertionPreparer<T> parser) {
		return (SqlInsertionPreparer<T>) suggestions.putIfAbsent(type, parser);
	}
}
