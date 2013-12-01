package com.ugent.networkplanningtool.utils;

public class Couple<T,U> {
	
	private T a;
	private U b;

	public Couple(T a, U b) {
		this.a = a;
		this.b = b;
	}

	public T getA() {
		return a;
	}

	public U getB() {
		return b;
	}
}
