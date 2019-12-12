package com.sb.echelon;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.sb.echelon.beans.AnalyzedClass;
import com.sb.echelon.exceptions.NoIdFieldException;
import com.sb.echelon.services.ClassAnalyzer;
import com.sb.echelon.services.ColumnParser;
import com.sb.echelon.services.ParserRecommander;
import com.sb.echelon.services.TypeRecommander;

/**
 * Facade class to interact with the Echelon module.
 * @author Samuel Beausoleil
 *
 */

@Component
@ComponentScan
public final class Echelon {
	
	@Autowired
	private ClassAnalyzer analyzer;
	@Autowired
	private TypeRecommander typeRecommander;
	@Autowired
	private ParserRecommander parserRecommander;
	
	private Map<Class<?>, AnalyzedClass<?>> analyzedClasses = new HashMap<>();
	
	public boolean hasAnalyzed(Class<?> clazz) {
		return analyzedClasses.containsKey(clazz);
	}
	
	@SuppressWarnings("unchecked")
	public <T> AnalyzedClass<T> getAnalyzed(Class<T> clazz) {
		return (AnalyzedClass<T>) analyzedClasses.get(clazz);
	}

	public AnalyzedClass<?> registerAnalyzedClass(Class<?> key, AnalyzedClass<?> value) {
		return analyzedClasses.put(key, value);
	}
	
	public void analyze(Class<?>... classes) throws NoIdFieldException {
		for (Class<?> clazz : classes) {
			analyzedClasses.put(clazz, analyzer.analyze(clazz));
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> AnalyzedClass<T> analyze(Class<T> clazz) throws NoIdFieldException {
		AnalyzedClass<T> analyzed = (AnalyzedClass<T>) analyzedClasses.get(clazz);
		if (analyzed == null) {
			analyzed = analyzer.analyze(clazz);
			analyzedClasses.put(clazz, analyzer.analyze(clazz));
		}
		return analyzed;
	}
	
	@SuppressWarnings("unchecked")
	public <T> AnalyzedClass<T> register(AnalyzedClass<T> analyzed) {
		return (AnalyzedClass<T>) analyzedClasses.put(analyzed.getTargetClass(), analyzed);
	}

	public String putSuggestion(Class<?> forType, String sqlType) {
		return typeRecommander.putSuggestion(forType, sqlType);
	}

	public String putSuggestionIfAbsent(Class<?> forType, String sqlType) {
		return typeRecommander.putSuggestionIfAbsent(forType, sqlType);
	}

	public <T> ColumnParser<T> putParser(Class<T> type, ColumnParser<T> parser) {
		return parserRecommander.putParser(type, parser);
	}

	public <T> ColumnParser<T> putParserIfAbsent(Class<T> type, ColumnParser<T> parser) {
		return parserRecommander.putParserIfAbsent(type, parser);
	}
}
