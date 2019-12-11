package com.sb.echelon;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the table that will contain the data of this class.
 * 
 * @author Samuel Beausoleil
 *
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
	public static final String GENERATE_TABLE_NAME = "GENERATE_TABLE_NAME";
	
	String name() default GENERATE_TABLE_NAME;
}
