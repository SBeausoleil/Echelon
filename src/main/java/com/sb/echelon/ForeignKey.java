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
public @interface ForeignKey {
	public static final String RESTRICT = "RESTRICT";
	public static final String CASCADE = "CASCADE";
	public static final String SET_NULL = "SET NULL";
	public static final String NO_ACTION = "NO ACTION";
	public static final String SET_DEFAULT = "SET DEFAULT";
	
	String onDelete() default RESTRICT;
	boolean isPolymorphic() default false;
}
