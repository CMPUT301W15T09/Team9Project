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
 * Uniquely identifies an object in an ElasticSearch database.
 */
public class ElasticSearchObjectID {
	//================================================================================
	// Properties
	//================================================================================
	/**
	 * The name of the index. (e.g. "expenses")
	 */
	private String index;
	
	/**
	 * The name of the type. (e.g. "expense_claim")
	 */
	private String type; 
	
	/**
	 * The unique ID of the object. (e.g. "1" or "1ab45fed6c")
	 */
	private String id;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link ElasticSearchObjectID}
	 * @param index The name of the index.
	 * @param type The name of the type.
	 * @param id The unique ID of the object.
	 */
	public ElasticSearchObjectID(String index, String type, String id) {
		this.index = index;
		this.type = type;
		this.id = id;
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	/**
	 * @return The name of the index.
	 */
	public String getIndex() {
		return index;
	}
	
	/**
	 * @return The name of the type.
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @return The unique ID of the object.
	 */
	public String getID() {
		return id;
	}
	
	//================================================================================
	// Object
	//================================================================================
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((index == null) ? 0 : index.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		ElasticSearchObjectID other = (ElasticSearchObjectID) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
