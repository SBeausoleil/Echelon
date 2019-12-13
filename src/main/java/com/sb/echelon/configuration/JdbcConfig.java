package com.sb.echelon.configuration;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
@PropertySource("classpath:echelon.properties")
public class JdbcConfig {

	@Value("${datasource.url}")
	private String url;
	@Value("${datasource.username}")
	private String user;
	@Value("${datasource.password}")
	private String password;
	
	@Bean
	public SimpleDriverDataSource simpleDriverDataSource() throws SQLException {
		return new SimpleDriverDataSource(new com.mysql.cj.jdbc.Driver(), url, user, password);
	}

	@Bean
	public JdbcTemplate jdbcTemplate(SimpleDriverDataSource ds) {
		return new JdbcTemplate(ds);
	}

}
