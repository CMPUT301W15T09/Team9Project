/* 
 * Copyright (C) 2015 Indragie Karunaratne
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.indragie.cmput301as1;

import java.util.HashSet;

/**
 * Represents an observable object that can have one or more observers
 * that implement the interface {@link TypedObserver<T>}. After an
 * observable instance changes, calling the {@link #notifyObservers(T)} 
 * method causes observers to be notified of the change via a call to
 * their {@link #TypedObserver<T>.update(TypedObservable<T>, T)} method.
 * 
 * This class is the same as {@link java.util.Observable}, except that the
 * observers are typed. 
 */
public class TypedObservable<T> {
	private boolean changed;
	private HashSet<TypedObserver<T>> observers = new HashSet<TypedObserver<T>>();
	
	/**
	 * Adds a new observer to the set of observers.
	 * @param o The observer to add.
	 */
	public void addObserver(TypedObserver<T> o) {
		observers.add(o);
	}
	
	/**
	 * @return The number of observers.
	 */
	public int countObservers() {
		return observers.size();
	}
	
	/**
	 * Deletes an observer from the set of observers.
	 * @param o The observer to delete.
	 */
	public void deleteObserver(TypedObserver<T> o) {
		observers.remove(o);
	}
	
	/**
	 * Deletes all observers.
	 */
	public void deleteObservers() {
		observers.clear();
	}

	/** 
	 * @return Whether the changed flag has been set by a call to
	 * {@link #setChanged()}
	 */
	public boolean hasChanged() {
		return changed;
	}
	
	/**
	 * Clears the changed flag such that {@link #hasChanged()} will
	 * return false.
	 */
	protected void clearChanged() {
		changed = false;
	}
	
	/**
	 * Sets the changed flag such that {@link #hasChanged()} will
	 * return true.
	 */
	protected void setChanged() {
		changed = true;
	}
	
	/**
	 * If {@link #hasChanged()} returns true, this method will notify
	 * all observers that the observable has been updated and then
	 * clear the changed flag.
	 * 
	 * If {@link #hasChanged()} returns false, this method has no effect.
	 * 
	 * @param arg An optional argument to pass to observers.
	 */
	public void notifyObservers(T arg) {
		if (hasChanged()) {
			for (TypedObserver<T> o : observers) {
				o.update(this, arg);
			}
			clearChanged();
		}
	}
	
	/**
	 * Calls {@link #hasChanged()} with a null argument.
	 */
	public void notifyObservers() {
		notifyObservers(null);
	}
}
