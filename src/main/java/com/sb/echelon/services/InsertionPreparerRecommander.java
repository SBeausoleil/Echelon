package com.sb.echelon.services;

import java.util.HashMap;

import org.springframework.stereotype.Component;

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

		map.put(short[].class, CommonPreparers::toJson);
		map.put(Short[].class, CommonPreparers::toJson);
		map.put(String[].class, CommonPreparers::toJson);
		map.put(int[].class, CommonPreparers::toJson);
		map.put(Integer[].class, CommonPreparers::toJson);
		map.put(long[].class, CommonPreparers::toJson);
		map.put(Long[].class, CommonPreparers::toJson);
		map.put(float[].class, CommonPreparers::toJson);
		map.put(Float[].class, CommonPreparers::toJson);
		map.put(double[].class, CommonPreparers::toJson);
		map.put(Double[].class, CommonPreparers::toJson);
		map.put(char[].class, CommonPreparers::prepareCharArray);
		map.put(Character[].class, CommonPreparers::toJson);
		map.put(String[].class, CommonPreparers::toJson);
		map.put(boolean[].class, CommonPreparers::toJson);
		map.put(Boolean[].class, CommonPreparers::toJson);
		
		return map;
	}

	@SuppressWarnings("unchecked") // Desired risk of exception.
	public <T> SqlInsertionPreparer<T> getPreparerFor(Class<T> type) {
		return (SqlInsertionPreparer<T>) suggestions.get(type);
	}

	@SuppressWarnings("unchecked")
	public <T> SqlInsertionPreparer<T> putPreparer(Class<T> type, SqlInsertionPreparer<T> parser) {
		return (SqlInsertionPreparer<T>) suggestions.put(type, parser);
	}

	@SuppressWarnings("unchecked")
	public <T> SqlInsertionPreparer<T> putPreparerIfAbsent(Class<T> type, SqlInsertionPreparer<T> parser) {
		return (SqlInsertionPreparer<T>) suggestions.putIfAbsent(type, parser);
	}
}
