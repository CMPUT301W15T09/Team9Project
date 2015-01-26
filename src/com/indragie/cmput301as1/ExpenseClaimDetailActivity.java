package com.indragie.cmput301as1;

import java.util.Date;
import java.text.DateFormat;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ExpenseClaimDetailActivity extends ListActivity {
	public static final String EXTRA_CLAIM = "com.indragie.cmput301as1.EXTRA_CLAIM";
	private ExpenseClaim claim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		claim = (ExpenseClaim)getIntent().getSerializableExtra(EXTRA_CLAIM);
		setTitle(claim.getName());
		setupListHeaderView();
		setListAdapter(new ExpenseClaimArrayAdapter(this, new ArrayList<ExpenseClaim>()));
	}
	
	private void setupListHeaderView() {
		ListView listView = getListView();
		View headerView = getLayoutInflater().inflate(R.layout.activity_claim_detail_header, listView, false);
		
		EditText etName = (EditText)headerView.findViewById(R.id.et_name);
		etName.setText(claim.getName());
		etName.setEnabled(claim.isEditable());
		
		EditText etDescription = (EditText)headerView.findViewById(R.id.et_description);
		etDescription.setText(claim.getDescription());
		etDescription.setEnabled(claim.isEditable());
		
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
		
		listView.addHeaderView(headerView);
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
}
