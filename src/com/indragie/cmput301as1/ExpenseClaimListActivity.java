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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

/**
 * An activity that presents a list of expense claims.
 */
public class ExpenseClaimListActivity extends ListActivity {
	//================================================================================
	// Constants
	//================================================================================
	private static final int ADD_EXPENSE_CLAIM_REQUEST = 1;
	private static final String EXPENSE_CLAIM_FILENAME = "claims";

	//================================================================================
	// Properties
	//================================================================================
	private ArrayList<ExpenseClaim> claims;
	private ExpenseClaimArrayAdapter adapter;

	//================================================================================
	// Activity Callbacks
	//================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		claims = loadExpenseClaims();
		adapter = new ExpenseClaimArrayAdapter(this, claims);
		setListAdapter(adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) return;

		if (requestCode == ADD_EXPENSE_CLAIM_REQUEST) {
			String name = data.getStringExtra(AddExpenseClaimActivity.EXTRA_EXPENSE_CLAIM_NAME);
			if (name == null) {
				name = getResources().getString(R.string.default_name);
			}
			String description = data.getStringExtra(AddExpenseClaimActivity.EXTRA_EXPENSE_CLAIM_DESCRIPTION);
			addExpenseClaim(name, description);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.expense_claim_list, menu);
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
		detailIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_CLAIM, claims.get(position));
		startActivity(detailIntent);
	}

	//================================================================================
	// Claims
	//================================================================================

	private void openAddExpenseClaim() {
		Intent addIntent = new Intent(this, AddExpenseClaimActivity.class);
		startActivityForResult(addIntent, ADD_EXPENSE_CLAIM_REQUEST);
	}

	private void addExpenseClaim(String name, String description) {
		ExpenseClaim claim = new ExpenseClaim(name, description);
		claims.add(claim);
		adapter.notifyDataSetChanged();
		saveExpenseClaims();
	}
	
	private void saveExpenseClaims() {
		try {
			FileOutputStream fos = openFileOutput(EXPENSE_CLAIM_FILENAME, 0);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(claims);
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<ExpenseClaim> loadExpenseClaims() {
		try {
			FileInputStream fis = openFileInput(EXPENSE_CLAIM_FILENAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			ois.close();
			fis.close();
			return (ArrayList<ExpenseClaim>)obj;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ExpenseClaim>();
		}
	}
}
