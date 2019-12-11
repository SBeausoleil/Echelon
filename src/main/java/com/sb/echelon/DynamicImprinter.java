package com.sb.echelon;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

import com.sb.echelon.exceptions.NoEmptyConstructorException;

import lombok.Getter;

@Getter
public class DynamicImprinter<T> implements Imprinter<T> {

	private Class<T> managed;
	private Supplier<T> supplier;

	public DynamicImprinter(Class<T> managed, Supplier<T> supplier) {
		this.managed = managed;
		this.supplier = supplier;
	}

	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T obj;
		if (supplier != null) {
			obj = supplier.get();
		} else {
			Constructor<T> emptyConstructor;
			try {
				emptyConstructor = managed.getDeclaredConstructor();
			} catch (NoSuchMethodException e) {
				throw new NoEmptyConstructorException(e);
			}
			try {
				obj = emptyConstructor.newInstance();
			} catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
				throw new RuntimeException(e);
			}
		}
		return imprintOn(obj, rs, rowNum);
	}

	public T imprintOn(T obj, ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getInsertStatement() {
		// TODO Auto-generated method stub
		return null;
	}

}
