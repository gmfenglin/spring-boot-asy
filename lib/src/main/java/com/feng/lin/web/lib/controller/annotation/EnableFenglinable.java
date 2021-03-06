package com.feng.lin.web.lib.controller.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.feng.lin.web.lib.aop.AsyControllerAspect;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Import({ AsyControllerAspect.class })
public @interface EnableFenglinable {
	boolean valid() default true;
	boolean asy() default true;
	boolean setResult() default true;
}
