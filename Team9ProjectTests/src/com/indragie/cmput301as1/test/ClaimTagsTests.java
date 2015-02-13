package com.indragie.cmput301as1.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.test.ActivityInstrumentationTestCase2;

public class ClaimTagsTests extends ActivityInstrumentationTestCase2<T> {

	public ClaimTagsTests(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	//================================================================================
	// Test 2_1
	//================================================================================
	
	public void testAddClaimToGroups() {
	    String tag1 = "tag 1";
	    String tag5 = "tag 5";
	    ExpenseClaim claim = new ExpenseClaim("Some claim","description", new Date(2015, 01, 20), new Date(2015, 01, 31), null);
	    ExpenseClaim claim2 = new ExpenseClaim("Some other claim", "description", new Date(2015, 01, 20), new Date(2015, 01, 25), null);
	    
	    claim.addTag(tag1);
	    
	    assertTrue(claim.hasTag());
	    assertFalse(claim2.hasTag());
	    
	    assertEquals(claim.numTags() == 1);
	    assertEquals(claim2.numTags() == 0);
	    
	}
	
	//================================================================================
	// Test 2_2
	//================================================================================
	public void testManipulateTags() {
	    Tags tag = new Tags();
	    
	    assertNull(tags.listTags());
	    
	    
	    tags.addTag(tag1);
	    tags.addTag(tag5);
	    
	    List<String> check = new ArrayList<String>();
	    check.add(tag1);
	    check.add(tag5);
	    assertArrayEquals(check, tags.listTags());
	    
	    
	    tags.renameTag(tag1, tag3);
	    
	    check.set(check.indexOf(tag1), tag5);
	    assertArrayEquals(check,tag.listTags());
	    
	    
	    tags.removeTag(tag3);
	    check.remove(tag3);
	    assertArrayEquals(check, tags.ListTag());
	}
	
	//================================================================================
	// Test 2_3
	//================================================================================
		
	public void testMatchTags() {
	    String tag1 = "tag 1";
	    String tag5 = "tag 5";
	    String tag3 = "tag 3";
	    
	    ExpenseClaim claim = new ExpenseClaim("Some claim","description", new Date(2015, 01, 20), new Date(2015, 01, 31), null);
	    ExpenseClaim claim2 = new ExpenseClaim("Some other claim", "description", new Date(2015, 01, 20), new Date(2015, 01, 25), null)
	    
	    claim.addTag(tag1);
	    claim2.addTag(tag5);
	    claim2.addTag(tag3);
	    
	    assertTrue(claim.hasTag(tag1));
	    assertFalse(claim.hasTag(tag3));
	    
	    assertTrue(claim2.hasTag(tag3));
	    assertTrue(claim2.hasTag(tag5));
		
	}

}
