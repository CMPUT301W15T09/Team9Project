/* 
 * Copyright (C) 2015 Andrew Zhong
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

public class UpdateListMutation<T> extends CollectionMutation<T> {

	/**
	 * The old list of objects (pre-update)
	 */
	private List<T> oldList;
	
	/**
	 * The new list of objects (post-update)
	 */
	private List<T> newList;
	
	/**
	 * Creates a new instance of {@link UpdateCollectionMutation<T>}
	 * @param oldObject The old list of objects (pre-update)
	 * @param newObject The new list of objects (post-update)
	 */
	public UpdateListMutation(List<T> oldList, List<T> newList) {
		super(MutationType.UPDATE);
		this.oldList = oldList;
		this.newList = newList;
	}
	
	/**
	 * @return The old object (pre-update)
	 */
	public List<T> getOldList() {
		return oldList;
	}
	
	/**
	 * @return The new object (post-update)
	 */
	public List<T> getNewList() {
		return newList;
	}
}