package com.indragie.cmput301as1.test;

import java.util.ArrayList;

import com.indragie.cmput301as1.ExpenseClaim;
import com.indragie.cmput301as1.R;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

public class ApprovalTests extends ActivityInstrumentationTestCase2<T> {

	public ApprovalTests(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	// All tests 5 were from 7 previously
	
	//================================================================================
	// Test 5_1
	//================================================================================
	public void testListSubmittedExpenseClaims(){
		ArrayList<ExpenseClaim> submittedList = new ArrayList<ExpenseClaim>();
		ExpenseClaim claim1 = new ExpenseClaim(claimantName, startDate, destinations, status, total, approverName);
		ExpenseClaim claim2 = new ExpenseClaim(claimantName, startDate, destinations, status, total, approverName);
		ExpenseClaim claim3 = new ExpenseClaim(claimantName, startDate, destinations, status, total, approverName);
		submittedList.add(claim1);
		submittedList.add(claim2);
		submittedList.add(claim3);
		ExpenseClaimListActivity activity = showList(submittedList);
		Intent intent = getIntent();
		View current = activity.getWindow().getDecorView();
		TextView checkView = (TextView) activity.findViewById(R.id.intentText);
		ViewAsserts.assertOnScreen(current, checkView);
	}
	
	//================================================================================
	// Test 5_2_1
	//================================================================================
	public void testDateSortSubmittedExpenseClaims(){
		//testing sorting by travel date from oldest to most recent
		ArrayList<ExpenseClaim> submittedList = new ArrayList<ExpenseClaim>();
		ExpenseClaim claim1 = new ExpenseClaim(claimantName, startDate, destinations, status, total, approverName);
		ExpenseClaim claim2 = new ExpenseClaim(claimantName, startDate, destinations, status, total, approverName);
		ExpenseClaim claim3 = new ExpenseClaim(claimantName, startDate, destinations, status, total, approverName);
		submittedList.add(claim1);
		submittedList.add(claim3);
		submittedList.add(claim2);
		submittedList.sortDate();
		ArrayList<ExpenseClaim> submittedList2 = new ArrayList<String>();
		submittedList2.add(claim1);
		submittedList2.add(claim2);
		submittedList2.add(claim3);
		assertEquals("Travel date order comparison not correct", submittedList, sbmittedList2);
	}
	
	//================================================================================
	// Test 5_2_2
	//================================================================================
	public void testEntrySortSubmittedExpenseClaims(){
		//testing reversal of items by order of entry
		ArrayList<ExpenseClaim> submittedList = new ArrayList<ExpenseClaim>();
		ExpenseClaim claim1 = new ExpenseClaim(claimantName, startDate, destinations, status, total, approverName);
		ExpenseClaim claim2 = new ExpenseClaim(claimantName, startDate, destinations, status, total, approverName);
		ExpenseClaim claim3 = new ExpenseClaim(claimantName, startDate, destinations, status, total, approverName);
		submittedList.add(claim1);
		submittedList.add(claim3);
		submittedList.add(claim2);
		submittedList.sortEntry();
		ArrayList<ExpenseClaim> submittedList2 = new ArrayList<String>();
		submittedList2.add(claim2);
		submittedList2.add(claim3);
		submittedList2.add(claim1);
		assertEquals("Travel date order comparison not correct", submittedList, sbmittedList2);
	}
	
	//================================================================================
	// Test 5_3_1
	//================================================================================
	public void testEditComment(){
		String comment = "Everything is valid";
		ArrayList<ExpenseClaim> submittedList = new ArrayList<ExpenseClaim>();
		submittedList.editComment(comment);
		assertEquals("Comment not correct.", comment, submittedList.getComment());
	}
	
	//================================================================================
	// Test 5_3_2
	//================================================================================
	public void testEditPhotoReceipt(){
		//should overwrite an existing image if it exists
		ArrayList<ExpenseClaim> submittedList = new ArrayList<ExpenseClaim>();
		//addReceipt sends intent to camera app or photo stream to get a picture to attach
		submittedList.addReceipt();
		assertTrue("Receipt not attached to submitted expense item list!", submittedList.getReceiptUri().equals(null));
	}
	
	//================================================================================
	// Test 5_4_1
	//================================================================================
	public void testApproveExpenseClaim{
		ArrayList<ExpenseClaim> submittedList = new ArrayList<ExpenseClaim>();
		ExpenseClaim claim1 = new ExpenseClaim(claimantName, startDate, destinations, status, total, approverName);
		submittedList.add(claim1);
		assertEquals("submittedList size not 1", 1, submittedList.getSize());
		submittedList.setStatusApproved();
		assertEquals("Status not set to approved", "Approved", submittedList.getStatus());
	}
	
	//================================================================================
	// Test 5_4_2
	//================================================================================
	public void testDisapproveExpenseClaim(){
		ArrayList<ExpenseClaim> submittedList = new ArrayList<ExpenseClaim>();
		ExpenseClaim claim1 = new ExpenseClaim(claimantName, startDate, destinations, status, total, approverName);
		submittedList.add(claim1);
		assertEquals("submittedList size not 1", 1, submittedList.getSize());
		submittedList.setStatusDispproved();
		assertEquals("Status not set to disapproved", "Dispproved", submittedList.getStatus());
	}
	
	//================================================================================
	// Test 5_5
	//================================================================================
	protected void testReturnApprove() {
        
	    
		ExpenseClaim claim = new ExpenseClaim("Some claim","description", new Date(2015, 01, 20), new Date(2015, 01, 31), null);
		ExpenseClaim claim2 = new ExpenseClaim("Some other claim", "description", new Date(2015, 01, 20), new Date(2015, 01, 25), null)    
	        
	        assertFalse(claim.setApproved("John"));
	        assertFalse(claim.setDisapproved("John"));
	        
	        assertTrue(claim.setApproved("Satyen"));
	        assertTrue(claim.setDisapproved("Satyen"));
	        
    	} 
}
