package com.sb.echelon.interpreters;

import java.util.function.Function;

/**
 * Gives a chance to convert a field to something that is understood by PreparedStatement.setObject(int, Object);
 * @author Samuel Beausoleil
 *
 * @param <T>
 */
@FunctionalInterface
public interface SqlInsertionPreparer<T> extends Function<T, Object> {

	default Object prepare(Object obj) {
		return apply((T) obj);
	}
}
