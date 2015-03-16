package com.indragie.cmput301as1.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import com.indragie.cmput301as1.CreationDateAscendingComparator;
import com.indragie.cmput301as1.CreationDateDescendingComparator;
import com.indragie.cmput301as1.ExpenseClaim;
import com.indragie.cmput301as1.ExpenseClaimListActivity;
import com.indragie.cmput301as1.StartDateAscendingComparator;
import com.indragie.cmput301as1.StartDateDescendingComparator;

import junit.framework.TestCase;

public class ClaimSortTest extends TestCase {
	
	private ArrayList<ExpenseClaim> claims;
	
	// startDate in format (YYYY, MM, DD)
	Date date1 = new Date(2000, 01, 01);
	Date date2 = new Date(1995, 01, 01);
	
	// creationDate in format (YYYY, MM, DD, Hr, Min, Sec)
	Date creation1 = new Date(2000, 01, 01, 01, 01, 01);
	Date creation2 = new Date(2000, 01, 01, 01, 00, 00);
	
	// ExpenseClaim(name, description, startDate, endDate, status, creationDate)
	ExpenseClaim claim1 = new ExpenseClaim(null, null, date1, null, null, creation1);
	ExpenseClaim claim2 = new ExpenseClaim(null, null, date2, null, null, creation2);
	
	public void testStart() {
		// call the list of claims
		claims = ExpenseClaimListActivity.getClaims();
		Collections.sort(claims, new StartDateAscendingComparator());
		
		// claim2 should now be in front
		assert(claims.get(0).getStartDate().equals(claim2.getStartDate()));
		
		Collections.sort(claims, new StartDateDescendingComparator());
		
		// claim1 should be first now
		assert(claims.get(0).getStartDate().equals(claim1.getStartDate()));
	}
	
	public void testCreation() {
		// call the list of claims
		claims = ExpenseClaimListActivity.getClaims();
		Collections.sort(claims, new CreationDateAscendingComparator());
				
		// claim2 should now be in front
		assert(claims.get(0).getCreationDate().equals(claim2.getCreationDate()));
				
		Collections.sort(claims, new CreationDateDescendingComparator());
				
		// claim1 should be first now
		assert(claims.get(0).getCreationDate().equals(claim1.getCreationDate()));
	}
}
