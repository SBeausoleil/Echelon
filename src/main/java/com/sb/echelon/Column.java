package com.sb.echelon;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
	public static final String USE_TYPE = "USE_TYPE";
	public static final String GENERATE_COL_NAME = "GENERATE_COL_NAME";
	
	/**
	 * The type of column to use for this field.
	 * DEFAULT: ECHELON_GENERATED: The Echelon system will attempt to find the most appropriate column type to use.
	 */
	ColType type() default ColType.ECHELON_GENERATED;
	/**
	 * The name of the field.
	 * DEFAULT: snake_case version of the field name.
	 */
	String name() default GENERATE_COL_NAME;
	/**
	 * The type of column to use for this field.
	 * If this field is set, it overrides type(), even if type() is set.
	 * DEFAULT: use type()
	 */
	String customType() default USE_TYPE;
}
