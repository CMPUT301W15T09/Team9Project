package com.indragie.cmput301as1;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import android.view.MenuItem;

public class ExpenseClaimDetailActivity extends Activity {
	public static final String EXTRA_CLAIM = "com.indragie.cmput301as1.EXTRA_CLAIM";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim_detail);
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
