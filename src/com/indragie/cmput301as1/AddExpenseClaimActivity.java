package com.indragie.cmput301as1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class AddExpenseClaimActivity extends EditingActivity {
	public static final String EXTRA_EXPENSE_CLAIM_NAME = "com.indragie.cmput301as1.EXPENSE_CLAIM_NAME";
	public static final String EXTRA_EXPENSE_CLAIM_DESCRIPTION = "com.indragie.cmput301as1.EXPENSE_CLAIM_DESCRIPTION";

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
	protected void onCancel() {
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}

	@Override
	protected void onDone() {
		Intent intent = new Intent();

		TextView nameTextView = (TextView)findViewById(R.id.et_name);
		intent.putExtra(EXTRA_EXPENSE_CLAIM_NAME, nameTextView.getText().toString());
		TextView descriptionTextView = (TextView)findViewById(R.id.et_description);
		intent.putExtra(EXTRA_EXPENSE_CLAIM_DESCRIPTION, descriptionTextView.getText().toString());

		setResult(RESULT_OK, intent);
		finish();
	}
}
