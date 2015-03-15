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

import java.io.Serializable;

/**
 * Model object representing a tag
 */
public class Tag implements Serializable, Comparable<Tag>{
	private static final long serialVersionUID = 4055130777493746380L;
	

	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * Name of the tag
	 */
	private String name;
	
	//================================================================================
	// Constructor
	//================================================================================
	
	public Tag(String name) {
		this.name = name;
	}
	
	//================================================================================
	// Accessor
	//================================================================================
	
	public String getName() {
		return this.name;
	}
	
	//================================================================================
	// Comparable
	//================================================================================
	
	@Override
	public int compareTo(Tag tag) {
		return this.name.compareTo(tag.getName());
	}

}