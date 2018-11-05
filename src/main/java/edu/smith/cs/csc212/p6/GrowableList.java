package edu.smith.cs.csc212.p6;

import edu.smith.cs.csc212.p6.errors.EmptyListError;
import edu.smith.cs.csc212.p6.errors.P6NotImplemented;

public class GrowableList<T> implements P6List<T> {
	public static final int START_SIZE = 32;
	private Object[] array;
	private int fill;
	
	public GrowableList() {
		this.array = new Object[START_SIZE];
		this.fill = 0;
	}

	@Override
	public T removeFront() { // Efficiency: O(n) because it uses removeIndex which is O(n)
		return removeIndex(0);
	}

	@Override
	public T removeBack() { // Efficiency: O(1)s
		if (this.size() == 0) {
			throw new EmptyListError();
		} 	
			T value = this.getIndex( fill - 1);
			fill--;
			this.array[fill] = null;
			return value;
	}

	@Override
	public T removeIndex(int index) { // Efficiency: O(n) because of for loop 
		if (this.size() == 0) {
			throw new EmptyListError();
		}
		T removed = this.getIndex(index);
		fill--;
		for (int i=index; i<fill; i++) {
			this.array[i] = this.array[i+1];
		}
		this.array[fill] = null;
		return removed;
	}

	@Override
	public void addFront(T item) { // Efficiency: O(n) it uses addIndex which is O(n)
		addIndex( item, 0 );
	}

	@Override
	public void addBack(T item) { // Efficiency: O(1)
		// I've implemented part of this for you.
		if (fill >= this.array.length) { 
			throw new P6NotImplemented();
		}
		this.array[fill++] = item;
	}

	@Override
	public void addIndex(T item, int index) { // Efficiency: O(n) because of for loop 
		for (int j=fill; j>index; j--) {
			array[j] = array[j-1];
		}
			array[index] = item;
			fill++;		
	}
	
	@Override
	public T getFront() { // Efficiency: O(1)
		return this.getIndex(0);
	}

	@Override
	public T getBack() { // Efficiency: O(1)
		return this.getIndex(this.fill-1);
	}

	/**
	 * Do not allow unchecked warnings in any other method.
	 * Keep the "guessing" the objects are actually a T here.
	 * Do that by calling this method instead of using the array directly.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T getIndex(int index) { // Efficiency: O(1)
		return (T) this.array[index];
	}

	@Override
	public int size() { // Efficiency: O(1)
		return fill;
	}

	@Override
	public boolean isEmpty() { // Efficiency: O(1)
		return fill == 0;
	}


}
