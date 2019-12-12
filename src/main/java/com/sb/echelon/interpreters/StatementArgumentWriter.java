package com.sb.echelon.interpreters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementArgumentWriter<T> {

	public void write(PreparedStatement ps, int parameterIndex, T value) throws SQLException;
}