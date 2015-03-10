package com.indragie.cmput301as1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListModel<T extends Comparable<? super T>> extends TypedObservable<List<T>> {

	protected ArrayList<T> list;
	
	/**
	 * Adds a object to the list of objects.
	 * @param o The object to add.
	 */
	public void add(T o) {
		list.add(o);
		Collections.sort(list);
	}
	

	/**
	 * Removes an existing object from the list of objects.
	 * @param o The object to remove.
	 */
	public boolean remove(T o) {
		if (list.remove(o)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Removes an existing object from the list of objects.
	 * @param index The index of the object to remove.
	 */
	public void remove(int index) {
		list.remove(index);
	}
	
	/**
	 * Remove all objects from the list of objects.
	 */
	public void removeAll() {
		list.clear();
	}
	
	/**
	 * Replaces an existing object.
	 * @param index The index of the object to replace.
	 * @param newO The new object to replace the existing object with.
	 */
	public void set(int index, T newO) {
		list.set(index, newO);
		Collections.sort(list);
	}
	
	/**
	 * @return The number of items in the list.
	 */
	public int count() {
		return list.size();
	}
	
}
