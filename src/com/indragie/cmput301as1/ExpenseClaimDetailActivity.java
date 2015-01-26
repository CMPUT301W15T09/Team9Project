package com.indragie.cmput301as1;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class ExpenseClaimDetailActivity extends ListActivity {
	public static final String EXTRA_CLAIM = "com.indragie.cmput301as1.EXTRA_CLAIM";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		ExpenseClaim claim = (ExpenseClaim)getIntent().getSerializableExtra(EXTRA_CLAIM);
		setTitle(claim.getName());
		
		ListView listView = getListView();
		View headerView = getLayoutInflater().inflate(R.layout.activity_claim_detail_header, listView, false);
		
		EditText etName = (EditText)headerView.findViewById(R.id.et_name);
		etName.setText(claim.getName());
		etName.setEnabled(claim.isEditable());
		
		EditText etDescription = (EditText)headerView.findViewById(R.id.et_description);
		etDescription.setText(claim.getDescription());
		etDescription.setEnabled(claim.isEditable());
		
		listView.addHeaderView(headerView);
		setListAdapter(new ExpenseClaimArrayAdapter(this, new ArrayList<ExpenseClaim>()));
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
