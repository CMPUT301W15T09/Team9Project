package com.indragie.cmput301as1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class ExpenseClaimAddActivity extends EditingActivity {
	public static final String EXTRA_EXPENSE_CLAIM = "com.indragie.cmput301as1.EXPENSE_CLAIM";
	
	// Cache references to these views so that they don't have to be found
	// a second time when the ExpenseClaim object is created.
	private DateEditText startDateField;
	private DateEditText endDateField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_expense_claim);
		
		startDateField = (DateEditText)findViewById(R.id.et_start_date);
		endDateField = (DateEditText)findViewById(R.id.et_end_date);
	}

	@Override
	protected void onCancel() {
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}

	@Override
	protected void onDone() {
		EditText nameField = (EditText)findViewById(R.id.et_name);
		EditText descriptionField = (EditText)findViewById(R.id.et_description);
		ExpenseClaim claim = new ExpenseClaim(
			nameField.getText().toString(), 
			descriptionField.getText().toString(), 
			startDateField.getDate(), 
			endDateField.getDate()
		);
		
		Intent intent = new Intent();
		intent.putExtra(EXTRA_EXPENSE_CLAIM, claim);
		setResult(RESULT_OK, intent);
		finish();
	}
}
