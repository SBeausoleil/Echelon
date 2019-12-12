package com.sb.echelon.services;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.sb.echelon.beans.AnalyzedClass;
import com.sb.echelon.beans.ColumnDefinition;
import com.sb.echelon.beans.ColumnDefinition.Primary;

@Service
public class TableWriter {

	@Autowired
	private JdbcTemplate jdbc;
	
	public void writeTable(AnalyzedClass<?> analyzed) {
		String sql = toSql(analyzed);
		jdbc.execute(sql);
	}

	public String toSql(AnalyzedClass<?> analyzed) {
		StringBuilder builder = new StringBuilder("CREATE TABLE " + analyzed.getTable() + " ( ");
		for (Iterator<ColumnDefinition<?>> i = analyzed.getFields().values().iterator(); i.hasNext();) {
			ColumnDefinition<?> col = i.next();
			builder.append(col.getName() + " ");
			builder.append(col.getSqlType());
			if (col.isPrimary()) {
				if (col.getPrimary() == Primary.AUTO_GENERATE)
					builder.append(" AUTO_INCREMENT");
				builder.append(" PRIMARY KEY");
			}
			if (i.hasNext())
				builder.append(", ");
		}
		builder.append(" )");
		return builder.toString();
	}
}
