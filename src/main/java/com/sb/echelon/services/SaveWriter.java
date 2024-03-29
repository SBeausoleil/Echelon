package com.sb.echelon.services;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.sb.echelon.Echelon;
import com.sb.echelon.beans.AnalyzedClass;
import com.sb.echelon.beans.ColumnDefinition;
import com.sb.echelon.exceptions.IdNotSetException;
import com.sb.echelon.util.ArrayUtil;
import com.sb.echelon.util.BeanUtil;

import lombok.NonNull;

@Service
public class SaveWriter {

	@Autowired
	private JdbcTemplate jdbc;

	public <T> T save(T obj, AnalyzedClass<T> analyzed) {
		try {
			Long id = (Long) analyzed.getIdField().get(obj);
			if ((id == null || id == 0l) && analyzed.useAutoGeneration()) {
				// This auto generated object has not been previous been saved, as his ID is null.
				insertAutoGeneratedBean(obj, analyzed);
			} else if (id == null || id == 0l) {
				throw new IdNotSetException("The object " + obj + " does not use ID autogeneration but has no ID set.");
			} else {
				// Perform an insert or update operation.
				insertOrUpdate(obj, analyzed);
			}
			return obj;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param <T>
	 * @param obj
	 * @param analyzed
	 * @throws IllegalAccessException
	 */
	public <T> void insertOrUpdate(T obj, AnalyzedClass<T> analyzed) throws IllegalAccessException {
		String sql = saveStatement(analyzed, true) + onDuplicateKeyUpdateStatement(analyzed);
		System.out.println(sql);

		Object[] insertArgs = makeInsertArguments(analyzed, obj, true);
		Object[] updateArgs = makeOnDuplicateArguments(analyzed, obj);
		Object[] args = ArrayUtil.concatenate(insertArgs, updateArgs,
				new Object[insertArgs.length + updateArgs.length]);

		jdbc.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql);
			var setter = new ArgumentPreparedStatementSetter(args);
			setter.setValues(ps);
			System.out.println(ps);
			return ps;
		});
	}

	public <T> void insertAutoGeneratedBean(T obj, AnalyzedClass<T> analyzed) throws IllegalAccessException {
		String sql = saveStatement(analyzed);
		System.out.println(sql);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(connection -> {
			try {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				Object[] args = makeInsertArguments(analyzed, obj);
				var setter = new ArgumentPreparedStatementSetter(args);
				setter.setValues(ps);
				System.out.println(ps);
				return ps;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}, keyHolder);
		analyzed.getIdField().set(obj, keyHolder.getKey().longValue());
	}

	public <T> Object[] makeInsertArguments(AnalyzedClass<T> analyzed, T obj)
			throws IllegalArgumentException, IllegalAccessException {
		return makeInsertArguments(analyzed, obj, false);
	}

	public <T> Object[] makeInsertArguments(AnalyzedClass<T> analyzed, T obj, boolean forceIdField)
			throws IllegalArgumentException, IllegalAccessException {

		Object[] args = new Object[nInsertArguments(analyzed, forceIdField)];

		int i = 0;
		for (Entry<Field, ColumnDefinition<?>> entry : analyzed.getFields().entrySet()) {
			if (analyzed.useAutoGeneration()
					&& entry.getValue().isPrimary()
					&& !forceIdField)
				continue;

			i = enterArgument(analyzed, obj, args, i, entry);
			i++;
		}
		return args;
	}

	private <T> int nInsertArguments(AnalyzedClass<T> analyzed, boolean forceIdField) {
		int nArguments = analyzed.useAutoGeneration() && !forceIdField
				? analyzed.getFields().size() - 1
				: analyzed.getFields().size();

		for (ColumnDefinition<?> col : analyzed.getFields().values())
			if (col.isPolymorphic())
				nArguments++; // Make room for the type indicator

		return nArguments;
	}

	protected <T> int enterArgument(AnalyzedClass<T> analyzed, T obj, Object[] args, int index,
			Entry<Field, ColumnDefinition<?>> entry) throws IllegalAccessException {
		if (entry.getKey() != null) {
			if (entry.getValue().isForeign()) {
				save(entry.getKey().get(obj), (AnalyzedClass<Object>) entry.getValue().getForeign());
			} else if (entry.getValue().isPolymorphic()) {
				Object currentPolymorphicValue = entry.getKey().get(obj);
				AnalyzedClass<?> analysisOfPolymorphic = BeanUtil.getBean(Echelon.class)
						.analyze(currentPolymorphicValue.getClass());
				save(currentPolymorphicValue, (AnalyzedClass<Object>) analysisOfPolymorphic);
				args[index++] = currentPolymorphicValue.getClass().getName();
			}

			if (!entry.getValue().hasPreparer()) {
				args[index] = entry.getKey().get(obj);
			} else {
				args[index] = entry.getValue().getPreparer().prepare(entry.getKey().get(obj));
			}
		} else
			args[index] = analyzed.getTargetClass().getName();
		return index;
	}

	/**
	 * Generate the SQL statement that allows for insertion into the DB for a given class.
	 * 
	 * @param analyzed
	 * @return
	 */
	public String saveStatement(@NonNull AnalyzedClass<?> analyzed) {
		return saveStatement(analyzed, false);
	}

	/**
	 * Generate the SQL statement that allows for insertion into the DB for a given class.
	 * 
	 * @param <T>
	 * @param analyzed
	 * @param forceIdField
	 *            if the analyzed class uses auto generation for it's key, normally that column is not included in the
	 *            insert statement.
	 *            If this is set to true, it will be included even if auto generation is true.
	 * @return
	 */
	public String saveStatement(@NonNull AnalyzedClass<?> analyzed, boolean forceIdField) {
		StringBuilder builder = new StringBuilder("INSERT INTO ");
		int nFields = 0;
		builder.append(analyzed.getTable() + " (");
		for (ListIterator<ColumnDefinition<?>> i = new LinkedList<ColumnDefinition<?>>(analyzed.getFields().values())
				.listIterator(); i.hasNext();) {
			ColumnDefinition<?> col = i.next();

			if (isSkipped(analyzed, forceIdField, col))
				continue;

			if (col.isPolymorphic()) {
				builder.append(col.getPolymorphicTypeColName() + ", ");
				nFields++;
			}
			builder.append(col.getName());
			nFields++;
			if (i.hasNext()) {
				if (!isSkipped(analyzed, forceIdField, i.next()) || i.hasNext())
					builder.append(", ");
				i.previous();
			}
		}
		builder.append(") VALUES (");

		// Add one less ? if the id won't be inserted
		for (int i = 0; i < nFields; i++) {
			builder.append("?");
			if (i + 1 < nFields)
				builder.append(", ");
		}

		builder.append(")");
		return builder.toString();
	}

	/**
	 * @param analyzed
	 * @param forceIdField
	 * @param col
	 * @return
	 */
	private boolean isSkipped(AnalyzedClass<?> analyzed, boolean forceIdField, ColumnDefinition<?> col) {
		return analyzed.useAutoGeneration() && col.isPrimary() && !forceIdField;
	}

	public String onDuplicateKeyUpdateStatement(@NonNull AnalyzedClass<?> analyzed) {
		var builder = new StringBuilder(" ON DUPLICATE KEY UPDATE ");

		for (ListIterator<ColumnDefinition<?>> i = new LinkedList<ColumnDefinition<?>>(analyzed.getFields().values())
				.listIterator(); i.hasNext();) {
			ColumnDefinition<?> col = i.next();
			if (col.isPrimary())
				continue;

			if (col.isPolymorphic())
				builder.append(col.getPolymorphicTypeColName() + " = ?, ");

			builder.append(col.getName() + " = ?");

			if (i.hasNext()) {
				if (!i.next().isPrimary() || i.hasNext())
					builder.append(", ");
				i.previous();
			}
		}
		return builder.toString();
	}

	public <T> Object[] makeOnDuplicateArguments(@NonNull AnalyzedClass<T> analyzed, T obj)
			throws IllegalArgumentException, IllegalAccessException {
		Object[] args = new Object[analyzed.getFields().size() - 1];

		int i = 0;
		for (Entry<Field, ColumnDefinition<?>> entry : analyzed.getFields().entrySet()) {
			if (entry.getValue().isPrimary())
				continue;

			i = enterArgument(analyzed, obj, args, i, entry);
			i++;
		}
		return args;
	}
}
