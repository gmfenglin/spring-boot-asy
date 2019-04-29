package com.feng.lin.web.lib.utils;

import java.util.function.Function;

public class ExeFunNode<T, I, H> {
	private ExeFunNode<T, I, H> parent;
	private Function<T, ExecResult<I, H>> node;
	private ExeFunNode<T, I, H> nextCondition;
	private ExeFunNode<T, I, H> nextThen;
	private ExeFunNode<T, I, H> nextTrue;
	private ExeFunNode<T, I, H> nextFalse;
	private ExeFunNode<T, I, H> nextOr;
	private ExeFunNode<T, I, H> nextDo;

	public ExeFunNode<T, I, H> getNextThen() {
		return nextThen;
	}

	public void setNextThen(ExeFunNode<T, I, H> nextThen) {
		this.nextThen = nextThen;
		this.nextThen.setParent(this);
	}

	public void setParent(ExeFunNode<T, I, H> parent) {
		this.parent = parent;
	}

	public ExeFunNode<T, I, H> getParent() {
		return parent;
	}

	public ExeFunNode<T, I, H> getNextCondition() {
		return nextCondition;
	}

	public ExeFunNode<T, I, H> getNextTrue() {
		return nextTrue;
	}

	public ExeFunNode<T, I, H> getNextFalse() {
		return nextFalse;
	}

	public ExeFunNode<T, I, H> getNextOr() {
		return nextOr;
	}

	public ExeFunNode<T, I, H> getNextDo() {
		return nextDo;
	}

	public Function<T, ExecResult<I, H>> getNode() {
		return node;
	}

	public void setNextCondition(ExeFunNode<T, I, H> nextCondition) {

		this.nextCondition = nextCondition;
		this.nextCondition.setParent(this);
	}

	public void setNextTrue(ExeFunNode<T, I, H> nextTrue) {
		this.nextTrue = nextTrue;
		this.nextTrue.setParent(this);
	}

	public void setNextFalse(ExeFunNode<T, I, H> nextFalse) {
		this.nextFalse = nextFalse;
		this.nextFalse.setParent(this);
	}

	public void setNextOr(ExeFunNode<T, I, H> nextOr) {
		this.nextOr = nextOr;
		this.nextOr.setParent(this);
	}

	public void setNextDo(ExeFunNode<T, I, H> nextDo) {
		this.nextDo = nextDo;
		this.nextDo.setParent(this);
	}

	public ExeFunNode(Function<T, ExecResult<I, H>> node) {
		super();
		this.node = node;
	}

}
