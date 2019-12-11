package com.sb.echelon.util;

import lombok.experimental.UtilityClass;
import org.springframework.jdbc.core.JdbcTemplate;

@UtilityClass
public class JdbcUtil {
	public static boolean exists(JdbcTemplate jdbc, String table, String idColumn, Object id) {
		return jdbc.queryForObject("SELECT COUNT(*) FROM " + table + " WHERE ? = ?",
				new Object[] { idColumn, id }, Integer.class) > 0;
	}
}
