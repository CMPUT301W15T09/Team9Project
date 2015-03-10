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
 * Model object that contains the section, item, and list view indices for
 * a {@link ListView} that uses a {@link SectionedListAdapter};
 */
public class SectionedListIndex {
	//================================================================================
	// Properties
	//================================================================================
	
	private int sectionIndex;
	private int itemIndex;
	private int listViewIndex;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link SectionedListIndex}
	 * @param sectionIndex The index of the section.
	 * @param itemIndex The index of the item relative to the section.
	 * @param listViewIndex The index of the item in the list view (i.e. the "position")
	 */
	public SectionedListIndex(int sectionIndex, int itemIndex, int listViewIndex)  {
		this.sectionIndex = sectionIndex;
		this.itemIndex = itemIndex;
		this.listViewIndex = listViewIndex;
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	/**
	 * @return The index of the section.
	 */
	public int getSectionIndex() {
		return sectionIndex;
	}
	
	/**
	 * @return The index of the item relative to the section.
	 */
	public int itemIndex() {
		return itemIndex;
	}
	
	/**
	 * @return The index of the item in the list view (i.e. the "position")
	 */
	public int listViewIndex() { 
		return listViewIndex;
	}
}
