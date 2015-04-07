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
import android.util.SparseArray;
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
	private static final int ADD_EXPENSE_CLAIM_REQUEST = 1;
	private static final int EDIT_EXPENSE_CLAIM_REQUEST = 2;
	private static final int SORT_EXPENSE_CLAIM_REQUEST = 3;
	private static final int MANAGE_TAGS_REQUEST = 4;
	private static final int FILTER_TAGS_REQUEST = 5;


	//================================================================================
	// Properties
	//================================================================================
	/**
	 *  ListModel of claim items
	 */
	protected ListModel<ExpenseClaim> listModel;

	/** Activity of the class that created the fragment */

	protected Activity activity;

	/**
	 * Manages the user and associated preferences.
	 */
	protected UserManager userManager;

	/**
	 * List of tags to filter expense claims.
	 */
	private ArrayList<Tag> filteredTagsList = new ArrayList<Tag>();

	/**
	 * List Model of filtered expense claim.
	 */
	protected ListModel<ExpenseClaim> filteredListModel;

	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().setOnItemLongClickListener(new LongClickDeleteListener(activity, new LongClickDeleteListener.OnDeleteListener() {
			@Override
			public void onDelete(int position) {
				showDeleteAlertDialog(position);
			}

			@Override
			public boolean shouldDelete(int position) {
				return true;
			}
		}));
	}

	@Override
	public void onDestroy() {
		listModel.deleteObserver(this);
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			if (resultCode == Activity.RESULT_CANCELED && requestCode == FILTER_TAGS_REQUEST) {
				setListAdapter(new ExpenseClaimArrayAdapter(activity, listModel.getItems()));
				filteredTagsList.clear();
			}
			return;
		}
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
			break;
		case FILTER_TAGS_REQUEST:
			onFilterTagsRequest(data);
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
		if (!filteredTagsList.isEmpty()) {
			setFilteredClaims();
			filteredListModel.setComparator(comparator);
		}
	}

	/**
	 * Adds a expense claim to list model from a intent.
	 * Displays the filteredListModel instead if there are filtered tags.
	 * @param data The intent to get the expense claim from.
	 */
	private void onAddExpenseResult(Intent data) {
		ExpenseClaim claim = (ExpenseClaim)data.getSerializableExtra(ExpenseClaimAddActivity.EXTRA_EXPENSE_CLAIM);
		listModel.add(claim);
		if (!filteredTagsList.isEmpty()) {
			setListAdapter(new ExpenseClaimArrayAdapter(activity, filteredListModel.getItems()));
		}
	}

	/**
	 * Sets a expense claim at a specified position in the list model from a intent.
	 * Displays the filteredListModel instead if there are filtered tags.
	 * If the claim exists in the filteredListModel, we check if it still has the filtered tag.
	 * @param data The intent to get the expense claim from.
	 */
	private void onEditExpenseResult(Intent data) {
		ExpenseClaim claim = (ExpenseClaim)data.getSerializableExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM);
		int position = data.getIntExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_INDEX, -1);
		listModel.set(position, claim);
		if (!filteredTagsList.isEmpty()) {
			setFilteredClaims();
		}
	}

	/**
	 * Sets the listModel used to filteredListModel.
	 * @param data The intent to get the filteredTagsList.
	 */
	@SuppressWarnings("unchecked")
	private void onFilterTagsRequest(Intent data) {
		filteredTagsList = (ArrayList<Tag>)data.getSerializableExtra(FilterTagsActivity.TAG_TO_FILTER);

		setFilteredClaims();
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
		case R.id.action_filter_tags:
			startFilterTagsActivity();
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
		startActivityForResult(manageTagsIntent, MANAGE_TAGS_REQUEST);
	}

	/**
	 * Starts the {@link FilterTagsActivity}
	 */
	private void startFilterTagsActivity() {
		Intent filterTagsIntent = new Intent(activity, FilterTagsActivity.class);
		filterTagsIntent.putExtra(FilterTagsActivity.TAG_TO_FILTER, filteredTagsList);
		startActivityForResult(filterTagsIntent, FILTER_TAGS_REQUEST);
	}


	/**
	 * Sets the list used in ListModel to the returned list of expense claims from the intent. 
	 * @param data The intent to get the list of expense claims from. 
	 */
	/**
	 * Sets the list used in ListModel to the returned list of expense claims from the intent. 
	 * If tag is in the filteredTagsList, we have to update the list to accommodate for the changes.
	 * Displays the filteredListModel instead if there are filtered tags.
	 * @param data The intent to get the list of expense claims from. 
	 */
	@SuppressWarnings("unchecked")
	private void onManageTagsResult(Intent data) {
		ArrayList<ManageTagsActivity.TagMutation> mutations =
				(ArrayList<ManageTagsActivity.TagMutation>)data.getSerializableExtra(ManageTagsActivity.EXTRA_TAG_MUTATIONS);
		SparseArray<ExpenseClaim> modifiedClaims = new SparseArray<ExpenseClaim>();

		for (ManageTagsActivity.TagMutation mutation : mutations) {
			Tag tag = mutation.getOldTag();
			int i = 0;
			for (ExpenseClaim claim : listModel.getItems()) {
				if (!claim.hasTag(tag)) continue;

				switch (mutation.getMutationType()) {
				case DELETE:
					claim.removeTag(tag);
					if (filteredTagsList.contains(tag) && !filteredTagsList.isEmpty()) {
						filteredTagsList.remove(tag);
					}
					break;
				case EDIT:
					int tagIndex = claim.getTags().indexOf(tag);
					claim.setTag(tagIndex, mutation.getNewTag());
					if (filteredTagsList.contains(tag) && !filteredTagsList.isEmpty()) {
						int selectedIndex = filteredTagsList.indexOf(tag);
						filteredTagsList.set(selectedIndex, mutation.getNewTag());
					}
					break;
				}

				modifiedClaims.append(i, claim);
				i++;
			}

		}

		for (int i = 0; i < modifiedClaims.size(); i++) {
			listModel.set(modifiedClaims.keyAt(i), modifiedClaims.valueAt(i));
		}
		if (!filteredTagsList.isEmpty()) {
			setFilteredClaims();
		}

	}

	/**
	 * Prompts the user for confirmation in response to deleting an expense claim.
	 * @param index The index of the expense claim to remove.
	 */
	public void showDeleteAlertDialog(final int index) {
		AlertDialog.Builder openDialog = new AlertDialog.Builder(activity);
		openDialog.setTitle(R.string.action_delete_claim_confirm);

		openDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (!filteredTagsList.isEmpty()) {
					ExpenseClaim claimToRemove = filteredListModel.getItems().get(index);
					listModel.remove(claimToRemove);
					setFilteredClaims();
				} else {
					listModel.remove(index);
				}
			}
		});

		openDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		openDialog.show();
	}

	/**
	 * Checks list of filtered tags. 
	 * If claim contains filtered tag, we keep the claim in our filteredListModel.
	 */
	private void setFilteredClaims() {
		ArrayList<ExpenseClaim> tempClaims = new ArrayList<ExpenseClaim>();

		for (Tag tag: filteredTagsList) {
			for (ExpenseClaim claim: listModel.getItems()) {
				if (claim.hasTag(tag)) {
					if (!tempClaims.contains(claim)) {
						tempClaims.add(claim);
					}
				}
			}
		}
		filteredListModel.replace(tempClaims);
		setListAdapter(new ExpenseClaimArrayAdapter(activity, filteredListModel.getItems()));
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
				ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_USER, userManager.getActiveUser());
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

	}

}