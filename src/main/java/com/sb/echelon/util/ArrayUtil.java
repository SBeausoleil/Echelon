package com.sb.echelon.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ArrayUtil {

	public static <T> T[] concatenate(T[] a, T[] b, T[] c) {
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}
}
