package com.sb.echelon.test;

import com.sb.echelon.Id;
import com.sb.echelon.Table;

@Table
public class PolymorphA implements Polymorph {

	@Id(autoGenerate = true)
	private long id;

	@Override
	public void foo() {
		System.out.println("And everybody was Kung-Foo fighting!");
	}

}
