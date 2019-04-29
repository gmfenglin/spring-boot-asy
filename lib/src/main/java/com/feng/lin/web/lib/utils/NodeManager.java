package com.feng.lin.web.lib.utils;

import java.util.function.Function;
import java.util.function.Predicate;

public class NodeManager<T, I, H> {
	private ExeFunNode<T, I, H> root = new ExeFunNode(null);
	private ExeFunNode<T, I, H> last = root;
	private ExeFunNode<T, I, H> current = null;

	public ExeFunNode<T, I, H> getRoot() {
		return root;
	}

	public void setNextCondition(ExeFunNode<T, I, H> nextCondition) {
		this.current = nextCondition;
		last.setNextCondition(this.current);
		last = current;
		current = null;

	}

	public void setNextThen(ExeFunNode<T, I, H> nextThen) {
		this.current = nextThen;
		last.setNextThen(this.current);
		last = current;
		current = null;

	}

	public void setNextTrue(ExeFunNode<T, I, H> nextTrue) {
		this.current = nextTrue;
		last.setNextTrue(this.current);
		last = current;
		current = null;
	}

	public void setNextFalse(ExeFunNode<T, I, H> nextFalse) throws FilterOrderException {
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

	public void setNextOr(ExeFunNode<T, I, H> nextOr) {
		this.current = nextOr;
		ExeFunNode<T, I, H> tmp = last;
		while (last.getNextThen() == null || last.getNextOr() != null) {
			last = last.getParent();
			if (last == null) {
				last = tmp;
				break;

			}
		}
		last.setNextOr(this.current);
		last = current;
		current = null;
	}

	public void setNextDo(ExeFunNode<T, I, H> nextDo) throws FilterOrderException {
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

	public ChainResult exec(ExeFunNode<Object, I, H> node, Object result, boolean flag) {
		if (node == null) {
			return new ChainResult(flag, result);
		}
		Function<Object, ExecResult<I, H>> exeFun = node.getNode();
		if (exeFun != null) {
			ExeFunNode<Object, I, H> tmp = null;
			ExecResult<I, H> execResult = exeFun.apply(result);
			result = execResult.getResult();
			flag = execResult.getExecFlag();
			if (flag) {
				tmp = node.getNextTrue();

				if (tmp == null) {
					tmp = node.getNextCondition();
				}
				if (tmp == null) {
					tmp = node.getNextThen();
					if (tmp != null) {
						ChainResult chainResult = exec(tmp, result, flag);
						if (chainResult.isFlag()) {
							return exec(node.getNextDo(), chainResult.getResult(), chainResult.isFlag());
						}
						return chainResult;
					}
				}
				if (tmp == null) {
					tmp = node.getNextDo();
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
							return exec(node.getNextDo(), chainResult.getResult(), chainResult.isFlag());
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
