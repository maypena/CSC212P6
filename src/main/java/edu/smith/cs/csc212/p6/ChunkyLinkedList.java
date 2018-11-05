package edu.smith.cs.csc212.p6;

import edu.smith.cs.csc212.p6.errors.BadIndexError;
import edu.smith.cs.csc212.p6.errors.EmptyListError;


/**
 * This is a data structure that has an array inside each node of a Linked List.
 * Therefore, we only make new nodes when they are full. Some remove operations
 * may be easier if you allow "chunks" to be partially filled.
 * 
 * @author jfoley
 * @param <T> - the type of item stored in the list.
 */
public class ChunkyLinkedList<T> implements P6List<T> {
	private int chunkSize;
	private SinglyLinkedList<FixedSizeList<T>> chunks;

	public ChunkyLinkedList(int chunkSize) {
		this.chunkSize = chunkSize;
		// We use chunks before creating it:
		chunks = new SinglyLinkedList<>();
		chunks.addBack(new FixedSizeList<>(chunkSize));
	}
	
	// Efficiency: O(n) because it uses removeFront from FixedSizeList which is O(n). 
	@Override
	public T removeFront() { 
		// if the chunk is empty
		if (this.isEmpty()) {
			throw new EmptyListError();
		} else {
			// find the first chunk
			FixedSizeList<T> firstChunk = this.chunks.getFront();
			// remove the first chunk and return its value
			return firstChunk.removeFront();
		}
	}
	
	// Efficiency: O(n) because of .getBack from SinglyLinkedList which is O(n)
	@Override
	public T removeBack() { 
		// if the chunk is empty
		if (this.isEmpty()) {
			throw new EmptyListError();
		} else {
			// find the last chunk
			FixedSizeList<T> lastChunk = this.chunks.getBack();
			// remove the last chunk and return its value
			return lastChunk.removeBack();
		}
	}
	// Efficiency: O(n) because of it has a for loop
	// and it has removeIndex from FixedSizeList which both have O(n).
	@Override
	public T removeIndex(int index) { 
		// check if chunk is empty
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
		// bookshelf counter
		int start = 0;
		// for every chunk in chunks
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();

			// check whether the index should be in this chunk:
			if (start <= index && index < end) {
				return chunk.removeIndex(index - start);
			}

			// update bounds of next chunk.
			start = end;
		}
		throw new BadIndexError();
	}
	// Efficiency: O(1), all extra methods are also O(1).
	@Override
	public void addFront(T item) {
		// find the first chunk
		FixedSizeList<T> firstChunk = this.chunks.getFront();
		// if the first chunk is full
		if (firstChunk.size() == chunkSize) {
			// create a new chunk
			FixedSizeList<T> newChunk = new FixedSizeList<T>(chunkSize);
			// put the new chunk at the front of linked list
			chunks.addFront(newChunk);
			// add item to the front of new chunk
			newChunk.addFront(item);
			// if it's not full
		} else {
			// just add item to that first chunk
			firstChunk.addFront(item);
		}
	}
	// Efficiency: O(n) because .getBack and .addBack from SinglyLinkedList
	// have an efficiency of O(n).
	@Override
	public void addBack(T item) {
		// find the last chunk
		FixedSizeList<T> lastChunk = this.chunks.getBack();
		// if the last chunk is full
		if (lastChunk.size() == chunkSize) {
			// create a new chunk
			FixedSizeList<T> newChunk = new FixedSizeList<T>(chunkSize);
			// put the new chunk at the back of the linked list
			chunks.addBack(newChunk);
			// add item to the back of the new chunk
			newChunk.addBack(item);
			// if it's not full
		} else {
			// just add item to that first chunk
			lastChunk.addBack(item);
		}

	}
	// Efficiency: O(n) because it has a for loop and it uses .addIndex 
	// form SinglyLinkedList which is O(n).
	@Override
	public void addIndex(T item, int index) {
		// check if chunk is empty
		if (this.isEmpty()) {
			this.addFront(item);
			return;
		}
		// start an index
		int chunkNum = 0;
		// something else
		int start = 0;
		// for every chunk in chunks
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate the bounds
			int end = start + chunk.size();
			// once we're at your index
			if (chunkNum <= index && index <= end) {
				// if the chunk is full
				if (chunk.size() == chunkSize) {
					// make a new chunk
					FixedSizeList<T> newChunk = new FixedSizeList<T>(chunkSize);
					// add the chunk to chunks
					chunks.addIndex(newChunk, chunkNum + 1);
					// add the item to the front of the chunk
					newChunk.addFront(item);
				// if the chunk is not full
				} else {
					// add the item to the front
					chunk.addIndex(item, index - start);
				}
				// stop
				return;
			}
			// Bookkeeping so that we move on to the next chunk 
			start = end;
			// we have moved on to a new node so add 1
			chunkNum++;
		}
	}
	// Efficiency: O(1) because both getFronts use O(1).
	@Override
	public T getFront() {
		return this.chunks.getFront().getFront();
	}
	
	// Efficiency: O(n) because .getBack() from SinglyLinkedList is O(n).
	@Override
	public T getBack() {
		return this.chunks.getBack().getBack();
	}
	
	// Efficiency: O(n) because of for loop 
	@Override
	public T getIndex(int index) {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();

			// Check whether the index should be in this chunk:
			if (start <= index && index < end) {
				return chunk.getIndex(index - start);
			}

			// update bounds of next chunk.
			start = end;
		}
		throw new BadIndexError();
	}
	// Efficiency: O(n) because of for loop 
	@Override
	public int size() {
		int total = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			total += chunk.size();
		}
		return total;
	}
	// Efficiency: O(1) 
	@Override
	public boolean isEmpty() {
		return this.chunks.getFront().isEmpty();
	}
}
