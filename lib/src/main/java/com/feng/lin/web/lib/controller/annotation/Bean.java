package com.feng.lin.web.lib.controller.annotation;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Bean {
	public Class<?> clsName();

	public String[] ignoreRequire() default {};
}