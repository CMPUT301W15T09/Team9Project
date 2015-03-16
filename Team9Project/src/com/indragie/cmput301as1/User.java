package com.indragie.cmput301as1;

import java.io.Serializable;

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
	

	

}
