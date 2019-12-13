package com.sb.echelon.test;

import com.sb.echelon.Id;
import com.sb.echelon.Table;

import lombok.Data;

@Table
@Data
public class Possessed {

	@Id(autoGenerate = true)
	private long id;
	
	private String someData;

	public Possessed(String someData) {
		super();
		this.someData = someData;
	}
	
	
}
