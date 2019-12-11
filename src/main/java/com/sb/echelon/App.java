package com.sb.echelon;

import java.util.HashMap;
import java.util.Map;

public class App {

	public static void main(String[] args) {
		Map<Integer, Object> map = new HashMap<>();
		map.put(0, true);
		map.put(1, null);
		System.out.println((boolean) map.get(0));
		System.out.println((String) map.getOrDefault(1, false));
	}
}
