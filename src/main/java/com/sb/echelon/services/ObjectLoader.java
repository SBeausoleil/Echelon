package com.sb.echelon.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.sb.echelon.beans.AnalyzedClass;
import com.sb.echelon.beans.ColumnDefinition;
import com.sb.echelon.interpreters.ObjectInterpreter;
import com.sb.echelon.util.ResultSetUtil;

@Service
public class ObjectLoader {

	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private ObjectInterpreter interpreter;

	public <T> ArrayList<T> load(AnalyzedClass<T> analyzed) {
		String sql = makeQuery(analyzed, null);
		ArrayList<LinkedHashMap<String, Object>[]> rs = jdbc.query(sql, ResultSetUtil::convertToMapsList);
		ResultSetUtil.printMapsList(rs);
		ArrayList<T> parsed = new ArrayList<>(rs.size());
		for (LinkedHashMap<String, Object>[] row : rs)
			parsed.add(interpreter.parse(row, analyzed));
		return parsed;
	}

	public <T> T load(AnalyzedClass<T> analyzed, long id) {
		String sql = makeQuery(analyzed, id);
		ArrayList<LinkedHashMap<String, Object>[]> rs = jdbc.query(sql, new Object[] { id },
				ResultSetUtil::convertToMapsList);
		ResultSetUtil.printMapsList(rs);
		return interpreter.parse(rs.get(0), analyzed);
	}

	private String makeQuery(AnalyzedClass<?> analyzed, Long id) {
		StringBuilder builder = new StringBuilder("SELECT * FROM " + analyzed.getTable());
		analyzed.getFields().entrySet().stream()
				.filter(entry -> entry.getValue().isForeign())
				.forEach(entry -> {
					ColumnDefinition<?> col = entry.getValue();
					String alias = col.getName();
					builder.append(" LEFT JOIN " + col.getForeign().getTable() + " AS " + alias);
					builder.append(" ON `" + analyzed.getTable() + "`.`" + col.getName());
					builder.append("` = `" + alias + "`.`" + col.getForeign().getIdCol().getName() + "`");
				});
		if (id != null) builder.append(" WHERE `" + analyzed.getTable() + "`.`" + analyzed.getIdCol().getName() + "` = ?");
		return builder.toString();
	}

	public <T> ArrayList<T> loadFromRaw(String sql, Object[] args, AnalyzedClass<T> analyzed) {
		ArrayList<LinkedHashMap<String, Object>[]> rs = jdbc.query(sql, args, ResultSetUtil::convertToMapsList);
		ResultSetUtil.printMapsList(rs);
		ArrayList<T> parsed = new ArrayList<>(rs.size());
		for (LinkedHashMap<String, Object>[] row : rs)
			parsed.add(interpreter.parse(row, analyzed));
		return parsed;
	}
}
