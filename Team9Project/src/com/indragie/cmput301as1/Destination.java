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

import java.io.Serializable;

/**
 * Model object representing a travel destination.
 */
public class Destination implements Serializable {
	private static final long serialVersionUID = -7090403546853416471L;

	//================================================================================
	// Properties
	//================================================================================
	/**
	 * The name of the destination.
	 */
	private String name;
	
	/**
	 * The reason of travel to the destination.
	 */
	private String travelReason;
	
	/**
	 * Location of the destination.
	 */
	private Geolocation location;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link Destination}
	 * @param name The name of the destination.
	 * @param travelReason The reason of travel to the destination.
	 */
	public Destination(String name, String travelReason) {
		this.name = name;
		this.travelReason = travelReason;
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	/**
	 * @return The name of the destination.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the destination.
	 * @param name The name of the destination.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return The reason of travel to the destination.
	 */
	public String getTravelReason() {
		return travelReason;
	}
	
	/**
	 * Sets the reason of travel to the destination.
	 * @param travelReason The reason of travel to the destination.
	 */
	public void setTravelReason(String travelReason) {
		this.travelReason = travelReason;
	}
	
	/**
	 * @return The location of the destination.
	 */
	public Geolocation getLocation() {
		return location;
	}
	
	//================================================================================
	// Object
	//================================================================================
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((travelReason == null) ? 0 : travelReason.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Destination other = (Destination) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (travelReason == null) {
			if (other.travelReason != null)
				return false;
		} else if (!travelReason.equals(other.travelReason))
			return false;
		return true;
	}
} 
