package com.indragie.cmput301as1;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AddExpenseClaimActivity extends EditingActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_expense_claim);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_expense_claim, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCancel() {
		
	}
	
	@Override
	protected void onDone() {
		
	}
}
