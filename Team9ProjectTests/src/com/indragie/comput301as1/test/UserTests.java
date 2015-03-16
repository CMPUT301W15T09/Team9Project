package com.indragie.comput301as1.test;

import junit.framework.TestCase;

import com.indragie.cmput301as1.User;

public class UserTests extends TestCase {

	User u;
	
	public UserTests() {
		super();
		
		// TODO Auto-generated constructor stub
	}
	protected void setUp() throws Exception {
		super.setUp();
		u = new User("Nic",1);
	}
	
	public void testUserName(){
		assertEquals("Nic", u.getName());
	}
	
	//Will need to be updated when online functionality is added
	public void testUserID(){
		User u = new User("Nic",1);
		assertEquals(1, u.getId());
	}

}
