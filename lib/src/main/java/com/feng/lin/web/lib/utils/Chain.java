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
	public <T, R> Chain doFilter(Function<T, R> fun, Predicate<R> predicate) {
		manger.setNextCondition(new ExeFunNode(new ExeFun(fun, predicate)));
		return this;
	}

	public <T, R> Chain doOrFilter(Function<T, R> fun, Predicate<R> predicate) throws FilterOrderException {
		manger.setNextOr(new ExeFunNode(new ExeFun(fun, predicate)));
		return this;
	}

	public <T, R> Chain doUnionFilter(Function<T, R> fun, Predicate<R> predicate) throws FilterOrderException {
		manger.setNextDo(new ExeFunNode(new ExeFun(fun, predicate)));
		return this;
	}

	public <T, R> Chain doTrueFilter(Function<T, R> fun, Predicate<R> predicate) throws FilterOrderException {
		manger.setNextTrue(new ExeFunNode(new ExeFun(fun, predicate)));
		return this;
	}

	public <T, R> Chain doFalseFilter(Function<T, R> fun, Predicate<R> predicate) throws FilterOrderException {
		manger.setNextFalse(new ExeFunNode(new ExeFun(fun, predicate)));
		return this;
	}

	public void finish(ChainResult chainResult) {

		ChainResult tmp = manger.exec(manger.getRoot(), null, false);
		chainResult.setFlag(tmp.isFlag());
		chainResult.setResult(tmp.getResult());
	}
}
