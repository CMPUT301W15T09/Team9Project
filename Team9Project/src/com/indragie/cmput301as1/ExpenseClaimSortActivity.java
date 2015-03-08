package com.indragie.cmput301as1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ExpenseClaimSortActivity extends Activity {

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expense_claim_sort, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
