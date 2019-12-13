package com.sb.echelon.interpreters;

import java.util.function.Function;

@FunctionalInterface
public interface SqlInsertionPreparer<T> extends Function<T, Object> {


	default Object prepare(Object obj) {
		return apply((T) obj);
	}
}
