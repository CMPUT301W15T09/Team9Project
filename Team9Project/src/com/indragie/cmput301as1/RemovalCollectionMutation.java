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
 * Represents a mutation to a collection where an object was
 * removed from the collection.
 * @param <T> The type of the object that was removed.
 */
public class RemovalCollectionMutation<T> extends CollectionMutation<T> {
	/**
	 * The index of the object that was removed.
	 */
	private int index;
	
	/**
	 * The object that was removed.
	 */
	private T object;
	
	/**
	 * Creates a new instance of {@link RemovalCollectionMutation<T>}
	 * @param index The index of the object that was removed.
	 * @param object The object that was removed.
	 */
	public RemovalCollectionMutation(int index, T object) {
		super(MutationType.REMOVE);
		this.index = index;
		this.object = object;
	}
	
	/**
	 * @return The index of the object that was removed.
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * @return The object that was removed.
	 */
	public T getObject() {
		return object;
	}
}
