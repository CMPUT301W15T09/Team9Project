package com.indragie.cmput301as1;

import java.io.Serializable;

import android.content.Context;

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
	private static final String EXPENSE_CLAIM_FILENAME = "user";
	private Context context;
	
	
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
