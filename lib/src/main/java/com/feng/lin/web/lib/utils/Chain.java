package com.feng.lin.web.lib.utils;

import java.util.function.Function;
import java.util.function.Predicate;

public class Chain {
	private NodeManager manger = new NodeManager();

	/*
	 * 
	 * .doFilter((result) -> { return null; }, (result) -> { return false; })
	 * 
	 **/
	public <T, R> Chain flow(Function<T, R> fun, Predicate<R> predicate) {
		manger.setNextCondition(new ExeFunNode(new ExeFun(fun, predicate)));
		return this;
	}
	public <T, R> Chain then(Function<T, R> fun, Predicate<R> predicate) {
		manger.setNextThen(new ExeFunNode(new ExeFun(fun, predicate)));
		return this;
	}
	public <T, R> Chain or(Function<T, R> fun, Predicate<R> predicate) throws FilterOrderException {
		manger.setNextOr(new ExeFunNode(new ExeFun(fun, predicate)));
		return this;
	}

	public <T, R> Chain union(Function<T, R> fun, Predicate<R> predicate) throws FilterOrderException {
		manger.setNextDo(new ExeFunNode(new ExeFun(fun, predicate)));
		return this;
	}

	public <T, R> Chain yes(Function<T, R> fun, Predicate<R> predicate) throws FilterOrderException {
		manger.setNextTrue(new ExeFunNode(new ExeFun(fun, predicate)));
		return this;
	}

	public <T, R> Chain no(Function<T, R> fun, Predicate<R> predicate) throws FilterOrderException {
		manger.setNextFalse(new ExeFunNode(new ExeFun(fun, predicate)));
		return this;
	}

	public void ok(ChainResult result) {
		ChainResult tmp = manger.exec(manger.getRoot(), null, false);
		result.setFlag(tmp.isFlag());
		result.setResult(tmp.getResult());

	}
}
