package com.ugent.networkplanningtool.utils;

/**
 * Class used in methods returning two types of results
 * @param <T> the first object type
 * @param <U> the second object type
 */
public class Couple<T,U> {
	
	private T a;
	private U b;

    /**
     * Deafult constructor
     * @param a the instance of the first object type
     * @param b the instance of the second object type
     */
	public Couple(T a, U b) {
		this.a = a;
		this.b = b;
	}

    /**
     * Gets the first object type instance
     * @return the first object type instance
     */
	public T getA() {
		return a;
	}

    /**
     * Gets the second object type instance
     * @return the second object type instance
     */
	public U getB() {
		return b;
	}
}
