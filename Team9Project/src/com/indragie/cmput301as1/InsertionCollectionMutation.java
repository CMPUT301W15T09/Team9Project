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

/**
 * Represents a mutation to a collection where an object was inserted.
 * @param <T> The type of the object that was inserted.
 */
public class InsertionCollectionMutation<T> extends CollectionMutation<T> {
	/**
	 * The index at which the object was inserted.
	 */
	private int index;
	
	/**
	 * The object that was inserted.
	 */
	private T object;
	
	/**
	 * Creates a new instance of {@link InsertionCollectionMutation<T>}
	 * @param index The index at which the object was inserted.
	 * @param object The object that was inserted.
	 */
	public InsertionCollectionMutation(int index, T object) {
		super(MutationType.INSERT);
		this.index = index;
		this.object = object;
	}
	
	/**
	 * @return The index at which the object was inserted.
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * @return The object that was inserted.
	 */
	public T getObject() {
		return object;
	}
}
