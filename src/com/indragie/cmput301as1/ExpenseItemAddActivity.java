package com.indragie.cmput301as1;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ExpenseItemAddActivity extends EditingActivity {
	private Spinner categorySpinner;
	private Spinner currencySpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_item_add);
		
		categorySpinner = (Spinner)findViewById(R.id.sp_category);
		configureSpinner(categorySpinner, R.array.categories_array);
		
		currencySpinner = (Spinner)findViewById(R.id.sp_currency);
		configureSpinner(currencySpinner, R.array.currency_array);
	}
	
	private void configureSpinner(Spinner spinner, int resourceID) {
		// From http://developer.android.com/guide/topics/ui/controls/spinner.html
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        resourceID, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	@Override
	protected void onCancel() {
		finish();
	}

	@Override
	protected void onDone() {
		finish();
	}
}
