package com.indragie.cmput301as1;

import org.joda.money.Money;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class ExpenseItemEditActivity extends ExpenseItemAddActivity {
	//================================================================================
	// Constants
	//================================================================================
	public static final String EXTRA_EXPENSE_ITEM_POSITION = "com.indragie.cmput301as1.EXPENSE_ITEM_POSITION";
	public static final String EXTRA_EXPENSE_ITEM_EDITABLE = "com.indragie.cmput301as1.EXPENSE_ITEM_EDITABLE";
	
	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// The superclass shows a custom action bar with Cancel and Done buttons by default
		// Disable that appearance here because this activity needs to show a custom menu in the bar.
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);

		Intent intent = getIntent();
		ExpenseItem item = (ExpenseItem)getIntent().getSerializableExtra(EXTRA_EXPENSE_ITEM);
		Boolean editable = intent.getBooleanExtra(EXTRA_EXPENSE_ITEM_EDITABLE, false);
		
		setTitle(item.getName());
		setupFields(item, editable);
	}

	private void setupFields(ExpenseItem item, Boolean editable) {
		Money amount = item.getAmount();

		nameField.setText(item.getName());
		nameField.setEnabled(editable);
		
		descriptionField.setText(item.getDescription());
		descriptionField.setEnabled(editable);
		
		SpinnerUtils.setSelectedItem(currencySpinner, amount.getCurrencyUnit().toString());
		currencySpinner.setEnabled(editable);
		
		amountField.setText(amount.getAmount().toString());
		amountField.setEnabled(editable);
		
		dateField.setDate(item.getDate());
		dateField.setEnabled(editable);
		
		SpinnerUtils.setSelectedItem(categorySpinner, item.getCategory());
		categorySpinner.setEnabled(editable);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onDone();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed() {
		// Changes should persist even when the back button is pressed,
		// since this is for editing and not adding.
		onDone();
	}

	//================================================================================
	// ExpenseItemAddActivity Overrides
	//================================================================================

	@Override
	protected Intent getResultIntent()  {
		Intent intent = super.getResultIntent();
		intent.putExtra(EXTRA_EXPENSE_ITEM_POSITION, getIntent().getIntExtra(EXTRA_EXPENSE_ITEM_POSITION, -1));
		return intent;
	}
}
