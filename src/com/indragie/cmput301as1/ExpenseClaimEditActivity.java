package com.indragie.cmput301as1;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class ExpenseClaimEditActivity extends ListActivity {
	//================================================================================
	// Constants
	//================================================================================
	public static final String EXTRA_CLAIM = "com.indragie.cmput301as1.EXTRA_CLAIM";
	public static final String EXTRA_CLAIM_POSITION = "com.indragie.cmput301as1.EXTRA_CLAIM_POSITION";

	//================================================================================
	// Properties
	//================================================================================

	private ExpenseClaim claim;
	private int claimPosition;

	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		claim = (ExpenseClaim)intent.getSerializableExtra(EXTRA_CLAIM);
		claimPosition = intent.getIntExtra(EXTRA_CLAIM_POSITION, -1);
		setTitle(claim.getName());

		ListView listView = getListView();
		listView.addHeaderView(getListHeaderView());
		listView.setAdapter(new ExpenseClaimArrayAdapter(this, new ArrayList<ExpenseClaim>()));
	}

	private View getListHeaderView() {
		View headerView = getLayoutInflater().inflate(R.layout.activity_claim_header, getListView(), false);

		Boolean editable = claim.isEditable();
		
		EditText nameField = (EditText)headerView.findViewById(R.id.et_name);
		nameField.setText(claim.getName());
		nameField.setEnabled(editable);
		nameField.addTextChangedListener(new OnTextChangedWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				claim.setName(s.toString());
			}
		});

		EditText descriptionField = (EditText)headerView.findViewById(R.id.et_description);
		descriptionField.setText(claim.getDescription());
		descriptionField.setEnabled(editable);
		descriptionField.addTextChangedListener(new OnTextChangedWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				claim.setDescription(s.toString());
			}
		});

		final DateEditText startDateField = (DateEditText)headerView.findViewById(R.id.et_start_date);
		startDateField.setEnabled(editable);
		startDateField.setFocusable(editable);
		startDateField.setDate(claim.getStartDate());
		startDateField.addTextChangedListener(new OnTextChangedWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				claim.setStartDate(startDateField.getDate());
			}
		});
		
		final DateEditText endDateField = (DateEditText)headerView.findViewById(R.id.et_end_date);
		endDateField.setEnabled(editable);
		endDateField.setFocusable(editable);
		endDateField.setDate(claim.getEndDate());
		endDateField.addTextChangedListener(new OnTextChangedWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				claim.setEndDate(endDateField.getDate());
			}
		});

		return headerView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			Intent intent = new Intent();
			intent.putExtra(EXTRA_CLAIM, claim);
			intent.putExtra(EXTRA_CLAIM_POSITION, claimPosition);
			setResult(RESULT_OK, intent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

