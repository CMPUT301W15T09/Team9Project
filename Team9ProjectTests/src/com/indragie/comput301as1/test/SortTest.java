package com.indragie.comput301as1.test;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.test.AndroidTestCase;
import android.util.Log;

import com.indragie.cmput301as1.CreationDateAscendingComparator;
import com.indragie.cmput301as1.CreationDateDescendingComparator;
import com.indragie.cmput301as1.ExpenseClaim;
import com.indragie.cmput301as1.ExpenseClaimSortActivity;
import com.indragie.cmput301as1.ListModel;
import com.indragie.cmput301as1.StartDateAscendingComparator;
import com.indragie.cmput301as1.StartDateDescendingComparator;

public class SortTest extends AndroidTestCase {
	
private static final String EXPENSE_CLAIM_SORT = "com.indragie.cmput301as1.EXPENSE_CLAIM";
	
	@SuppressWarnings("unchecked")
	public void testStart() throws InterruptedException {
		// startDate in format (YYYY, MM, DD)
		Date date1 = new Date(2000, 01, 01);
		Date date2 = new Date(1995, 01, 01);
		
		// ExpenseClaim(name, description, startDate, endDate, status, creationDate)
		ExpenseClaim claim1 = new ExpenseClaim(null, null, date1, null, null);
		Thread.sleep(2000);
		ExpenseClaim claim2 = new ExpenseClaim(null, null, date2, null, null);
		
		ListModel<ExpenseClaim> listmodel = new ListModel<ExpenseClaim>("claims.dat", getContext());
		
		/*
		String test3 = claim1.getStartDate().toString();
		String test2 = claim2.getStartDate().toString();		
		Log.d(test3, test2);
		*/
		// add the list of claims to listmodel
		listmodel.add(claim1);
		listmodel.add(claim2);
		
		// get the list of claims
		List<ExpenseClaim> claims = listmodel.getItems();
		
		// start the sorts
		Intent data = getStartDateAscendingIntent();
		Comparator<ExpenseClaim> comparator = (Comparator<ExpenseClaim>)data.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT);
		listmodel.setComparator(comparator);

		// check to see that the comparator is not returning null
		assertNotNull(comparator);
		
		// claim2 should now be in front
		assertEquals(claims.get(0).getStartDate(),claim2.getStartDate());
		
		data = getStartDateDescendingIntent();
		listmodel.setComparator((Comparator<ExpenseClaim>) data.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT));
		
		// claim1 should be first now
		assertEquals(claims.get(0).getStartDate(),claim1.getStartDate());

		data = getCreationDateDescendingIntent();
		listmodel.setComparator((Comparator<ExpenseClaim>) data.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT));
				
		// claim2 should now be in front
		assertEquals(claims.get(0).getCreationDate(),claim2.getCreationDate());
		
		data = getCreationDateAscendingIntent();
		listmodel.setComparator((Comparator<ExpenseClaim>) data.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT));
		
		String test1 = claims.get(0).getCreationDate().toString();
		String test2 = claim1.getCreationDate().toString();
		String test3 = claim2.getCreationDate().toString();
		Log.d("claimsget0", test1);
		Log.d("claim1", test2);
		Log.d("claim2", test3);

		// claim1 should be first now
		assertEquals(claims.get(0),claim1);
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
