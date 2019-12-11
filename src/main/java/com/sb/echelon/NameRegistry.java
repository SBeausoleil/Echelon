package com.sb.echelon;

import java.util.HashMap;

import org.springframework.stereotype.Component;

@Deprecated
@Component
public class NameRegistry {

	private HashMap<String, Class<?>> registeredPrefixes = new HashMap<>();
	private HashMap<Class<?>, String> registeredClasses = new HashMap<>();

	public boolean registerPrefix(String prefix, Class<?> clazz) {
		if (registeredPrefixes.putIfAbsent(prefix, clazz) != null) {
			registeredClasses.put(clazz, prefix);
			return true;
		}
		return false;
	}

	public boolean registerPrefixFor(Class<?> clazz) {
		String prefix;
		String className = clazz.getSimpleName();
		if (clazz.isMemberClass()) {
			// Nested classes often have the same name (e.g.: Builder), if this is a nested
			// class, prefix the three first of the parent also
			prefix = "cla";
		} else {

			// Try with the classic 3 first letters method
			prefix = className.substring(0, 3 < className.length() ? 3 : className.length());
			if (registerPrefix(prefix, clazz))
				return true;
		}
		// If that doesn't work, add the first 2 letters of the package to the prefix
		String packageName = clazz.getPackageName();
		int lastFullStop = packageName.lastIndexOf('.');
		if (lastFullStop != -1)
			packageName = packageName.substring(lastFullStop,
					lastFullStop + 2 < packageName.length() ? 2 : packageName.length() - lastFullStop);

		prefix = packageName + prefix;
		if (registerPrefix(prefix, clazz))
			return true;

		// If that still doesn't work, start adding letters to the prefix
		return true;
	}
}
