package com.sb.echelon.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Function;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FieldUtil {

	public static Field[] getFields(@NonNull Object target, Function<Field, Boolean> shouldIgnore) {
		return getFields(target.getClass(), shouldIgnore);
	}

	public static Field[] getFields(@NonNull Class<?> target, Function<Field, Boolean> shouldIgnore) {
		var hierarchy = new LinkedList<Class<?>>();
		while (target != null) {
			hierarchy.addFirst(target);
			target = target.getSuperclass();
		}

		var fields = new ArrayList<Field>();
		for (Class<?> current : hierarchy) {
			for (Field field : current.getDeclaredFields())
				if (!Modifier.isStatic(field.getModifiers()) && !shouldIgnore.apply(field))
					fields.add(field);
		}
		return fields.toArray(new Field[fields.size()]);
	}

	public static boolean isTransient(Field field) {
		return Modifier.isTransient(field.getModifiers());
	}
}
