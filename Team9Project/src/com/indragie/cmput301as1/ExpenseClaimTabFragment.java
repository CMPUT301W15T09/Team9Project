/* 
 * Copyright (C) 2015 Nic Carroll
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
import java.util.Comparator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


/**
 * A fragment base to implement tabs
 */
public class ExpenseClaimTabFragment extends ListFragment implements
TypedObserver<CollectionMutation<ExpenseClaim>> {

	//================================================================================
	// Constants
	//================================================================================

	protected static final String EXPENSE_CLAIM_FILENAME = "claims";
	protected static final String EXPENSE_APPROVAL_FILENAME = "approval";
	private static final String PREFERENCE = "PREFERENCE";
	private static final int ADD_EXPENSE_CLAIM_REQUEST = 1;
	private static final int EDIT_EXPENSE_CLAIM_REQUEST = 2;
	private static final int SORT_EXPENSE_CLAIM_REQUEST = 3;
	private static final int MANAGE_TAGS_REQUEST = 4;


	//================================================================================
	// Properties
	//================================================================================
	/**
	 *  ListModel of claim items
	 */
	protected ListModel<ExpenseClaim> listModel;

	/**
	 * Index of a item that is long pressed.
	 */
	private int longPressedItemIndex;

	/**
	 * Active user.
	 */
	protected User user;

	/** Activity of the class that created the fragment */

	protected Activity activity;

	/**
	 * Manages the user and associated preferences.
	 */
	protected UserManager userManager;

	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		activity = getActivity();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		userManager = new UserManager(activity);
		user = userManager.getActiveUser();
		setHasOptionsMenu(true);

		getListView().setOnItemLongClickListener(
				new LongClickDeleteListener(activity, new LongClickDeleteListener.OnDeleteListener() {
					@Override
					public void onDelete(int position) {
						if (getListView().getItemAtPosition(position) == null) {
							return;
						}
					}

					@Override
					public boolean shouldDelete(int position) {
						// TODO Auto-generated method stub
						return false;
					}}));
	}

	@Override
	public void onDestroy() {
		listModel.deleteObserver(this);
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;
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
		case MANAGE_TAGS_REQUEST:
			onManageTagsResult(data);
		}
	}

	/**
	 * Changes the sorting mode based on a comparator chosen by
	 * {@link ExpenseClaimSortActivity}
	 * 
	 * @param data
	 *            The intent to get the comparator from.
	 */


	@SuppressWarnings("unchecked")
	private void onSortExpenseResult(Intent data) {
		Comparator<ExpenseClaim> comparator = (Comparator<ExpenseClaim>) data
				.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT);
		listModel.setComparator(comparator);

	}

	/**
	 * Adds a expense claim to list model from a intent.
	 * 
	 * @param data
	 *            The intent to get the expense claim from.
	 */
	private void onAddExpenseResult(Intent data) {
		ExpenseClaim claim = (ExpenseClaim) data
				.getSerializableExtra(ExpenseClaimAddActivity.EXTRA_EXPENSE_CLAIM);
		listModel.add(claim);

	}

	/**
	 * Sets a expense claim at a specified position in the list model from a
	 * intent.
	 * 
	 * @param data
	 *            The intent to get the expense claim from.
	 */
	private void onEditExpenseResult(Intent data) {
		ExpenseClaim claim = (ExpenseClaim) data
				.getSerializableExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM);
		int position = data.getIntExtra(
				ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_INDEX, -1);
		listModel.set(position, claim);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater item) {
		activity.getMenuInflater()
		.inflate(R.menu.expense_claim_list, menu);
		super.onCreateOptionsMenu(menu, item);
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
		Intent addIntent = new Intent(activity, ExpenseClaimAddActivity.class);
		addIntent.putExtra(ExpenseClaimAddActivity.EXTRA_EXPENSE_CLAIM_USER, userManager.getActiveUser());
		startActivityForResult(addIntent, ADD_EXPENSE_CLAIM_REQUEST);
	}

	/**
	 * Starts the {@link ExpenseClaimSortActivity}
	 */
	private void startSortExpenseClaimActivity() {
		Intent intent = new Intent(activity, ExpenseClaimSortActivity.class);
		startActivityForResult(intent, SORT_EXPENSE_CLAIM_REQUEST);
	}

	/**
	 * Starts the {@link ManageTagsActivity}
	 */
	private void startManageTagsActivity() {
		Intent manageTagsIntent = new Intent(activity, ManageTagsActivity.class);
		manageTagsIntent.putExtra(ManageTagsActivity.CLAIM_LIST, new ArrayList<ExpenseClaim>(listModel.getItems()));
		startActivityForResult(manageTagsIntent, MANAGE_TAGS_REQUEST);
	}


	/**
	 * Sets the list used in ListModel to the returned list of expense claims from the intent. 
	 * @param data The intent to get the list of expense claims from. 
	 */
	@SuppressWarnings("unchecked")
	private void onManageTagsResult(Intent data) {
		ArrayList<ExpenseClaim> claimList = (ArrayList<ExpenseClaim>)data.getSerializableExtra(ManageTagsActivity.CLAIM_LIST);
		listModel.replace(claimList);
	}

	/**
	 * Prompts the user for confirmation in response to deleting an expense
	 * claim.
	 */
	public void startDeleteAlertDialog() {
		AlertDialog.Builder openDialog = new AlertDialog.Builder(activity);
		openDialog.setTitle(R.string.action_delete_claim_confirm);

		openDialog.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listModel.remove(longPressedItemIndex);

			}
		});

		openDialog.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		openDialog.show();
	}

	// ================================================================================
	// ListView Callbacks
	// ================================================================================

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		startEditExpenseClaimActivity(position);
	}


	/**
	 * Calls the intent to edit a expense claim at a specified position.
	 * 
	 * @param position
	 *            The position of the expense claim to edit.
	 */
	private void startEditExpenseClaimActivity(int position) {
		Intent editIntent = new Intent(activity,
				ExpenseClaimDetailActivity.class);
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM,
				listModel.getItems().get(position));
		editIntent.putExtra(
				ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_INDEX, position);
		editIntent.putExtra(
				ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_USER, user);
		startActivityForResult(editIntent, EDIT_EXPENSE_CLAIM_REQUEST);
	}

	// ================================================================================
	// TypedObserver
	// ================================================================================

	@Override
	public void update(TypedObservable<CollectionMutation<ExpenseClaim>> o,
			CollectionMutation<ExpenseClaim> arg) {
		setListAdapter(new ExpenseClaimArrayAdapter(activity,
				listModel.getItems()));
		// TODO Auto-generated method stub

	}

}