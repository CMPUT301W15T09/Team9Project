package com.indragie.cmput301as1.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import com.indragie.cmput301as1.ExpenseClaim;
import com.indragie.cmput301as1.ExpenseClaimListActivity;
import com.indragie.cmput301as1.StartDateAscendingComparator;

import junit.framework.TestCase;

public class ClaimSortTest extends TestCase {
	
	// startDate in format (YYYY, MM, DD)
	Date date1 = new Date(2000, 01, 01);
	Date date2 = new Date(1995, 01, 01);
	
	// creationDate in format
	
	
	// ExpenseClaim(name, description, startDate, endDate, status, creationDate)
	ExpenseClaim claim1 = new ExpenseClaim(null, null, date1, null, null, null);
	ExpenseClaim claim2 = new ExpenseClaim(null, null, date2, null, null, null);
	
	// call the list of claims
	ArrayList<ExpenseClaim> claims = ExpenseClaimListActivity.getClaims();
	Collections.sort(claims, new StartDateAscendingComparator());
	
	// claim2 should now be in front
	assert(claims.get(0).getStartDate().equals(claim2.getStartDate()));
	
	Collections.sort(claims, new StartDateDescendingComparator());
	
	assert(claims.get(0).getStartDate().equals(claim1.getStartDate()));
}
