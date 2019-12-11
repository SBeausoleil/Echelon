package com.sb.echelon.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FieldUtil {

	public static Field[] getFields(@NonNull Object target, Function<Field, Boolean> shouldIgnore) {
		return getFields(target.getClass(), shouldIgnore);
	}

	public static Field[] getFields(@NonNull Class<?> target, Function<Field, Boolean> shouldIgnore) {
		ArrayList<Field> fields = new ArrayList<>();
		while (target != null) {
			for (Field field : target.getDeclaredFields())
				if (!Modifier.isStatic(field.getModifiers()) && !shouldIgnore.apply(field))
					fields.add(field);
			target = target.getSuperclass();
		}
		Collections.reverse(fields); // Get the super class fields before the subclass fields
		return fields.toArray(new Field[fields.size()]);
	}

	public static boolean isTransient(Field field) {
		return Modifier.isTransient(field.getModifiers());
	}
}
