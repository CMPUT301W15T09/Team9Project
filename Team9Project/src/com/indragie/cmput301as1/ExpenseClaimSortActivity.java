package com.indragie.cmput301as1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import android.app.ListActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ExpenseClaimSortActivity extends AddActivity {
	
	private static final String EXPENSE_CLAIM_FILENAME = "claims";
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
		
		ExpenseClaimSortType sort = new ExpenseClaimSortType(sort_time, sort_type);
		
		
		adapter = ExpenseClaimListActivity.getAdapter();
		claims = ExpenseClaimListActivity.getClaims();
		
		String type1 = "Date of Travel";
		String type2 = "Order of Entry";
		
		String time1 = "Ascending";
		String time2 = "Descending";
		
		// Date of Travel
		if (type1.equals(sort_type)) {
			if (time1.equals(sort_time)) {
				// Ascending order
				Collections.sort(claims, new StartDateComparator());
				commitClaimsMutation();
			} else {
				// Descending order
				Collections.sort(claims, new StartDateComparator2());
				commitClaimsMutation();
			}
		// Order of Entry
		} else if (type2.equals(sort_type)) {
			if (time1.equals(sort_time)) {
				// Ascending order
				Collections.sort(claims, new CreationDateComparator());
				commitClaimsMutation();
			} else {
				// Descending order
				Collections.sort(claims, new CreationDateComparator2());
				commitClaimsMutation();
			}
		}
		
		Intent intent = new Intent();
		intent.putExtra("SORT_CLAIM", sort);
		return intent;
	}
	
	
	private void commitClaimsMutation() {
		adapter.notifyDataSetChanged();
		
	}

	
}
