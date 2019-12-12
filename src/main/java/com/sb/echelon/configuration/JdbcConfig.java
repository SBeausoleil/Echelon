package com.sb.echelon.configuration;

import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class JdbcConfig {

	@Bean
	public SimpleDriverDataSource simpleDriverDataSource() throws SQLException {
		return new SimpleDriverDataSource(new com.mysql.cj.jdbc.Driver(),
				"jdbc:mysql://localhost:3306/echelon?serverTimezone=UTC", "echelon", "echelon");
	}

	@Bean
	public JdbcTemplate jdbcTemplate(SimpleDriverDataSource ds) {
		return new JdbcTemplate(ds);
	}

}
