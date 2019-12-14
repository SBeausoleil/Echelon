package com.sb.echelon.services;

import java.lang.reflect.Field;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.sb.echelon.ColType;
import com.sb.echelon.ForeignKey;
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
		fields(analyzed, builder);
		foreignKeys(analyzed, builder);
		builder.append(" )");
		return builder.toString();
	}

	public void fields(AnalyzedClass<?> analyzed, StringBuilder builder) {
		for (Iterator<ColumnDefinition<?>> i = analyzed.getFields().values().iterator(); i.hasNext();) {
			ColumnDefinition<?> col = i.next();
			builder.append(col.getName() + " ");
			builder.append(col.getSqlType());
			
			if (col.isPrimary()) {
				if (col.getPrimary() == Primary.AUTO_GENERATE)
					builder.append(" AUTO_INCREMENT");
				builder.append(" PRIMARY KEY");
			}
			
			if (col.isPolymorphic())
				builder.append(", " + col.getPolymorphicTypeColName() + " " + ColType.VARCHAR.sql);
			if (i.hasNext())
				builder.append(", ");
		}
	}

	public void foreignKeys(AnalyzedClass<?> analyzed, StringBuilder builder) {
		List<Entry<Field, ColumnDefinition<?>>> foreigns = analyzed.getFields().entrySet().stream()
				.filter(entry -> entry.getValue().isForeign())
				.collect(Collectors.toList());

		for (Iterator<Entry<Field, ColumnDefinition<?>>> i = foreigns.iterator(); i.hasNext();) {
			Entry<Field, ColumnDefinition<?>> entry = i.next();
			ColumnDefinition<?> col = entry.getValue();

			builder.append(", CONSTRAINT " + col.getName() + "_fk_ndx");
			builder.append(" FOREIGN KEY (" + col.getName() + ")");
			builder.append(" REFERENCES " + col.getForeign().getTable()
					+ "(" + col.getForeign().getIdCol().getName() + ")");

			ForeignKey fk = entry.getKey().getAnnotation(ForeignKey.class);
			if (fk != null)
				builder.append(" ON DELETE " + fk.onDelete());
		}
	}

	public boolean tableExists(AnalyzedClass<?> analyzed) throws SQLException {
		DatabaseMetaData meta = jdbc.getDataSource().getConnection().getMetaData();
		ResultSet rs = meta.getTables(null, null, analyzed.getTable(), null);
		return rs.next();
	}
}
