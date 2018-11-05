package edu.smith.cs.csc212.p6;

import edu.smith.cs.csc212.p6.errors.BadIndexError;
import edu.smith.cs.csc212.p6.errors.EmptyListError;

public class DoublyLinkedList<T> implements P6List<T> {
	private Node<T> start;
	private Node<T> end;

	/**
	 * A doubly-linked list starts empty.
	 */
	public DoublyLinkedList() {
		this.start = null;
		this.end = null;
	}

	@Override
	public T removeFront() {  // Efficiency: O(1)
		// checks if list is empty
		checkNotEmpty();
		// find the front
		T removed = start.value;
		// set front to next value
		start = start.after;

		// if the start is not pointing to null, point its before to null
		if (start != null) {
			start.before = null;
		}
		// return previous
		return removed;

	}

	@Override
	public T removeBack() { // Efficiency: O(1)
		// if empty
		checkNotEmpty();
		// if there is only one value
		if (this.size() == 1) {
			T removed = start.value;
			start = null;
			return removed;
		} else {
			// find the back
			T removed = end.value;
			// set back to previous value
			end = end.before;
			// if the end is not pointing to null, point it to null
			if (end != null) {
				end.after = null;
			}

			// return our previous end
			return removed;
		}
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
		if (index == this.size()) {
			return this.removeBack();
		}
		// if removing from the middle
		// index counter
		int at = 0;
		// go through every node
		for (Node<T> current = start; current != null; current = current.after) {
			// find the node before the one we want to delete
			if (at == index - 1) {
				// save the node that will be deleted
				T removed = current.after.value;
				// set our current node to skip the one to be deleted
				current.after = current.after.after;
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
		// save what it was before
		Node<T> before = this.start;
		// set start to the new node
		this.start = new Node<T>(item);
		// set start to point to the previous start
		start.after = before;
		// if it's not pointing to null, point its before to null
		if (start != null) {
			start.before = null;
		}
	}

	@Override
	public void addBack(T item) { // Efficiency: O(1)
		// if empty just add to front
		if (this.isEmpty()) {
			addFront(item);
			// otherwise
		} else {	
			//save the new node in a variable
			Node<T> newNode = new Node<T>(item);
			// set the before of the new node to the previous end
			newNode.before = end;
			// set the after of the new node to null
			newNode.after = null;
			// set the end to point to the new node
			this.end.after = newNode;
			// set the end to the new node
			this.end = newNode;	
		}
	}

	@Override
	public void addIndex(T item, int index) { // Efficiency: O(n) because of for loop 
		// if adding at the front or to an empty list
		if (index == 0 || this.isEmpty()) {
			addFront(item);
		} else {
			// if adding in the middle
			// index counter
			int at = 0;
			// go through every node
			for (Node<T> current = start; current != null; current = current.after) {
				// find the node before the one we want to add
				if (at == index - 1) {
					// save the new node in a variable
					Node<T> newNode = new Node<T>(item);
					// point new node's after to the node next to the current one
					newNode.after = current.after;
					// point new node's before to our current node
					newNode.before = current;
					// point the node after the current one back to our new node
					current.after.before = newNode;
					// point our current node to the new node
					current.after = newNode;	
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
	public T getBack() { // Efficiency: O(1)
		return end.value;
	}

	@Override
	public T getIndex(int index) { // Efficiency: O(n) because of for loop 
		// if empty
		if (this.isEmpty()) {
			throw new EmptyListError();
		} else {
			// if the index exists
			// index counter
			int at = 0;
			// go through every node
			for (Node<T> current = start; current != null; current = current.after) {
				// if we are at the given index
				if (at == index) {
					// return its value
					return current.value;
				}
				at++;
			} // we couldn't find the index
		}
		throw new BadIndexError();
	}

	@Override
	public int size() { // Efficiency: O(n) because of for loop 
		int count = 0;
		for (Node<T> n = start; n != null; n = n.after) {
			count++;
		}
		return count;
	}

	@Override
	public boolean isEmpty() { // Efficiency: O(1)
		return start == null;
	}

	private void checkNotEmpty() { // Efficiency: O(1)
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
	}

	/**
	 * The node on any linked list should not be exposed. Static means we don't need
	 * a "this" of DoublyLinkedList to make a node.
	 * 
	 * @param <T> the type of the values stored.
	 */
	private static class Node<T> {
		/**
		 * What node comes before me?
		 */
		public Node<T> before;
		/**
		 * What node comes after me?
		 */
		public Node<T> after;
		/**
		 * What value is stored in this node?
		 */
		public T value;

		/**
		 * Create a node with no friends.
		 * 
		 * @param value - the value to put in it.
		 */
		public Node(T value) {
			this.value = value;
			this.before = null;
			this.after = null;
		}
	}
}
