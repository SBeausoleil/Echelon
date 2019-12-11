package com.sb.echelon.services;

import org.springframework.stereotype.Component;

@Component
public class SnakeCaseFormatter implements StringFormatter {

	@Override
	public String format(String str) {
		StringBuilder res = new StringBuilder(str.length() + 1); // field names typically are constituted of one or two
																 // words, so add room for the _
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (Character.isUpperCase(chars[i]))
				res.append('_');
			res.append(Character.toLowerCase(chars[i]));
		}
		return res.toString();
	}

}
