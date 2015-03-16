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
	
private static final String EXPENSE_CLAIM_SORT = null;
	
	@SuppressWarnings("unchecked")
	public void testStart() {
		// startDate in format (YYYY, MM, DD)
		Date date1 = new Date(2000, 01, 01);
		Date date2 = new Date(1995, 01, 01);
		
		// ExpenseClaim(name, description, startDate, endDate, status, creationDate)
		ExpenseClaim claim1 = new ExpenseClaim(null, null, date1, null, null);
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
		listmodel.setComparator((Comparator<ExpenseClaim>) data.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT));

		assertNotNull(data);
		// claim2 should now be in front
		/*
		String test1 = claims.get(0).getStartDate().toString();
		test2 = claim2.getStartDate().toString();
		String test4 = claims.get(1).getStartDate().toString();
		Log.d("claimget0", test1);
		Log.d("claimget1", test4);
		Log.d("claim1", test3);
		Log.d("claim2", test2);

		String test5 = claims.get(0).getCreationDate().toString();
		String test6 = claims.get(1).getCreationDate().toString();
		Log.d("claimget0c", test5);
		Log.d("claimget1c", test6);
		*/
		assertEquals(claims.get(0), claim1);
		
		data = getStartDateDescendingIntent();
		listmodel.setComparator((Comparator<ExpenseClaim>) data.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT));
		
		// claim1 should be first now
		assertEquals(claims.get(1).getStartDate(),claim2.getStartDate());

		data = getCreationDateDescendingIntent();
		listmodel.setComparator((Comparator<ExpenseClaim>) data.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT));
				
		// claim2 should now be in front
		//assertEquals(claims.get(1).getCreationDate(),claim2.getCreationDate());
				
		data = getCreationDateAscendingIntent();
		listmodel.setComparator((Comparator<ExpenseClaim>) data.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT));
				
		// claim1 should be first now
		//assertEquals(claims.get(0).getCreationDate(),claim1.getCreationDate());
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
