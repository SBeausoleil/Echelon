package com.sb.echelon.test;

import com.sb.echelon.ForeignKey;
import com.sb.echelon.Id;
import com.sb.echelon.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table
@Data
@NoArgsConstructor
public class PolymorphicHolder {

	@Id(autoGenerate = true)
	private long id;

	@ForeignKey(isPolymorphic = true)
	private Polymorph poly;

	public PolymorphicHolder(Polymorph poly) {
		this.poly = poly;
	}
	
	
}
