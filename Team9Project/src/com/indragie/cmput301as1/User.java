/* 
 * Copyright (C) 2015 Nic Carroll, Indragie Karunaratne
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
 * Model object representing a user object.
 */
public class User implements Serializable, ElasticSearchDocument {
	private static final long serialVersionUID = 5700320468581917557L;

	/**
	 * Type used for indexing on ElasticSearch.
	 */
	public static final String ELASTIC_SEARCH_TYPE = "user";

	//================================================================================
	// Properties
	//================================================================================
	/**
	 * Unique document ID.
	 */
	private ElasticSearchDocumentID documentID;
	
	/**
	 * The user's name.
	 */
	private String name;
	
	private double latitude;
	private double longitude;

	//================================================================================
	// Constructors
	//================================================================================

	/**
	 * Creates a new instance of {@link User}
	 * @param identifier A unique identifier for the user. This should typically be
	 * a device-specific identifier for our purposes.
	 * @param name The user's name.
	 */
	public User(String identifier, String name){
		this.name = name;
		this.documentID = new ElasticSearchDocumentID(
			ElasticSearchConfiguration.INDEX_NAME, 
			ELASTIC_SEARCH_TYPE,
			identifier
		);
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	/**
	 * @return The name of user.
	 */
	public String getName() {
		return name;
	}
	
	//================================================================================
	// ElasticSearchDocument
	//================================================================================
	
	/* (non-Javadoc)
	 * @see com.indragie.cmput301as1.ElasticSearchDocument#getDocumentID()
	 */
	@Override
	public ElasticSearchDocumentID getDocumentID() {
		return documentID;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLocation(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	//================================================================================
	// Object
	//================================================================================
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((documentID == null) ? 0 : documentID.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		User other = (User) obj;
		if (documentID == null) {
			if (other.documentID != null)
				return false;
		} else if (!documentID.equals(other.documentID))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
