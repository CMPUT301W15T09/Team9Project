package com.indragie.cmput301as1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ExpenseClaimSortActivity extends AddActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_claim_sort);
		
		Button doneButton = (Button) findViewById(R.id.done_sort_button);
		doneButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(ExpenseClaimSortActivity.this, "Sorted!", Toast.LENGTH_SHORT).show();
				
				// now we want to grab the sort type from the spinners
				Spinner sort_type_spinner = (Spinner) findViewById(R.id.sort_type_spinner);
				String sort_type = sort_type_spinner.getSelectedItem().toString();
				
				Spinner sort_time_spinner = (Spinner) findViewById(R.id.sort_time_spinner);
				String sort_time = sort_time_spinner.getSelectedItem().toString();
				
				// now we want to make it set in our expense claim sort class
				new ExpenseClaimSortType(sort_time, sort_type);
				
				// now we need a way to call our compare function to work
				
				finish();
			}
		});
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
		
		Intent intent = new Intent();
		intent.putExtra("SORT_CLAIM", sort);
		return intent;
	}

	
	
}
