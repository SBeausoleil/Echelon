package com.sb.echelon.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.sb.echelon.beans.AnalyzedClass;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResultSetUtil {

	public static ArrayList<LinkedHashMap<String, Object>[]> convertToMapsList(ResultSet rs) throws SQLException {
		ArrayList<LinkedHashMap<String, Object>[]> list = new ArrayList<>();
		ResultSetMetaData meta = rs.getMetaData();
		final int N_COLUMNS = meta.getColumnCount();
		while (rs.next()) {
			ArrayList<LinkedHashMap<String, Object>> completeRow = new ArrayList<>();
			LinkedHashMap<String, Object> map = null;
			for (int i = 1; i <= N_COLUMNS; i++) {
				if (meta.getColumnName(i).equals(AnalyzedClass.CLASS_COLUMN_NAME)) {
					// Means that we are not looking at data from an other class on the same row
					map = new LinkedHashMap<>();
					completeRow.add(map);
				}

				map.put(meta.getColumnName(i), rs.getObject(i));
			}
			list.add(completeRow.toArray(new LinkedHashMap[list.size()]));
		}
		return list;
	}

	public static void printMapsList(ArrayList<LinkedHashMap<String, Object>[]> rs) {
		int row = 0;
		for (Map<String, Object>[] classes : rs) {
			System.out.print(row++);
			for (Map<String, Object> clazz : classes) {
				for (Entry<String, Object> entry : clazz.entrySet()) {
					System.out.print(": " + entry.getKey() + " = " + entry.getValue());
				}
				System.out.println();
			}
			System.out.println();
		}
	}
}
