package com.indragie.cmput301as1.test;

import java.util.Date;

import android.test.ActivityInstrumentationTestCase2;

public class OfflineTests extends ActivityInstrumentationTestCase2<T> {

	public OfflineTests(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	
	//================================================================================
		// Test 6_1 from 8_1
		//================================================================================
		protected void testOfflinePush() {
			Claim claim = new Claim("Some claim", new Date(2015, 01, 20), new Date(2015, 01, 31));
			
			saveInFile(claim);
			
			assertEquals(claim, loadFromFile());
			
			claim.addTag("Adding a tag to make this claim different");
			
			assertNotEquals(claim, loadFromFile());
		}
}
