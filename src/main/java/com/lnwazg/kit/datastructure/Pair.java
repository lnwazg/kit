package com.lnwazg.kit.datastructure;

/**
 * 一对对象
 * @author nan.li
 * @version 2018年5月16日
 */
public class Pair<L, R> {
	private L l;
	private R r;

	public Pair() {
	}

	public Pair(L left, R right) {
		this.l = left;
		this.r = right;
	}

	public L getLeft() {
		return l;
	}

	public void setLeft(L l) {
		this.l = l;
	}

	public R getRight() {
		return r;
	}

	public void setRight(R r) {
		this.r = r;
	}

	/**
	 * 根据等号左边的泛型，自动构造合适的Pair
	 */
	public static <L, R> Pair<L, R> of(L left, R right) {
		return new Pair<L, R>(left, right);
	}

	@Override
	public String toString() {
		return "Pair [l=" + l + ", r=" + r + "]";
	}
}