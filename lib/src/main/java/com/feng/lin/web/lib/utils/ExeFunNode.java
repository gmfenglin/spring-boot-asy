package com.feng.lin.web.lib.utils;

public class ExeFunNode {
	private ExeFunNode parent;
	private ExeFun node;
	private ExeFunNode nextCondition;
	private ExeFunNode nextTrue;
	private ExeFunNode nextFalse;
	private ExeFunNode nextOr;
	private ExeFunNode nextDo;


	public void setParent(ExeFunNode parent) {
		this.parent = parent;
	}

	public ExeFunNode getParent() {
		return parent;
	}

	public ExeFunNode getNextCondition() {
		return nextCondition;
	}

	public ExeFunNode getNextTrue() {
		return nextTrue;
	}

	public ExeFunNode getNextFalse() {
		return nextFalse;
	}

	public ExeFunNode getNextOr() {
		return nextOr;
	}

	public ExeFunNode getNextDo() {
		return nextDo;
	}

	public ExeFun getNode() {
		return node;
	}

	public void setNextCondition(ExeFunNode nextCondition) {
		
		this.nextCondition = nextCondition;
		this.nextCondition.setParent(this);
	}

	public void setNextTrue(ExeFunNode nextTrue) {
		this.nextTrue = nextTrue;
		this.nextTrue.setParent(this);
	}

	public void setNextFalse(ExeFunNode nextFalse) {
		this.nextFalse = nextFalse;
		this.nextFalse.setParent(this);
	}

	public void setNextOr(ExeFunNode nextOr) {
		this.nextOr = nextOr;
		this.nextOr.setParent(this);
	}

	public void setNextDo(ExeFunNode nextDo) {
		this.nextDo = nextDo;
		this.nextDo.setParent(this);
	}

	public ExeFunNode( ExeFun node) {
		super();
		this.node = node;
	}

}
