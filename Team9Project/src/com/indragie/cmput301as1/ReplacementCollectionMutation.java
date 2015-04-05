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

import java.util.List;

/**
 * Represents a mutation to a collection where the entire collection
 * contents were replaced.
 * @param <T> The collection's object type.
 */
public class ReplacementCollectionMutation<T> extends CollectionMutation<T> {
	/**
	 * The old contents of the collection.
	 */
	private List<T> oldContents;
	
	/**
	 * The new contents of the collection.
	 */
	private List<T> newContents;
	
	/**
	 * Creates a new instance of {@link ReplacementCollectionMutation}
	 * @param oldContents The old contents of the collection.
	 * @param newContents The new contents of the collection.
	 */
	public ReplacementCollectionMutation(List<T> oldContents, List<T> newContents) {
		super(CollectionMutation.MutationType.REPLACEMENT);
		this.oldContents = oldContents;
		this.newContents = newContents;
	}
	
	/**
	 * @return The old contents of the collection.
	 */
	public List<T> getOldContents() {
		return oldContents;
	}
	
	/**
	 * @return The new contents of the collection.
	 */
	public List<T> getNewContents() {
		return newContents;
	}
}
