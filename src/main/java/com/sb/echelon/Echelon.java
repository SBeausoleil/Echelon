package com.sb.echelon;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.sb.echelon.beans.AnalyzedClass;
import com.sb.echelon.exceptions.NoIdFieldException;
import com.sb.echelon.interpreters.ColumnParser;
import com.sb.echelon.interpreters.SqlInsertionPreparer;
import com.sb.echelon.services.ClassAnalyzer;
import com.sb.echelon.services.InsertionPreparerRecommander;
import com.sb.echelon.services.ObjectLoader;
import com.sb.echelon.services.ParserRecommander;
import com.sb.echelon.services.SaveWriter;
import com.sb.echelon.services.TableWriter;
import com.sb.echelon.services.TypeRecommander;

import lombok.NonNull;

/**
 * Facade class to interact with the Echelon module.
 * 
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
	@Autowired
	private InsertionPreparerRecommander preparerRecommander;
	@Autowired
	private TableWriter tableWriter;
	@Autowired
	private SaveWriter saveWriter;
	@Autowired
	private ObjectLoader loader;

	private Map<Class<?>, AnalyzedClass<?>> analyzedClasses = new HashMap<>();

	private HashSet<AnalyzedClass<?>> existingTables = new HashSet<>();

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
			register(analyzed);
		}
		return analyzed;
	}

	@SuppressWarnings("unchecked")
	public <T> AnalyzedClass<T> getAnalyzed(Class<T> clazz) {
		return (AnalyzedClass<T>) analyzedClasses.get(clazz);
	}

	/**
	 * Get a map of all the analyzed classes.
	 * 
	 * @return an unmodifiable map of the analyzed classes.
	 */
	public Map<Class<?>, AnalyzedClass<?>> getAnalyzedClasses() {
		return Collections.unmodifiableMap(analyzedClasses);
	}

	public <T> ColumnParser<T> getParserFor(Class<T> type) {
		return parserRecommander.getParserFor(type);
	}

	public <T> SqlInsertionPreparer<T> getPreparerFor(Class<T> type) {
		return preparerRecommander.getPreparerFor(type);
	}

	public String getSuggestionFor(Class<?> type) {
		return typeRecommander.getSuggestionFor(type);
	}

	public boolean hasAnalyzed(Class<?> clazz) {
		return analyzedClasses.containsKey(clazz);
	}

	/**
	 * Load an object of the desired class.
	 * 
	 * @param <T>   the type of the object
	 * @param clazz the class of the object
	 * @param id    the object's id
	 * @return the object or null if none was found.
	 */
	public <T> T load(Class<T> clazz, long id) {
		AnalyzedClass<T> analyzed = (AnalyzedClass<T>) analyze(clazz);
		if (analyzed == null)
			analyzed = (AnalyzedClass<T>) analyze(clazz);
		return loader.load(analyzed, id);
	}

	public <T> ArrayList<T> loadRaw(String sql, Object[] args, Class<T> target) {
		return loader.loadFromRaw(sql, args, (AnalyzedClass<T>) analyze(target));
	}

	public <T> ColumnParser<T> putParser(Class<T> type, ColumnParser<T> parser) {
		return parserRecommander.putParser(type, parser);
	}

	public <T> ColumnParser<T> putParserIfAbsent(Class<T> type, ColumnParser<T> parser) {
		return parserRecommander.putParserIfAbsent(type, parser);
	}

	public <T> SqlInsertionPreparer<T> putPreparer(Class<T> type, SqlInsertionPreparer<T> parser) {
		return preparerRecommander.putPreparer(type, parser);
	}

	public <T> SqlInsertionPreparer<T> putPreparerIfAbsent(Class<T> type, SqlInsertionPreparer<T> parser) {
		return preparerRecommander.putPreparerIfAbsent(type, parser);
	}

	public String putSuggestion(Class<?> forType, String sqlType) {
		return typeRecommander.putSuggestion(forType, sqlType);
	}

	public String putSuggestionIfAbsent(Class<?> forType, String sqlType) {
		return typeRecommander.putSuggestionIfAbsent(forType, sqlType);
	}

	public String putTypeSuggestion(Class<?> forType, String sqlType) {
		return typeRecommander.putSuggestion(forType, sqlType);
	}

	public String putTypeSuggestionIfAbsent(Class<?> forType, String sqlType) {
		return typeRecommander.putSuggestionIfAbsent(forType, sqlType);
	}

	@SuppressWarnings("unchecked")
	public <T> AnalyzedClass<T> register(AnalyzedClass<T> analyzed) {
		try {
			if (!tableWriter.tableExists(analyzed)) {
				writeTable(analyzed);
				existingTables.add(analyzed);
			} else if (!existingTables.contains(analyzed)) {
				existingTables.add(analyzed);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return (AnalyzedClass<T>) analyzedClasses.put(analyzed.getTargetClass(), analyzed);
	}

	/**
	 * Save an object in the database.
	 * 
	 * @param <T> the type of the object
	 * @param obj the object to save
	 * @return the object after the save operation. If it was attributed an ID
	 *         during the operation, it will have been set.
	 */
	@SuppressWarnings("unchecked")
	public <T> T save(T obj) {
		AnalyzedClass<T> analyzed = (AnalyzedClass<T>) analyzedClasses.get(obj.getClass());
		if (analyzed == null)
			analyzed = (AnalyzedClass<T>) analyze(obj.getClass());
		return saveWriter.save(obj, analyzed);
	}

	protected void writeTable(AnalyzedClass<?> analyzed) {
		tableWriter.writeTable(analyzed);
	}

	/**
	 * Loads the class associated with that name and analyzes it.
	 * 
	 * @param className the name of the class acquired using
	 *                  {@link Class#getName()}.
	 * @return the analysis of that class.
	 * @throws ClassNotFoundException 
	 */
	public AnalyzedClass<?> analyze(@NonNull String className) throws ClassNotFoundException {
		// First, try to look through the desired class (this risk causing less runtime exceptions)
		for (Entry<Class<?>, AnalyzedClass<?>> entry : analyzedClasses.entrySet())
			if (entry.getClass().getName().equals(className))
				return entry.getValue();
		Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(className);
		return analyze(clazz);
	}
}
