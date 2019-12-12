package com.sb.echelon.beans;

import com.sb.echelon.ColType;
import com.sb.echelon.Column;
import com.sb.echelon.Id;
import com.sb.echelon.Table;

import lombok.NoArgsConstructor;

@Table(name = "test_beans")
@NoArgsConstructor
public class TestBean {

	@Id
	@Column(type = ColType.BIGINT)
	private long id;
	
	private String text;

	public TestBean(String text) {
		this.text = text;
	}
	
	
}
