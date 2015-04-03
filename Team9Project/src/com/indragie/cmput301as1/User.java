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
import java.util.UUID;

/**
 * Model object representing a user object.
 */
public class User implements Serializable, ElasticSearchDocument {
	private static final long serialVersionUID = 9042593982087737387L;

	//================================================================================
	// Properties
	//================================================================================
	/**
	 * Unique document ID.
	 */
	private ElasticSearchDocumentID documentID = new ElasticSearchDocumentID(
		ElasticSearchConfiguration.INDEX_NAME, 
		"user", 
		UUID.randomUUID().toString()
	);
	
	/**
	 * The user's name.
	 */
	private String name;

	//================================================================================
	// Constructors
	//================================================================================

	/**
	 * Creates a new instance of {@link User}
	 * @param name The user's name.
	 */
	public User(String name){
		this.name = name;
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
}
