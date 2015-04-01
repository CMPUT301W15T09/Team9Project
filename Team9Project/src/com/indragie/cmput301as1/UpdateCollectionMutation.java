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

public class UpdateCollectionMutation<T> extends CollectionMutation<T> {
	/**
	 * The index of the object that was updated.
	 */
	private int index;
	
	/**
	 * The old object (pre-update)
	 */
	private T oldObject;
	
	/**
	 * The new object (post-update)
	 */
	private T newObject;
	
	/**
	 * Creates a new instance of {@link UpdateCollectionMutation<T>}
	 * @param index The index of the object that was updated.
	 * @param oldObject The old object (pre-update)
	 * @param newObject The new object (post-update)
	 */
	public UpdateCollectionMutation(int index, T oldObject, T newObject) {
		super(MutationType.UPDATE);
		this.index = index;
		this.oldObject = oldObject;
		this.newObject = newObject;
	}
	
	/**
	 * @return The index of the object that was updated.
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * @return The old object (pre-update)
	 */
	public T getOldObject() {
		return oldObject;
	}
	
	/**
	 * @return The new object (post-update)
	 */
	public T getNewObject() {
		return newObject;
	}
}
