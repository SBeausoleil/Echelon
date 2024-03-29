package com.sb.echelon;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ColType {
	// @// @formatter:off
	BIT, TINYINT, SMALLINT, INTEGER, BIGINT, FLOAT, DOUBLE("DOUBLE PRECISION"),
	CHAR("CHAR(1)"), VARCHAR("VARCHAR(255)"), TEXT, LONGTEXT,
	BOOLEAN,
	DATE, TIME, TIMESTAMP,
	TINYBLOB, BLOB, MEDIUMBLOB, LONGBLOB,
	JSON,
	/**
	 * Means that Echelon must find out which type is best for this field.
	 */
	ECHELON_GENERATED;
	// @formatter:on

	public final String sql;

	ColType() {
		sql = name();
	}

	@Override
	public String toString() {
		return sql;
	}
}