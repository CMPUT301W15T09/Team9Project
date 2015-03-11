package com.indragie.cmput301as1;

import java.io.Serializable;

public class User implements Serializable {
	
	//================================================================================
	// Properties
	//================================================================================
	
	private String name;
	
	private int id;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	public User(String name){
		
	}

	
	//================================================================================
	// Accessors
	//================================================================================
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	

}
