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
 * Represents a single mutation to a collection.
 */
public abstract class CollectionMutation<T> {
	/**
	 * The type of the collection mutation.
	 */
	public enum MutationType { 
		/**
		 * An object was inserted into a collection.
		 */
		INSERT, 
		/**
		 * An object was removed from a collection.
		 */
		REMOVE, 
		/**
		 * An existing object in a collection was replaced with
		 * a new one.
		 */
		UPDATE,
		/**
		 * The entire collection contents were replaced.
		 */
		REPLACEMENT
	}
	
	/**
	 * The type of the collection mutation.
	 */
	private MutationType mutationType;
	
	/**
	 * Creates a new instance of {@link CollectionMutation}
	 * @param mutationType The type of the collection mutation.
	 */
	protected CollectionMutation(MutationType mutationType) {
		this.mutationType = mutationType;
	}
	
	/**
	 * @return The type of the collection mutation.
	 */
	public MutationType getMutationType() {
		return mutationType;
	}
}
