package com.feng.lin.web.lib.utils;

public class NodeManager {
	private ExeFunNode root = new ExeFunNode(null);
	private ExeFunNode last = root;
	private ExeFunNode current = null;

	public ExeFunNode getRoot() {
		return root;
	}

	public void setNextCondition(ExeFunNode nextCondition) {
		this.current = nextCondition;
		last.setNextCondition(this.current);
		last = current;
		current = null;

	}

	public void setNextTrue(ExeFunNode nextTrue) {
		this.current = nextTrue;
		last.setNextTrue(this.current);
		last = current;
		current = null;
	}

	public void setNextFalse(ExeFunNode nextFalse) throws FilterOrderException {
		this.current = nextFalse;
		while (last.getNextTrue() == null || last.getNextFalse() != null) {
			last = last.getParent();
			if (last == null) {
				throw new FilterOrderException("setNextFalse");

			}
		}
		last.setNextFalse(this.current);
		last = current;
		current = null;
	}

	public void setNextOr(ExeFunNode nextOr) {
		this.current = nextOr;
		last.setNextOr(this.current);
		last = current;
		current = null;
	}

	public void setNextDo(ExeFunNode nextDo) throws FilterOrderException {
		this.current = nextDo;
		while (last.getNextOr() == null || last.getNextDo() != null) {
			last = last.getParent();
			if (last == null) {
				throw new FilterOrderException("setNextDo");

			}
		}
		last.setNextDo(this.current);
		last = current;
		current = null;
	}

	public ChainResult exec(ExeFunNode node, Object result, boolean flag) {
		if (node == null) {
			return new ChainResult(flag, result);
		}
		ExeFun exeFun = node.getNode();
		if (exeFun != null) {
			ExeFunNode tmp = null;
			result = exeFun.getFun().apply(result);
			flag = exeFun.getPredicate().test(result);
			if (flag) {
				tmp = node.getNextTrue();
				if (tmp == null) {
					tmp = node.getNextDo();
				}
				if (tmp == null) {
					tmp = node.getNextCondition();
				}
				if (tmp != null) {
					return exec(tmp, result, flag);
				}
			} else {
				tmp = node.getNextFalse();
				if (tmp != null) {
					return exec(tmp, result, flag);
				}
				if (tmp == null) {
					tmp = node.getNextOr();
					if (tmp != null) {
						ChainResult chainResult = exec(tmp, result, flag);
						if (chainResult.isFlag()) {
							return exec(node.getNextDo(), chainResult, flag);
						}
						return chainResult;
					}
				}

			}
		} else {
			return exec(node.getNextCondition(), result, flag);
		}
		return new ChainResult(flag, result);
	}
}
