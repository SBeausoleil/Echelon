package com.sb.echelon.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.sb.echelon.beans.AnalyzedClass;
import com.sb.echelon.interpreters.ObjectInterpreter;
import com.sb.echelon.util.ResultSetUtil;

@Service
public class ObjectLoader {

	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private ObjectInterpreter interpreter;

	public <T> T load(AnalyzedClass<T> analyzed, long id) {
		ArrayList<LinkedHashMap<String, Object>[]> rs = jdbc.query(
				"SELECT * FROM " + analyzed.getTable() + " WHERE " + analyzed.getIdCol().getName() + " = ?",
				new Object[] { id }, ResultSetUtil::convertToMapsList);
		ResultSetUtil.printMapsList(rs);
		//return interpreter.parse(rs.get(0), analyzed);
		return null;
	}
}
