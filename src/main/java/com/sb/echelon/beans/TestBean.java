package com.sb.echelon.beans;

import com.sb.echelon.ColType;
import com.sb.echelon.Column;
import com.sb.echelon.Id;
import com.sb.echelon.Table;

@Table(name = "test_beans")
public class TestBean {

	@Id
	@Column(type = ColType.BIGINT)
	private long id;
}
