package com.sb.echelon.test;

import com.sb.echelon.Id;
import com.sb.echelon.Table;

import lombok.Data;

@Table
@Data
public class Owner {

	@Id(autoGenerate = true)
	private long id;
	
	private float cash;
	
	private Possessed possession;

	public Owner(float cash, Possessed possession) {
		this.cash = cash;
		this.possession = possession;
	}
}
