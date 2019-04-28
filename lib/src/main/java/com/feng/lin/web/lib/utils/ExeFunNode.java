package com.feng.lin.web.lib.utils;

public class ExeFunNode<T, R> {
	private ExeFunNode<T, R> parent;
	private ExeFun<T, R> node;
	private ExeFunNode<T, R> nextCondition;
	private ExeFunNode<T, R> nextThen;
	private ExeFunNode<T, R> nextTrue;
	private ExeFunNode<T, R> nextFalse;
	private ExeFunNode<T, R> nextOr;
	private ExeFunNode<T, R> nextDo;

	public ExeFunNode<T, R> getNextThen() {
		return nextThen;
	}

	public void setNextThen(ExeFunNode<T, R> nextThen) {
		this.nextThen = nextThen;
		this.nextThen.setParent(this);
	}

	public void setParent(ExeFunNode<T, R> parent) {
		this.parent = parent;
	}

	public ExeFunNode<T, R> getParent() {
		return parent;
	}

	public ExeFunNode<T, R> getNextCondition() {
		return nextCondition;
	}

	public ExeFunNode<T, R> getNextTrue() {
		return nextTrue;
	}

	public ExeFunNode<T, R> getNextFalse() {
		return nextFalse;
	}

	public ExeFunNode<T, R> getNextOr() {
		return nextOr;
	}

	public ExeFunNode<T, R> getNextDo() {
		return nextDo;
	}

	public ExeFun<T, R> getNode() {
		return node;
	}

	public void setNextCondition(ExeFunNode<T, R> nextCondition) {

		this.nextCondition = nextCondition;
		this.nextCondition.setParent(this);
	}

	public void setNextTrue(ExeFunNode<T, R> nextTrue) {
		this.nextTrue = nextTrue;
		this.nextTrue.setParent(this);
	}

	public void setNextFalse(ExeFunNode<T, R> nextFalse) {
		this.nextFalse = nextFalse;
		this.nextFalse.setParent(this);
	}

	public void setNextOr(ExeFunNode<T, R> nextOr) {
		this.nextOr = nextOr;
		this.nextOr.setParent(this);
	}

	public void setNextDo(ExeFunNode<T, R> nextDo) {
		this.nextDo = nextDo;
		this.nextDo.setParent(this);
	}

	public ExeFunNode(ExeFun<T, R> node) {
		super();
		this.node = node;
	}

}
