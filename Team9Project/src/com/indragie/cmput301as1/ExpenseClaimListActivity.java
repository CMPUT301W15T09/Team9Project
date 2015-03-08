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

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * An activity that presents a list of expense claims.
 */
public class ExpenseClaimListActivity extends ListActivity implements TypedObserver<List<ExpenseClaim>> {
	//================================================================================
	// Constants
	//================================================================================
	
	private static final int ADD_EXPENSE_CLAIM_REQUEST = 1;
	private static final int EDIT_EXPENSE_CLAIM_REQUEST = 2;
	private static final String EXPENSE_CLAIM_FILENAME = "claims";

	//================================================================================
	// Properties
	//================================================================================
	
	private ExpenseClaimList claimsList;
	private int longPressedItemIndex;

	//================================================================================
	// Activity Callbacks
	//================================================================================
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		claimsList = new ExpenseClaimList(EXPENSE_CLAIM_FILENAME, this);
		claimsList.addObserver(this);
		setListAdapter(new ExpenseClaimArrayAdapter(this, claimsList.getExpenseClaims()));
		
		final ActionMode.Callback longClickCallback = new ActionMode.Callback() {
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
				case R.id.action_delete:
					claimsList.removeExpenseClaim(longPressedItemIndex);
					mode.finish();
					return true;
				default:
					return false;
				}
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				mode.getMenuInflater().inflate(R.menu.contextual_delete, menu);
				return true;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}
		};
		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				longPressedItemIndex = position;
				startActionMode(longClickCallback);
				return true;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) return;
		switch (requestCode) {
		case ADD_EXPENSE_CLAIM_REQUEST:
			onAddExpenseResult(data);
			break;
		case EDIT_EXPENSE_CLAIM_REQUEST:
			onEditExpenseResult(data);
			break;
		}
	}
	
	private void onAddExpenseResult(Intent data) {
		ExpenseClaim claim = (ExpenseClaim)data.getSerializableExtra(ExpenseClaimAddActivity.EXTRA_EXPENSE_CLAIM);
		claimsList.addExpenseClaim(claim);
	}
	
	private void onEditExpenseResult(Intent data) {
		ExpenseClaim claim = (ExpenseClaim)data.getSerializableExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM);
		int position = data.getIntExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_POSITION, -1);
		claimsList.setExpenseClaim(position, claim);
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
			startAddExpenseClaimActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void startAddExpenseClaimActivity() {
		Intent addIntent = new Intent(this, ExpenseClaimAddActivity.class);
		startActivityForResult(addIntent, ADD_EXPENSE_CLAIM_REQUEST);
	}

	//================================================================================
	// ListView Callbacks
	//================================================================================
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		startEditExpenseClaimActivity(position);
	}
	
	private void startEditExpenseClaimActivity(int position) {
		Intent editIntent = new Intent(this, ExpenseClaimDetailActivity.class);
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM, claimsList.getExpenseClaims().get(position));
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_POSITION, position);
		startActivityForResult(editIntent, EDIT_EXPENSE_CLAIM_REQUEST);
	}

	//================================================================================
	// TypedObserver
	//================================================================================
	
	@Override
	public void update(TypedObservable<List<ExpenseClaim>> o, List<ExpenseClaim> claims) {
		setListAdapter(new ExpenseClaimArrayAdapter(this, claims));
	}
}
