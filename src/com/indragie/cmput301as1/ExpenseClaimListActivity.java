/* 
 * Copyright (C) 2015 Indragie Karunaratne
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.indragie.cmput301as1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * An activity that presents a list of expense claims.
 */
public class ExpenseClaimListActivity extends ListActivity {
	//================================================================================
	// Constants
	//================================================================================
	private static final int ADD_CLAIM_REQUEST = 1;

	//================================================================================
	// Properties
	//================================================================================
	private ArrayList<ExpenseClaim> claims;
	private ArrayAdapter<ExpenseClaim> adapter;

	//================================================================================
	// Activity Callbacks
	//================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		claims = new ArrayList<ExpenseClaim>();
		adapter = new ArrayAdapter<ExpenseClaim>(
				this,
				android.R.layout.simple_list_item_activated_2,
				android.R.id.text1,
				claims);
		setListAdapter(adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) return;

		if (requestCode == ADD_CLAIM_REQUEST) {
			Bundle extras = data.getExtras();
			String name = extras.getString(AddExpenseClaimActivity.EXTRA_EXPENSE_NAME, getResources().getString(R.string.default_name));
			String description = extras.getString(AddExpenseClaimActivity.EXTRA_EXPENSE_DESCRIPTION);
			addExpenseClaim(name, description);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.expense_claim_list, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_claim:
			openAddExpenseClaim();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	//================================================================================
	// ListView Callbacks
	//================================================================================
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		Intent detailIntent = new Intent(this, ExpenseClaimDetailActivity.class);
		detailIntent.putExtra(ExpenseClaimDetailFragment.ARG_ITEM_ID, id);
		startActivity(detailIntent);
	}

	//================================================================================
	// Claims
	//================================================================================

	private void openAddExpenseClaim() {
		Intent addIntent = new Intent(this, AddExpenseClaimActivity.class);
		startActivityForResult(addIntent, ADD_CLAIM_REQUEST);
	}

	private void addExpenseClaim(String name, String description) {
		ExpenseClaim claim = new ExpenseClaim(name, description);
		claims.add(claim);
		adapter.notifyDataSetChanged();
	}
}
