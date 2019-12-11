package com.sb.echelon;

public class App {

	private static class SubClass {
		
	}

	public static void main(String[] args) {
		App.SubClass a = new SubClass();
		System.out.println(a.getClass().getSimpleName());
		System.out.println(a.getClass().getName());
		System.out.println(a.getClass().getCanonicalName());
		System.out.println(a.getClass().isMemberClass());
		System.out.println(App.class.isMemberClass());
	}
}
