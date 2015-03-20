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


import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * An activity that presents a list of expense claims.
 */
public class ExpenseClaimListActivity extends ListActivity implements TypedObserver<List<ExpenseClaim>> {
	//================================================================================
	// Constants
	//================================================================================

	private static final int ADD_EXPENSE_CLAIM_REQUEST = 1;
	private static final int EDIT_EXPENSE_CLAIM_REQUEST = 2;
	private static final int SORT_EXPENSE_CLAIM_REQUEST = 3;
	private static final String EXPENSE_CLAIM_FILENAME = "claims";
	private static final String PREFERENCE = "PREFERENCE";

	//================================================================================
	// Properties
	//================================================================================

	/**
	 * List model of expense claim.
	 */
	private ListModel<ExpenseClaim> listModel;
	
	/**
	 * Index of a item that is long pressed.
	 */
	private int longPressedItemIndex;
	
	/**
	 * Active user.
	 */
	private User user;

	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		checkFirstRun();
		setUserFromPreferences();

		listModel = new ListModel<ExpenseClaim>(EXPENSE_CLAIM_FILENAME, this);
		listModel.addObserver(this);
		setListAdapter(new ExpenseClaimArrayAdapter(this, listModel.getItems()));
		
		final ActionMode.Callback longClickCallback = new ActionMode.Callback() {
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
				case R.id.action_delete:
					listModel.remove(longPressedItemIndex);
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
	
	private void setUserFromPreferences() {
		SharedPreferences prefs = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
		user = new User(prefs.getString("name", "USER DOES NOT EXIST"), prefs.getInt("id", -1));
	}

	@Override
	public void onDestroy() {
		listModel.deleteObserver(this);
		super.onDestroy();
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
		case SORT_EXPENSE_CLAIM_REQUEST:
			onSortExpenseResult(data);
			break;
		}
	}
	
	/**
	 * Changes the sorting mode based on a comparator chosen by {@link ExpenseClaimSortActivity}
	 * @param data The intent to get the comparator from.
	 */
	@SuppressWarnings("unchecked")
	private void onSortExpenseResult(Intent data) {
		Comparator<ExpenseClaim> comparator = (Comparator<ExpenseClaim>)data.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT);
		listModel.setComparator(comparator);
	}

	/**
	 * Adds a expense claim to list model from a intent.
	 * @param data The intent to get the expense claim from.
	 */
	private void onAddExpenseResult(Intent data) {
		ExpenseClaim claim = (ExpenseClaim)data.getSerializableExtra(ExpenseClaimAddActivity.EXTRA_EXPENSE_CLAIM);
		listModel.add(claim);
	}
	
	/**
	 * Sets a expense claim at a sepcified position in the list model from a intent.
	 * @param data The intent to get the expense claim from.
	 */
	private void onEditExpenseResult(Intent data) {
		ExpenseClaim claim = (ExpenseClaim)data.getSerializableExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM);
		int position = data.getIntExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_INDEX, -1);
		listModel.set(position, claim);
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
		case R.id.action_sort_claim:
			startSortExpenseClaimActivity();
			return true;
		case R.id.action_manage_tags:
			startManageTagsActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	/**
	 * Starts the {@link ExpenseClaimAddActivity}
	 */
	private void startAddExpenseClaimActivity() {
		Intent addIntent = new Intent(this, ExpenseClaimAddActivity.class);
		addIntent.putExtra(ExpenseClaimAddActivity.EXTRA_EXPENSE_CLAIM_USER, user);
		startActivityForResult(addIntent, ADD_EXPENSE_CLAIM_REQUEST);
	}
	
	/**
	 * Starts the {@link ExpenseClaimSortActivity}
	 */
	private void startSortExpenseClaimActivity() {
		Intent intent = new Intent(this, ExpenseClaimSortActivity.class);
		startActivityForResult(intent, SORT_EXPENSE_CLAIM_REQUEST);
	}
		
	/**
	 * Starts the {@link ManageTagsActivity}
	 */
	private void startManageTagsActivity() {
		Intent manageTagsIntent = new Intent(this, ManageTagsActivity.class);
		startActivity(manageTagsIntent);
	}


	public void checkFirstRun() {
		boolean isFirstRun = getSharedPreferences(PREFERENCE, MODE_PRIVATE).getBoolean("isFirstRun", true);
		if (isFirstRun){ 
			// http://www.androidsnippets.com/prompt-user-input-with-an-alertdialog
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setCancelable(false);

			alert.setTitle("Username");
			alert.setMessage("Please enter your name:");

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String value = input.getText().toString();

					if(value != null && !value.isEmpty()){
						SharedPreferences.Editor editor = getSharedPreferences(PREFERENCE, MODE_PRIVATE).edit();
						editor.putString("name", value);
						editor.putInt("id", 1);
						editor.putBoolean("isFirstRun", false);
						editor.apply();
						setUserFromPreferences();
					}
					else{
						checkFirstRun();
						Toast.makeText(getApplicationContext(), "You must enter a username", Toast.LENGTH_LONG).show(); 
					}

				}
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					checkFirstRun();
					Toast.makeText(getApplicationContext(), "You must enter a username", Toast.LENGTH_LONG).show();
				}
			});

			alert.show();
		}
	}

	//================================================================================
	// ListView Callbacks
	//================================================================================

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		startEditExpenseClaimActivity(position);
	}
	
	/**
	 * Calls the intent to edit a expense claim at a specified position.
	 * @param position The position of the expense claim to edit.
	 */
	private void startEditExpenseClaimActivity(int position) {
		Intent editIntent = new Intent(this, ExpenseClaimDetailActivity.class);
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM, listModel.getItems().get(position));
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_INDEX, position);
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_USER, user);
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
