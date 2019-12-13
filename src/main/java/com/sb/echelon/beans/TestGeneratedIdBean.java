package com.sb.echelon.beans;

import com.sb.echelon.ColType;
import com.sb.echelon.Column;
import com.sb.echelon.Id;
import com.sb.echelon.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "test_beans")
@NoArgsConstructor
@Data
public class TestGeneratedIdBean {

	@Id
	@Column(type = ColType.BIGINT)
	private long id;
	
	private String text;
	
	private int randomValue;

	public TestGeneratedIdBean(String text, int randomValue) {
		this.text = text;
		this.randomValue = randomValue;
	}
	
	
}
