package com.sb.echelon.interpreters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLType;

public class ArgumentWriters {

	public static void writeByte(PreparedStatement ps, int parameterIndex, byte value) throws SQLException {
		ps.setByte(parameterIndex, value);
	}
	
	public static void writeByteWrapper(PreparedStatement ps, int parameterIndex, Byte value) throws SQLException {
		if (value != null)
			ps.setByte(parameterIndex, value);
		/*else
			ps.setNull(parameterIndex, SQLType);*/
	}
}
