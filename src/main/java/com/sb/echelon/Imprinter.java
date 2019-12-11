package com.sb.echelon;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public interface Imprinter<T> extends RowMapper<T> {

	T imprintOn(T obj, ResultSet rs, int rowNum) throws SQLException;
	
	String getInsertStatement();
}
