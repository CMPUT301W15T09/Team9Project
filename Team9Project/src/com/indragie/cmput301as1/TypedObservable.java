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
	
	public void addObserver(TypedObserver<T> o) {
		observers.add(o);
	}
	
	public int countObservers() {
		return observers.size();
	}
	
	public void deleteObserver(TypedObserver<T> o) {
		observers.remove(o);
	}
	
	public void deleteObservers() {
		observers.clear();
	}

	public boolean hasChanged() {
		return changed;
	}
	
	protected void clearChanged() {
		changed = false;
	}
	
	protected void setChanged() {
		changed = true;
	}
	
	public void notifyObservers(T arg) {
		if (hasChanged()) {
			for (TypedObserver<T> o : observers) {
				o.update(this, arg);
			}
			clearChanged();
		}
	}
	
	public void notifyObservers() {
		notifyObservers(null);
	}
}
