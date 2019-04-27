package com.feng.lin.web.lib.utils;

import java.util.function.Function;
import java.util.function.Predicate;

public class ExeFun<T, R> {
	public ExeFun(Function<T, R> fun, Predicate<R> predicate) {
		this.fun = fun;
		this.predicate = predicate;

	}

	private Function<T, R> fun;
	private Predicate<R> predicate;

	public Predicate<R> getPredicate() {
		return predicate;
	}

	public Function<T, R> getFun() {
		return fun;
	}

}