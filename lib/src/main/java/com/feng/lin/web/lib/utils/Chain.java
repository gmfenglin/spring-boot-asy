package com.feng.lin.web.lib.utils;

import java.util.function.Function;

public class Chain {
	private NodeManager manger = new NodeManager();

	/*
	 * 
	 * .doFilter((result) -> { return null; }, (result) -> { return false; })
	 * 
	 **/
	public <I, H, T> Chain flow(Function<T, ExecResult<I, H>> fun) {
		manger.setNextCondition(new ExeFunNode(fun));
		return this;
	}

	public <I, H, T> Chain then(Function<T, ExecResult<I, H>> fun) {
		manger.setNextThen(new ExeFunNode(fun));
		return this;
	}

	public <I, H, T> Chain or(Function<T, ExecResult<I, H>> fun) throws FilterOrderException {
		manger.setNextOr(new ExeFunNode(fun));
		return this;
	}

	public <I, H, T> Chain union(Function<T, ExecResult<I, H>> fun) throws FilterOrderException {
		manger.setNextDo(new ExeFunNode(fun));
		return this;
	}

	public <I, H, T> Chain yes(Function<T, ExecResult<I, H>> fun) throws FilterOrderException {
		manger.setNextTrue(new ExeFunNode(fun));
		return this;
	}

	public <I, H, T> Chain no(Function<T, ExecResult<I, H>> fun) throws FilterOrderException {
		manger.setNextFalse(new ExeFunNode(fun));
		return this;
	}

	public void ok(ChainResult result) {
		ChainResult tmp = manger.exec(manger.getRoot(), null, false);
		result.setFlag(tmp.isFlag());
		result.setResult(tmp.getResult());

	}
}
