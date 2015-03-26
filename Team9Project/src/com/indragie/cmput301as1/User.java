/* 
 * Copyright (C) 2015 Nic Carroll
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
import java.util.ArrayList;
import java.util.List;

/**
 * Model object representing a user object.
 */
public class User implements Serializable {
	
	//================================================================================
	// Properties
	//================================================================================
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private int id;
	private List<String> claimIDlist = new ArrayList<String>();
	
	
	//================================================================================
	// Constructors
	//================================================================================
	
	public User(String name, int id){
		this.name = name;
		this.id = id;
		
	}

	
	//================================================================================
	// Accessors
	//================================================================================
	/**
	 * @return the name of user
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the id number of user
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the list of expense claim ids
	 */
	public List<String> getClaimIDList() {
		return claimIDlist;
	}
	
	/**
	 * adds an id to the list of expense claim ids
	 * @param id
	 */
	public void addClaimID(String id) {
		this.claimIDlist.add(id);
	}
	
	/**
	 * removes and id from the list of expense claim ids
	 * @param id
	 */
	public void removeClaimID(String id) {
		this.claimIDlist.remove(id);
	}

}
