package com.indragie.cmput301as1;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Spinner;

public class ExpenseClaimSortActivity extends AddActivity {
	
	private ArrayList<ExpenseClaim> claims;
	private ExpenseClaimArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_claim_sort);

	}
	
	@Override
	protected void onCancel() {
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}

	@Override
	protected void onDone() {
		setResult(RESULT_OK, getSortType());
		finish();
	}
	
	private Intent getSortType() {
		// now we want to grab the sort type from the spinners
		Spinner sort_type_spinner = (Spinner) findViewById(R.id.sort_type_spinner);
		String sort_type = sort_type_spinner.getSelectedItem().toString();
		
		Spinner sort_time_spinner = (Spinner) findViewById(R.id.sort_time_spinner);
		String sort_time = sort_time_spinner.getSelectedItem().toString();

		adapter = ExpenseClaimListActivity.getAdapter();
		claims = ExpenseClaimListActivity.getClaims();
		
		String type1 = "Date of Travel";
		String type2 = "Order of Entry";
		
		String time1 = "Ascending";
		// String time2 = "Descending";
		
		// Date of Travel
		if (type1.equals(sort_type)) {
			if (time1.equals(sort_time)) {
				// Ascending order
				Collections.sort(claims, new StartDateAscendingComparator());
				commitClaimsMutation();
			} else {
				// Descending order
				Collections.sort(claims, new StartDateDescendingComparator());
				commitClaimsMutation();
			}
		// Order of Entry
		} else if (type2.equals(sort_type)) {
			if (time1.equals(sort_time)) {
				// Ascending order
				Collections.sort(claims, new CreationDateAscendingComparator());
				commitClaimsMutation();
			} else {
				// Descending order
				Collections.sort(claims, new CreationDateDescendingComparator());
				commitClaimsMutation();
			}
		}
		
		Intent intent = new Intent();
		return intent;
	}
	
	
	private void commitClaimsMutation() {
		adapter.notifyDataSetChanged();
		
	}

	
}
