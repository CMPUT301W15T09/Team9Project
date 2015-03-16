package com.indragie.comput301as1.test;

import java.util.Date;
import java.util.List;

import com.indragie.cmput301as1.CreationDateAscendingComparator;
import com.indragie.cmput301as1.CreationDateDescendingComparator;
import com.indragie.cmput301as1.ExpenseClaim;
import com.indragie.cmput301as1.ExpenseClaimSortActivity;
import com.indragie.cmput301as1.StartDateAscendingComparator;
import com.indragie.cmput301as1.StartDateDescendingComparator;

import android.content.Intent;
import junit.framework.TestCase;

public class ExpenseClaimSortTests extends TestCase {
	private static final String EXPENSE_CLAIM_SORT = null;
	
	public void testStart() {
		// startDate in format (YYYY, MM, DD)
		Date date1 = new Date(2000, 01, 01);
		Date date2 = new Date(1995, 01, 01);
		
		// creationDate in format (YYYY, MM, DD, Hr, Min, Sec)
		Date creation1 = new Date(2000, 01, 01, 01, 01, 01);
		Date creation2 = new Date(2000, 01, 01, 01, 00, 00);
		
		// ExpenseClaim(name, description, startDate, endDate, status, creationDate)
		ExpenseClaim claim1 = new ExpenseClaim(null, null, date1, null, null, creation1);
		ExpenseClaim claim2 = new ExpenseClaim(null, null, date2, null, null, creation2);
		
		// add the list of claims to listmodel
		listmodel.add(claim1);
		listmodel.add(claim2);
		
		// get the list of claims
		List<ExpenseClaim> claims = listmodel.getExpenseClaims();
		
		// start the sorts
		Intent data = getStartDateAscendingIntent();
		listModel.setComparator(data.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT));

		// claim2 should now be in front
		assert(claims.get(0).getStartDate().equals(claim2.getStartDate()));
		
		data = getStartDateDescendingIntent();
		listModel.setComparator(data.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT));
		
		// claim1 should be first now
		assert(claims.get(0).getStartDate().equals(claim1.getStartDate()));

		data = getCreationDateAscendingIntent();
		listModel.setComparator(data.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT));
				
		// claim2 should now be in front
		assert(claims.get(0).getCreationDate().equals(claim2.getCreationDate()));
				
		data = getCreationDateDescendingIntent();
		listModel.setComparator(data.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT));
				
		// claim1 should be first now
		assert(claims.get(0).getCreationDate().equals(claim1.getCreationDate()));
	}
	
	public Intent getStartDateAscendingIntent() {
		Intent intent = new Intent();
		intent.putExtra(EXPENSE_CLAIM_SORT, new StartDateAscendingComparator());
		return intent;
	}
	
	public Intent getStartDateDescendingIntent() {
		Intent intent = new Intent();
		intent.putExtra(EXPENSE_CLAIM_SORT, new StartDateDescendingComparator());
		return intent;
	}
	
	public Intent getCreationDateAscendingIntent() {
		Intent intent = new Intent();
		intent.putExtra(EXPENSE_CLAIM_SORT, new CreationDateAscendingComparator());
		return intent;
	}
	
	public Intent getCreationDateDescendingIntent() {
		Intent intent = new Intent();
		intent.putExtra(EXPENSE_CLAIM_SORT, new CreationDateDescendingComparator());
		return intent;
	}
}
