package edu.smith.cs.csc212.p6;

import java.util.Iterator;

import edu.smith.cs.csc212.p6.errors.BadIndexError;
import edu.smith.cs.csc212.p6.errors.EmptyListError;

public class SinglyLinkedList<T> implements P6List<T>, Iterable<T> {
	/**
	 * The start of this list. Node is defined at the bottom of this file.
	 */
	Node<T> start;

	@Override
	public T removeFront() { // Efficiency: O(1)
		// checks if list is empty
		checkNotEmpty();
		// find the front
		T found = start.value;
		// set start to next value
		start = start.next;
		// return our previous start
		return found;
	}

	@Override
	public T removeBack() { // Efficiency: O(n) because of for loop 
		// if empty
		checkNotEmpty();
		// if there is only one value
		if (start.next == null) {
			return this.removeFront();
			// if all is fine
		} else {
			//   go through every node
			for (Node<T> current = start; current != null; current = current.next) {
				// find the last node
				if (current.next.next == null) {
					// save its value
					T remove = current.next.value;
					// set the node before the last one to null
					current.next = null;
					// return our "deleted" node
					return remove;
				}
			}
		}
		return null;
	}

	@Override
	public T removeIndex(int index) { // Efficiency: O(n) because of for loop 
		// if empty
		checkNotEmpty();

		// if removing the first item
		if (index == 0) {
			return this.removeFront();
		}
		// if removing last item
		if (index == this.size() - 1) {
			return this.removeBack();
		}
		// if removing from the middle
		// index counter
		int at = 0;
		// go through every node
		for (Node<T> current = start; current != null; current = current.next) {
			// find the node before the one we want to delete
			if (at == index - 1) {
				// save the node that will be deleted
				T removed = current.next.value;
				// set our current node to skip the one to be deleted
				current.next = current.next.next;
				// return deleted node
				return removed;
			} 
			at++;
		}
		// if nothing else could happen, your index doesn't exist
		throw new BadIndexError();
	}

	@Override
	public void addFront(T item) { // Efficiency: O(1)
		this.start = new Node<T>(item, start);
	}

	@Override
	public void addBack(T item) { // Efficiency: O(n) because of for loop 
		// if empty
		if (this.isEmpty()) {
			addFront(item);
		} else {
			// go through every node
			for (Node<T> current = start; current != null; current = current.next) {
				// find the last node
				if (current.next == null) {
					// point it at the added value
					current.next = new Node<T>(item, null);
					break;
				}
			}
		}
	}

	@Override
	public void addIndex(T item, int index) { // Efficiency: O(n) because of for loop 
		// if adding at the front
		if (index == 0 || this.isEmpty()) { 
			addFront(item);
		} else {
			// if adding in the middle
			// index counter
			int at = 0;
			// go through every node
			for (Node<T> current = start; current != null; current = current.next) {
				// find the node before the one we want to add
				if (at == index - 1) {
					// save the node that will be deleted
					Node<T> pointTo = current.next;
					// set our current node to the one to be added
					current.next = new Node<T>(item, pointTo);
					break;
				}
				at++;
			}
		}
	}

	@Override
	public T getFront() { // Efficiency: O(1)
		return start.value;
	}

	@Override
	public T getBack() { // Efficiency: O(n)
		// if there is only one item
		if (start.next == null) {
			return start.value;
		}

		// go through every node
		for (Node<T> current = start; current != null; current = current.next) {
			// find the last node
			if (current.next == null) {
				// return its value
				return current.value;
			}
		}
		throw new EmptyListError();
	}

	@Override
	public T getIndex(int index) { // Efficiency: O(n) because of for loop 
		// if empty
		checkNotEmpty();
		// if the index exists
		// index counter
		int at = 0;
		// go through every node
		for (Node<T> current = start; current != null; current = current.next) {
			// if we are at the given index
			if (at == index) {
				// return its value
				return current.value;
			}
			at++;
		} // we couldn't find the index
		throw new BadIndexError();
	}

	@Override
	public int size() { // Efficiency: O(n) because of for loop 
		int count = 0;
		for (Node<T> n = this.start; n != null; n = n.next) {
			count++;
		}
		return count;
	}

	@Override
	public boolean isEmpty() { // Efficiency: O(1)
		return start == null;
	}

	/**
	 * Helper method to throw the right error for an empty state.
	 */
	private void checkNotEmpty() { // Efficiency: O(1)
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
	}

	/**
	 * The node on any linked list should not be exposed. Static means we don't need
	 * a "this" of SinglyLinkedList to make a node.
	 * 
	 * @param <T> the type of the values stored.
	 */
	private static class Node<T> {
		/**
		 * What node comes after me?
		 */
		public Node<T> next;
		/**
		 * What value is stored in this node?
		 */
		public T value;

		/**
		 * Create a node with no friends.
		 * 
		 * @param value - the value to put in it.
		 */
		public Node(T value, Node<T> next) {
			this.value = value;
			this.next = next;
		}
	}

	/**
	 * I'm providing this class so that SinglyLinkedList can be used in a for loop
	 * for {@linkplain ChunkyLinkedList}. This Iterator type is what java uses for
	 * {@code for (T x : list) { }} lops.
	 * 
	 * @author jfoley
	 *
	 * @param <T>
	 */
	private static class Iter<T> implements Iterator<T> {
		/**
		 * This is the value that walks through the list.
		 */
		Node<T> current;

		/**
		 * This constructor details where to start, given a list.
		 * 
		 * @param list - the SinglyLinkedList to iterate or loop over.
		 */
		public Iter(SinglyLinkedList<T> list) { 
			this.current = list.start;
		}

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			T found = current.value;
			current = current.next;
			return found;
		}
	}

	/**
	 * Implement iterator() so that {@code SinglyLinkedList} can be used in a for
	 * loop.
	 * 
	 * @return an object that understands "next()" and "hasNext()".
	 */
	public Iterator<T> iterator() {
		return new Iter<>(this);
	}
}
