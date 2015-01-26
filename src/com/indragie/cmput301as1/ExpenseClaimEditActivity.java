package com.indragie.cmput301as1;

import java.util.Date;
import java.text.DateFormat;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
		claimPosition = intent.getIntExtra(EXTRA_CLAIM_POSITION, 0);
		setTitle(claim.getName());

		ListView listView = getListView();
		listView.addHeaderView(getListHeaderView());
		listView.setAdapter(new ExpenseClaimArrayAdapter(this, new ArrayList<ExpenseClaim>()));
	}

	private View getListHeaderView() {
		View headerView = getLayoutInflater().inflate(R.layout.activity_claim_edit_header, getListView(), false);

		EditText etName = (EditText)headerView.findViewById(R.id.et_name);
		etName.setText(claim.getName());
		etName.setEnabled(claim.isEditable());
		etName.addTextChangedListener(new OnTextChangedWatcher() {
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				claim.setName(s.toString());
			}
		});

		EditText etDescription = (EditText)headerView.findViewById(R.id.et_description);
		etDescription.setText(claim.getDescription());
		etDescription.setEnabled(claim.isEditable());
		etDescription.addTextChangedListener(new OnTextChangedWatcher() {
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				claim.setDescription(s.toString());
			}
		});

		DateFormat formatter = DateFormat.getDateInstance(DateFormat.LONG);
		Date startDate = claim.getStartDate();
		if (startDate != null) {
			TextView tvStartDate = (TextView)headerView.findViewById(R.id.tv_start_date);
			tvStartDate.setText(formatter.format(startDate));
		}

		Date endDate = claim.getEndDate();
		if (endDate != null) {
			TextView tvEndDate = (TextView)headerView.findViewById(R.id.tv_end_date);
			tvEndDate.setText(formatter.format(endDate));
		}
		return headerView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			navigateUpTo(new Intent(this, ExpenseClaimListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//================================================================================
	// EditingActivity
	//================================================================================

	protected void onDone() {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_CLAIM, claim);
		intent.putExtra(EXTRA_CLAIM_POSITION, claimPosition);
		setResult(RESULT_OK, intent);
		finish();
	}

	protected void onCancel() {
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}
}

