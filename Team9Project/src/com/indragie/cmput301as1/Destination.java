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
 * Model object representing a travel destination.
 */
public class Destination {
	//================================================================================
	// Properties
	//================================================================================
	/**
	 * The name of the destination.
	 */
	private String name;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link Destination}
	 * @param name The name of the destination.
	 */
	public Destination(String name) {
		this.name = name;
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}