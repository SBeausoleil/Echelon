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

	public <T> T load(AnalyzedClass<T> analyzed, long id) {
		if (!analyzed.hasRelations()) {
			ArrayList<LinkedHashMap<String, Object>[]> rs = jdbc.query(
					"SELECT * FROM " + analyzed.getTable() + " WHERE `" + analyzed.getIdCol().getName() + "` = ?",
					new Object[] { id }, ResultSetUtil::convertToMapsList);
			ResultSetUtil.printMapsList(rs);
			if (rs.size() > 0)
				return interpreter.parse(rs.get(0)[0], analyzed);
			else
				return null;
		} else {
			String sql = makeComplexQuery(analyzed, id);
			System.out.println(sql);
			ArrayList<LinkedHashMap<String, Object>[]> rs = jdbc.query(sql, new Object[] { id },
					ResultSetUtil::convertToMapsList);
			ResultSetUtil.printMapsList(rs);
			return interpreter.parse(rs.get(0), analyzed);
		}
	}

	private String makeComplexQuery(AnalyzedClass<?> analyzed, long id) {
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
		builder.append(" WHERE `" + analyzed.getTable() + "`.`" + analyzed.getIdCol().getName() + "` = ?");
		return builder.toString();
	}
}
