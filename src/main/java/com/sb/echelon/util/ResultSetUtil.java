package com.sb.echelon.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResultSetUtil {

	public static ArrayList<LinkedHashMap<String, Object>> convertToMapsList(ResultSet rs) throws SQLException {
		ArrayList<LinkedHashMap<String, Object>> list = new ArrayList<>();
		ResultSetMetaData meta = rs.getMetaData();
		final int N_COLUMNS = meta.getColumnCount();
		while (rs.next()) {
			LinkedHashMap<String, Object> map = new LinkedHashMap<>(N_COLUMNS);
			for (int i = 1; i <= N_COLUMNS; i++) { map.put(meta.getColumnName(i), rs.getObject(i)); }
			list.add(map);
		}
		return list;
	}

	public static void printMapsList(ArrayList<LinkedHashMap<String, Object>> rs) {
		int row = 0;
		for (Map<String, Object> map : rs) {
			System.out.print(row++);
			for (Entry<String, Object> entry : map.entrySet()) {
				System.out.print(": " + entry.getKey() + " = " + entry.getValue());
			}
			System.out.println();
		}
	}
}
