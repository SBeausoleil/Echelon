package com.sb.echelon.test;

import com.sb.echelon.Id;
import com.sb.echelon.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table()
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArraysBean {

	@Id(autoGenerate = true)
	private long id;
	
	private short[] shortArray;
	private Short[] shortWrapperArray;
	
	private boolean[] bools;
	
	private char[] chars;
}
